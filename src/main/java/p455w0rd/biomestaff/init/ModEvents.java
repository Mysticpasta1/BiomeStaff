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

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.biomestaff.client.model.IModelProvider;
import p455w0rd.biomestaff.client.model.ItemModelWrapper;
import p455w0rd.biomestaff.client.render.BiomeStaffItemRenderer;

/**
 * @author p455w0rd
 *
 */
@EventBusSubscriber
public class ModEvents {

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelBake(ModelBakeEvent event) {
		IBakedModel biomeStaffModel = event.getModelRegistry().getObject(new ModelResourceLocation(ModItems.BIOME_STAFF.getRegistryName(), "inventory"));
		BiomeStaffItemRenderer.wrapperModel = new ItemModelWrapper(biomeStaffModel);
		event.getModelRegistry().putObject(new ModelResourceLocation(ModItems.BIOME_STAFF.getRegistryName(), "inventory"), BiomeStaffItemRenderer.wrapperModel);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegistryReady(ModelRegistryEvent event) {
		for (Item item : ModItems.ITEM_LIST) {
			if (item instanceof IModelProvider) {
				((IModelProvider) item).initModel();
			}
		}
		ModItems.BIOME_STAFF.setTileEntityItemStackRenderer(new BiomeStaffItemRenderer());
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onClientTick(ClientTickEvent event) {
		ModGlobals.staffAnimationTicker++;
		if (ModGlobals.staffAnimationTicker >= 360) {
			ModGlobals.staffAnimationTicker = -180;
		}
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent event) {
		if (event.getModID().equals(ModGlobals.MODID)) {
			ModConfig.getConfig().save();
			ModConfig.init();
		}
	}

	@SubscribeEvent
	public static void onItemRegistryReady(RegistryEvent.Register<Item> event) {
		for (Item item : ModItems.ITEM_LIST) {
			event.getRegistry().register(item);
		}
	}

}
