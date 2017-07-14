/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.crowsofwar.avatar.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.List;

import static net.minecraft.client.Minecraft.getMinecraft;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ScrollsPerspectiveModel implements IBakedModel {
	
	private final ModelResourceLocation mrlRegular, mrlGlow;
	private final ItemCameraTransforms cameraTransforms;
	private final ItemOverrideList overrideList;
	private final IBakedModel baseModel, baseModelGlow;
	
	public ScrollsPerspectiveModel(ModelResourceLocation mrlRegular, ModelResourceLocation mrlGlow,
			IBakedModel baseModel, IBakedModel baseModelGlow) {
		this.mrlRegular = mrlRegular;
		this.mrlGlow = mrlGlow;
		this.cameraTransforms = ItemCameraTransforms.DEFAULT;
		this.overrideList = ItemOverrideList.NONE;
		this.baseModel = baseModel;
		this.baseModelGlow = baseModelGlow;
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType transform) {
		
		ModelManager mm = getMinecraft().getRenderItem().getItemModelMesher().getModelManager();
		ModelResourceLocation mrl = transform == TransformType.GUI ? mrlGlow : mrlRegular;
		
		Matrix4f mat = baseModel.handlePerspective(transform).getRight();
		
		return Pair.of(mm.getModel(mrl), mat);
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return baseModel.getQuads(state, side, rand);
	}
	
	@Override
	public boolean isAmbientOcclusion() {
		return baseModel.isAmbientOcclusion();
	}
	
	@Override
	public boolean isGui3d() {
		return baseModel.isGui3d();
	}
	
	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture() {
		return baseModel.getParticleTexture();
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return baseModel.getItemCameraTransforms();
	}
	
	@Override
	public ItemOverrideList getOverrides() {
		return overrideList;
	}
	
}
