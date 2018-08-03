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

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import p455w0rd.biomestaff.network.PacketSyncBiomeStaff;
import p455w0rd.biomestaff.network.PacketUpdateChunkRender;

/**
 * @author p455w0rd
 *
 */
public class ModNetworking {

	private static int packetId = 0;
	private static SimpleNetworkWrapper INSTANCE = null;

	private static int nextID() {
		return packetId++;
	}

	public static SimpleNetworkWrapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModGlobals.MODID);
		}
		return INSTANCE;
	}

	public static void init() {
		getInstance().registerMessage(PacketSyncBiomeStaff.class, PacketSyncBiomeStaff.class, nextID(), Side.CLIENT);
		getInstance().registerMessage(PacketUpdateChunkRender.class, PacketUpdateChunkRender.class, nextID(), Side.CLIENT);
	}

}
