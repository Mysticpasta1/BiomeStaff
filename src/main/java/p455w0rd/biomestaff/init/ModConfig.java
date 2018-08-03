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

import java.io.File;

import net.minecraftforge.common.config.Configuration;

/**
 * @author p455w0rd
 *
 */
public class ModConfig {

	private static final Configuration CONFIG = new Configuration(new File(ModGlobals.CONFIG_FILE));

	public static void init() {
		CONFIG.load();
		Options.rotateBiomeBlockHorizontal = CONFIG.getBoolean("rotateBiomeBlockHorizontal", Configuration.CATEGORY_CLIENT, true, "When rendering the biome block on the wand item, should it rotate on the horizontal axis?");
		Options.rotateBiomeBlockVertical = CONFIG.getBoolean("rotateBiomeBlockVertical", Configuration.CATEGORY_CLIENT, true, "When rendering the biome block on the wand item, should it rotate on the vertical axis?");
		if (CONFIG.hasChanged()) {
			CONFIG.save();
		}
	}

	public static Configuration getConfig() {
		return CONFIG;
	}

	public static class Options {

		public static boolean rotateBiomeBlockHorizontal = true;
		public static boolean rotateBiomeBlockVertical = true;

	}

}
