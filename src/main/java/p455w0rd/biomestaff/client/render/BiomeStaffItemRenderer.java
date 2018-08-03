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
package p455w0rd.biomestaff.client.render;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.pipeline.LightUtil;
import p455w0rd.biomestaff.client.model.ItemModelWrapper;
import p455w0rd.biomestaff.client.model.ModelBiomeStaff;
import p455w0rd.biomestaff.init.ModConfig.Options;
import p455w0rd.biomestaff.init.ModGlobals;
import p455w0rd.biomestaff.util.BiomeStaffUtil;

/**
 * @author p455w0rd
 *
 */
public class BiomeStaffItemRenderer extends TileEntityItemStackRenderer {

	ModelBiomeStaff staffModel = new ModelBiomeStaff();
	public static ItemModelWrapper wrapperModel;
	public static TransformType transformType = TransformType.NONE;

	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		Minecraft mc = Minecraft.getMinecraft();
		TextureManager textureManager = mc.getTextureManager();
		float pbx = OpenGlHelper.lastBrightnessX;
		float pby = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(180.0F, 1.0f, 0.0f, 0f);
		GlStateManager.translate(0.45, -0.5F, -0.5F);
		textureManager.bindTexture(new ResourceLocation(ModGlobals.MODID, "textures/models/biome_staff.png"));
		GlStateManager.scale(1.0F, 1.5f, 1.0F);

		switch (transformType) {
		case GROUND:
			GlStateManager.translate(0, -0.85f, 0);
			break;
		case FIXED:
		case GUI:
			GlStateManager.rotate(45f, 0f, 1.0f, 1.0f);
			GlStateManager.scale(0.5f, 0.5f, 0.5f);
			GlStateManager.translate(0, -0.5f, 0);
			break;
		case THIRD_PERSON_LEFT_HAND:
		case THIRD_PERSON_RIGHT_HAND:
			GlStateManager.translate(0, -0.25, 0);
			break;
		default:
			GlStateManager.rotate(45.0f, 0f, 1f, 0f);
			break;
		}

		staffModel.render(player, player.getSwingProgress(partialTicks), 0f, 0f, player.rotationYaw, player.rotationPitch, 0.0625F);

		GlStateManager.scale(1.0F, 0.75f, 1.0F);

		ItemStack topBlockStack = BiomeStaffUtil.getItemTopBlockStack(stack);
		if (!topBlockStack.isEmpty()) {

			GlStateManager.translate(0.0625F, -0.25F, 0);
			GlStateManager.rotate(-180.0F, 1.0f, 0.0f, 0f);
			if (Options.rotateBiomeBlockVertical) {
				GlStateManager.rotate(ModGlobals.staffAnimationTicker, 0, 0, 1.0f);
			}
			if (Options.rotateBiomeBlockHorizontal) {
				GlStateManager.rotate(ModGlobals.staffAnimationTicker, 0, 1.0f, 0);
			}
			switch (transformType) {
			case FIRST_PERSON_LEFT_HAND:
			case FIRST_PERSON_RIGHT_HAND:
				GlStateManager.scale(0.25F, 0.25F, 0.25F);
				break;
			case GROUND:
				GlStateManager.scale(0.25F, 0.25F, 0.25F);
				break;
			case FIXED:
			case GUI:
				GlStateManager.scale(0.25F, 0.25F, 0.25F);
				break;
			case THIRD_PERSON_LEFT_HAND:
			case THIRD_PERSON_RIGHT_HAND:
				GlStateManager.scale(0.25F, 0.25F, 0.25F);
				break;
			default:
				break;
			}

			IBakedModel bakedmodel = mc.getRenderItem().getItemModelMesher().getItemModel(topBlockStack);
			//bakedmodel.getOverrides().handleItemState(bakedmodel, topBlockStack, mc.world, player);
			textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.enableBlend();
			GlStateManager.pushMatrix();
			//bakedmodel = ForgeHooksClient.handleCameraTransforms(bakedmodel, transformType, false);

			if (!topBlockStack.isEmpty()) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.5F, -0.5F, -0.5F);
				if (bakedmodel.isBuiltInRenderer()) {
					GlStateManager.enableRescaleNormal();
					topBlockStack.getItem().getTileEntityItemStackRenderer().renderByItem(topBlockStack);
				}
				else {
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder bufferbuilder = tessellator.getBuffer();
					bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
					for (EnumFacing enumfacing : EnumFacing.values()) {
						List<BakedQuad> quads = bakedmodel.getQuads((IBlockState) null, enumfacing, 0L);
						boolean flag = !topBlockStack.isEmpty();
						int i = 0;
						for (int j = quads.size(); i < j; ++i) {
							BakedQuad bakedquad = quads.get(i);
							int k = -1;
							if (flag && bakedquad.hasTintIndex()) {
								//k = mc.getItemColors().colorMultiplier(topBlockStack, bakedquad.getTintIndex());
								k = BiomeStaffUtil.getBiomeFromStaff(stack).getGrassColorAtPos(new BlockPos(0, 0, 0));
								if (EntityRenderer.anaglyphEnable) {
									k = TextureUtil.anaglyphColor(k);
								}
								k = k | -16777216;
							}
							LightUtil.renderQuadColor(bufferbuilder, bakedquad, k);
						}
					}

					List<BakedQuad> quads = bakedmodel.getQuads((IBlockState) null, (EnumFacing) null, 0L);
					boolean flag = !topBlockStack.isEmpty();
					int i = 0;
					for (int j = quads.size(); i < j; ++i) {
						BakedQuad bakedquad = quads.get(i);
						int k = -1;
						if (flag && bakedquad.hasTintIndex()) {
							k = mc.getItemColors().colorMultiplier(topBlockStack, bakedquad.getTintIndex());
							if (EntityRenderer.anaglyphEnable) {
								k = TextureUtil.anaglyphColor(k);
							}
							k = k | -16777216;
						}
						LightUtil.renderQuadColor(bufferbuilder, bakedquad, k);
					}
					tessellator.draw();
					if (topBlockStack.hasEffect()) {

					}
				}

				GlStateManager.popMatrix();
			}

			GlStateManager.cullFace(GlStateManager.CullFace.BACK);
			GlStateManager.popMatrix();
			textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		}
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, pbx, pby);
	}

	public static class RenderModel {
		public static void render(IBakedModel model, @Nonnull ItemStack stack) {
			render(model, -1, stack);
		}

		public static void render(IBakedModel model, int color) {
			render(model, color, ItemStack.EMPTY);
		}

		public static void render(IBakedModel model, int color, @Nonnull ItemStack stack) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder vertexbuffer = tessellator.getBuffer();
			vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
			for (EnumFacing enumfacing : EnumFacing.values()) {
				renderQuads(vertexbuffer, model.getQuads((IBlockState) null, enumfacing, 0L), color, stack);
			}
			renderQuads(vertexbuffer, model.getQuads((IBlockState) null, (EnumFacing) null, 0L), color, stack);
			tessellator.draw();
		}

		public static void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack) {
			boolean flag = (color == -1) && (!stack.isEmpty());
			int i = 0;
			for (int j = quads.size(); i < j; i++) {
				BakedQuad bakedquad = quads.get(i);
				int k = color;
				if ((flag) && (bakedquad.hasTintIndex())) {
					ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
					k = itemColors.colorMultiplier(stack, bakedquad.getTintIndex());
					if (EntityRenderer.anaglyphEnable) {
						k = TextureUtil.anaglyphColor(k);
					}
					k |= 0xFF000000;
				}
				LightUtil.renderQuadColor(renderer, bakedquad, k);
			}
		}
	}

}
