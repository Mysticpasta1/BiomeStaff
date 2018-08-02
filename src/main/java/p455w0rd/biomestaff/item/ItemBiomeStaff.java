package p455w0rd.biomestaff.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
import p455w0rd.biomestaff.BiomeStaff;
import p455w0rd.biomestaff.init.ModGlobals;

/**
 * @author p455w0rd
 *
 */
public class ItemBiomeStaff extends Item {

	private static final ResourceLocation REGISTRY_NAME = new ResourceLocation(ModGlobals.MODID, "biome_staff");

	public ItemBiomeStaff() {
		setUnlocalizedName(REGISTRY_NAME.getResourcePath().toString());
		setRegistryName(REGISTRY_NAME);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND) {
			return EnumActionResult.PASS;
		}
		ItemStack heldStack = player.getHeldItemMainhand();
		if (!heldStack.hasTagCompound()) {
			heldStack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound tag = heldStack.getTagCompound();
		int size = tag.hasKey("actionSize", Constants.NBT.TAG_INT) ? tag.getInteger("actionSize") : 7;
		int rad = size / 2;

		if (!heldStack.isEmpty()) {
			if (player.isSneaking()) {
				Chunk chunk = world.getChunkFromBlockCoords(pos);
				int relBlockX = pos.getX() & 15;
				int relBlockZ = pos.getZ() & 15;
				byte biomeType = chunk.getBiomeArray()[relBlockZ << 4 | relBlockX];
				tag.setByte("sampledBiome", biomeType);
				heldStack.setTagCompound(tag);
				/*
				int size = tag.hasKey("actionSize") ? tag.getInteger("actionSize") : 7;
				switch (size) {
				case 1: {
					size = 3;
					break;
				}
				case 3: {
					size = 5;
					break;
				}
				case 5: {
					size = 7;
					break;
				}
				case 7: {
					size = 9;
					break;
				}
				case 9: {
					size = 1;
					break;
				}
				default: {
					size = 7;
				}
				}
				tag.setInteger("actionSize", size);
				//TranslateUtils.chat((EntityPlayer) player, (String) "item.wand.chat.actionSizeChanged", (Object[]) new Object[] {
				//		size
				//});
				player.sendMessage(new TextComponentString("Radius: "+size));
				*/
			}
			else if (tag.hasKey("sampledBiome")) {
				byte biome = tag.getByte("sampledBiome");
				//int totalDamage = 0;
				for (int ix = pos.getX() - rad; ix <= pos.getX() + rad; ++ix) {
					for (int iz = pos.getZ() - rad; iz <= pos.getZ() + rad; ++iz) {
						int relBlockX = ix & 15;
						int relBlockZ = iz & 15;

						Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(ix, pos.getY(), iz));
						byte[] byteArray = chunk.getBiomeArray();
						byteArray[relBlockZ << 4 | relBlockX] = biome;
						chunk.setBiomeArray(byteArray);
						chunk.setModified(true);
						if (world.isRemote) {

						}
						BiomeStaff.PROXY.updateChunkRendering(ix, iz);
						//++totalDamage;
					}
				}
				//world.markBlockRangeForRenderUpdate(new BlockPos(pos.getX() - rad, 0, pos.getZ() - rad), new BlockPos(pos.getX() + rad, 255, pos.getZ() + rad));
			}
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("sampledBiome", Constants.NBT.TAG_BYTE)) {
			Biome biome = Biome.getBiome(stack.getTagCompound().getByte("sampledBiome") & 255);
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
		return I18n.translateToLocal(stack.getItem().getUnlocalizedName() + "_" + stack.getItemDamage() + ".name").trim();
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(REGISTRY_NAME, "inventory"));
	}

}
