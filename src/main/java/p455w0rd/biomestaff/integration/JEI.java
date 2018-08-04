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
package p455w0rd.biomestaff.integration;

import javax.annotation.Nonnull;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import p455w0rd.biomestaff.init.ModItems;

/**
 * @author p455w0rd
 *
 */
@JEIPlugin
public class JEI implements IModPlugin {

	@Override
	public void register(@Nonnull IModRegistry registry) {
		registry.addIngredientInfo(new ItemStack(ModItems.BIOME_STAFF), ItemStack.class, "jei.biomestaff:biome_staff.desc");
	}

}
