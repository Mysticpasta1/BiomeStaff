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
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.pipeline.LightUtil;
import p455w0rd.biomestaff.client.model.ItemModelWrapper;
import p455w0rd.biomestaff.client.model.ModelBiomeStaff;
import p455w0rd.biomestaff.init.ModGlobals;
import p455w0rd.biomestaff.util.BiomeStaffUtil;

/**
 * @author p455w0rd
 *
 */
public class BiomeStaffItemRenderer extends TileEntityItemStackRenderer {

	ModelBiomeStaff staffModel = new ModelBiomeStaff();
	private static ItemModelWrapper wrapperModel;
	private static TransformType transformType = TransformType.NONE;

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
		GlStateManager.translate(0.5, -0.5F, -0.5F);

		textureManager.bindTexture(new ResourceLocation(ModGlobals.MODID, "textures/models/biome_staff.png"));
		GlStateManager.scale(1.0F, 1.5f, 1.0F);
		staffModel.render(player, player.getSwingProgress(partialTicks), 0f, 0f, player.rotationYaw, player.rotationPitch, 0.0625F);
		GlStateManager.scale(1.0F, 0.75f, 1.0F);

		ItemStack topBlockStack = BiomeStaffUtil.getItemTopBlockStack(stack);
		if (!topBlockStack.isEmpty()) {

			GlStateManager.translate(0.0625F, -0.25F, 0);
			GlStateManager.rotate(-180.0F, 1.0f, 0.0f, 0f);
			GlStateManager.scale(0.25F, 0.25F, 0.25F);

			IBakedModel bakedmodel = mc.getRenderItem().getItemModelMesher().getItemModel(topBlockStack);
			bakedmodel.getOverrides().handleItemState(bakedmodel, topBlockStack, mc.world, player);
			textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.enableBlend();
			GlStateManager.pushMatrix();
			bakedmodel = ForgeHooksClient.handleCameraTransforms(bakedmodel, transformType, false);

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
								//k = k | -16777216;
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

						/*
						GlStateManager.depthMask(false);
						GlStateManager.depthFunc(514);
						GlStateManager.disableLighting();
						GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
						textureManager.bindTexture(RES_ITEM_GLINT);
						GlStateManager.matrixMode(5890);
						GlStateManager.pushMatrix();
						GlStateManager.scale(8.0F, 8.0F, 8.0F);
						float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
						GlStateManager.translate(f, 0.0F, 0.0F);
						GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
						this.renderModel(model, -8372020);
						GlStateManager.popMatrix();
						GlStateManager.pushMatrix();
						GlStateManager.scale(8.0F, 8.0F, 8.0F);
						float f1 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
						GlStateManager.translate(-f1, 0.0F, 0.0F);
						GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
						this.renderModel(model, -8372020);
						GlStateManager.popMatrix();
						GlStateManager.matrixMode(5888);
						GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
						GlStateManager.enableLighting();
						GlStateManager.depthFunc(515);
						GlStateManager.depthMask(true);
						this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
						*/
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

	public static void setTransformType(TransformType transformType) {
		BiomeStaffItemRenderer.transformType = transformType;
	}

	public static TransformType getTransformType() {
		return BiomeStaffItemRenderer.transformType;
	}

	public static void setWrapperModel(ItemModelWrapper wrapperModel) {
		BiomeStaffItemRenderer.wrapperModel = wrapperModel;
	}

	public static ItemModelWrapper getWrapperModel() {
		return wrapperModel;
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
