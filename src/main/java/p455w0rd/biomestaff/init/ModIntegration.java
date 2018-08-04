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

import net.minecraftforge.fml.common.Loader;

/**
 * @author p455w0rd
 *
 */
public class ModIntegration {

	public static enum Mods {
			JEI("jei");

		String modid;

		Mods(String modid) {
			this.modid = modid;
		}

		public String getModId() {
			return modid;
		}

		public boolean isLoaded() {
			return Loader.isModLoaded(getModId());
		}

	}

}
