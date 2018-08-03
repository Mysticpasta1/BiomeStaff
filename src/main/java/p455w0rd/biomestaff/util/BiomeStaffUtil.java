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
package p455w0rd.biomestaff.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.Constants;
import p455w0rd.biomestaff.item.ItemBiomeStaff;

/**
 * @author p455w0rd
 *
 */
public class BiomeStaffUtil {

	public static Biome getBiomeFromStaff(ItemStack staff) {
		if (staff.hasTagCompound() && staff.getTagCompound().hasKey(ItemBiomeStaff.TAG_BIOME, Constants.NBT.TAG_BYTE)) {
			Biome biome = Biome.getBiome(staff.getTagCompound().getByte(ItemBiomeStaff.TAG_BIOME) & 255);
			if (biome != null) {
				return biome;
			}
		}
		return null;
	}

	public static boolean doesStaffContainBiome(ItemStack staff) {
		return getBiomeFromStaff(staff) != null;
	}

	public static IBlockState getBiomeTopBlockFromStaff(ItemStack staff) {
		if (doesStaffContainBiome(staff)) {
			Biome biome = getBiomeFromStaff(staff);
			return biome.topBlock;
		}
		return Blocks.AIR.getDefaultState();
	}

	public static ItemStack getItemTopBlockStack(ItemStack staff) {
		IBlockState biomeTopBlockState = getBiomeTopBlockFromStaff(staff);
		if (biomeTopBlockState != null) {
			return biomeTopBlockState.getBlock().getPickBlock(biomeTopBlockState, null, null, null, null);
		}
		return ItemStack.EMPTY;
	}

}
