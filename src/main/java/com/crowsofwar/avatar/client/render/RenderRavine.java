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
package com.crowsofwar.avatar.client.render;

import java.util.Random;

import com.crowsofwar.avatar.common.entity.EntityRavine;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class RenderRavine extends Render<EntityRavine> {
	
	private final Random random;
	
	/**
	 * @param renderManager
	 */
	public RenderRavine(RenderManager renderManager) {
		super(renderManager);
		this.random = new Random();
	}
	
	@Override
	public void doRender(EntityRavine entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		World world = entity.getEntityWorld();
		IBlockState blockState = world.getBlockState(entity.getPosition().offset(EnumFacing.DOWN));
		Block block = blockState.getBlock();
		world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, entity.posX, entity.posY + 0.3, entity.posZ,
				random.nextGaussian() - 0.5, random.nextGaussian() * 0.4, random.nextGaussian() - 0.5,
				Block.getStateId(blockState));
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityRavine entity) {
		return null;
	}
	
}
