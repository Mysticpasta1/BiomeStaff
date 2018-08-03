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
package p455w0rd.biomestaff.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import p455w0rd.biomestaff.init.ModConfig;
import p455w0rd.biomestaff.init.ModGlobals;

/**
 * @author p455w0rd
 *
 */
public class GuiModConfig extends GuiConfig {

	public GuiModConfig(GuiScreen parent) {
		super(getParent(parent), getConfigElements(), ModGlobals.MODID, false, false, getTitle(parent));
	}

	private static GuiScreen getParent(GuiScreen parent) {
		return parent;
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> configElements = new ArrayList<IConfigElement>();
		Configuration config = ModConfig.getConfig();
		if (config != null) {
			ConfigCategory categoryClient = config.getCategory(Configuration.CATEGORY_CLIENT);
			configElements.addAll(new ConfigElement(categoryClient).getChildElements());
		}
		return configElements;
	}

	private static String getTitle(GuiScreen parent) {
		return I18n.translateToLocal(ModGlobals.NAME + " Config");
	}
}
