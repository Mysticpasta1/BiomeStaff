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
package p455w0rd.biomestaff.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import p455w0rd.biomestaff.BiomeStaff;

/**
 * @author p455w0rd
 *
 */
public class PacketUpdateChunkRender implements IMessage, IMessageHandler<PacketUpdateChunkRender, IMessage> {

	BlockPos pos;
	int radius;
	byte biome;

	public PacketUpdateChunkRender() {
	}

	public PacketUpdateChunkRender(BlockPos pos, int radius, byte biome) {
		this.pos = pos;
		this.radius = radius;
		this.biome = biome;
	}

	@Override
	public IMessage onMessage(PacketUpdateChunkRender message, MessageContext ctx) {
		int rad = message.radius;
		BlockPos pos = message.pos;
		World world = Minecraft.getMinecraft().world;
		for (int x = pos.getX() - rad; x <= pos.getX() + rad; ++x) {
			for (int z = pos.getZ() - rad; z <= pos.getZ() + rad; ++z) {
				int relBlockX = x & 15;
				int relBlockZ = z & 15;
				Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, pos.getY(), z));
				byte[] byteArray = chunk.getBiomeArray();
				byte currentByte = byteArray[relBlockZ << 4 | relBlockX];
				if (currentByte != message.biome) {
					byteArray[relBlockZ << 4 | relBlockX] = message.biome;
					chunk.setBiomeArray(byteArray);
					chunk.setModified(true);
					BiomeStaff.PROXY.updateChunkRendering(x, z);
				}
			}
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		pos = new BlockPos(x, y, z);
		radius = buf.readInt();
		biome = buf.readByte();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());

		buf.writeInt(radius);

		buf.writeByte(biome);
	}

}
