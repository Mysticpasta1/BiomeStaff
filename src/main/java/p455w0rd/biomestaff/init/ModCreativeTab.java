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
package p455w0rd.biomestaff.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * @author p455w0rd
 *
 */
public class ModCreativeTab extends CreativeTabs {

	public static final CreativeTabs MAIN_TAB = new ModCreativeTab();

	public ModCreativeTab() {
		super(ModGlobals.MODID);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ModItems.BIOME_STAFF);
	}
}
