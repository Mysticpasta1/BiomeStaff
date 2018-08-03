/*
 * This file is part of Biome Staff Mod. Copyright (c) 2018, p455w0rd
 * (aka TheRealp455w0rd), All rights reserved unless otherwise stated.
 *
 * Biome Staff Mod is free software: you can redistribute it and/or
 * modify it under the terms of the MIT License.
 *
 * Biome Staff Mod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the MIT
 * License for more details.
 *
 * You should have received a copy of the MIT License along with Biome
 * Staff Mod Crafting Terminal. If not, see
 * <https://opensource.org/licenses/MIT>.
 */
package p455w0rd.biomestaff.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.biomestaff.init.ModGlobals;
import p455w0rd.biomestaff.init.ModNetworking;
import p455w0rd.biomestaff.network.PacketSyncBiomeStaff;
import p455w0rd.biomestaff.network.PacketUpdateChunkRender;
import p455w0rd.biomestaff.util.BiomeStaffUtil;

/**
 * @author p455w0rd
 *
 */
public class ItemBiomeStaff extends Item {

	public static final String TAG_BIOME = "biome";

	private static final ResourceLocation REGISTRY_NAME = new ResourceLocation(ModGlobals.MODID, "biome_staff");

	public ItemBiomeStaff() {
		setUnlocalizedName(REGISTRY_NAME.getResourcePath().toString());
		setRegistryName(REGISTRY_NAME);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			items.add(new ItemStack(this));
			items.addAll(BiomeStaffUtil.getAllBiomeStaffs());
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote && hand == EnumHand.MAIN_HAND && player.isSneaking()) {
			ItemStack heldStack = player.getHeldItemMainhand();
			if (heldStack.hasTagCompound()) {
				NBTTagCompound nbt = heldStack.getTagCompound();
				if (nbt.hasKey(TAG_BIOME, Constants.NBT.TAG_BYTE)) {
					nbt.removeTag(TAG_BIOME);
					ModNetworking.getInstance().sendTo(new PacketSyncBiomeStaff(nbt), (EntityPlayerMP) player);
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, heldStack);
				}
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldStack = player.getHeldItemMainhand();
		if (!heldStack.hasTagCompound()) {
			heldStack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound tag = heldStack.getTagCompound();
		int size = 7;
		int rad = size / 2;
		if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
			if (!heldStack.isEmpty()) {
				if (player.isSneaking()) {
					Chunk chunk = world.getChunkFromBlockCoords(pos);
					int relBlockX = pos.getX() & 15;
					int relBlockZ = pos.getZ() & 15;
					byte biomeType = chunk.getBiomeArray()[relBlockZ << 4 | relBlockX];
					if (!tag.hasKey(TAG_BIOME, Constants.NBT.TAG_BYTE) || (tag.hasKey(TAG_BIOME, Constants.NBT.TAG_BYTE) && tag.getByte(TAG_BIOME) != biomeType)) {
						tag.setByte(TAG_BIOME, biomeType);
						heldStack.setTagCompound(tag);
						ModNetworking.getInstance().sendTo(new PacketSyncBiomeStaff(heldStack.getTagCompound()), (EntityPlayerMP) player);
					}
				}
				else if (tag.hasKey(TAG_BIOME)) {
					byte biome = tag.getByte(TAG_BIOME);
					for (int ix = pos.getX() - rad; ix <= pos.getX() + rad; ++ix) {
						for (int iz = pos.getZ() - rad; iz <= pos.getZ() + rad; ++iz) {
							int relBlockX = ix & 15;
							int relBlockZ = iz & 15;
							Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(ix, pos.getY(), iz));
							byte[] byteArray = chunk.getBiomeArray();
							byte currentByte = byteArray[relBlockZ << 4 | relBlockX];
							if (currentByte != biome) {
								byteArray[relBlockZ << 4 | relBlockX] = biome;
								chunk.setBiomeArray(byteArray);
								chunk.setModified(true);
							}
						}
					}
					ModNetworking.getInstance().sendTo(new PacketUpdateChunkRender(pos, rad, biome), (EntityPlayerMP) player);
				}
			}
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_BIOME, Constants.NBT.TAG_BYTE)) {
			Biome biome = Biome.getBiome(stack.getTagCompound().getByte(TAG_BIOME) & 255);
			if (biome != null) {
				tooltip.add("Biome: " + biome.getBiomeName());
			}
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		String biomeName = "";
		Biome biome = BiomeStaffUtil.getBiomeFromStaff(stack);
		if (biome != null) {
			biomeName = biome.getBiomeName();
			if (biomeName != null && !biomeName.isEmpty()) {
				biomeName = " - " + biomeName;
			}
		}
		return I18n.translateToLocal(stack.getItem().getUnlocalizedName() + ".name").trim() + "" + biomeName;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(REGISTRY_NAME, "inventory"));
	}

}
