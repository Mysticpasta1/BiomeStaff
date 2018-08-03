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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import p455w0rd.biomestaff.BiomeStaff;

/**
 * @author p455w0rd
 *
 */
public class PacketSyncBiomeStaff implements IMessage, IMessageHandler<PacketSyncBiomeStaff, IMessage> {

	NBTTagCompound nbt = new NBTTagCompound();

	public PacketSyncBiomeStaff() {
	}

	public PacketSyncBiomeStaff(NBTTagCompound nbt) {
		this.nbt = nbt;
	}

	@Override
	public IMessage onMessage(PacketSyncBiomeStaff message, MessageContext ctx) {
		ItemStack heldStack = BiomeStaff.PROXY.getPlayer().getHeldItemMainhand();
		heldStack.deserializeNBT(nbt);
		BiomeStaff.PROXY.getPlayer().setHeldItem(EnumHand.MAIN_HAND, heldStack);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		nbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbt);
	}

}
