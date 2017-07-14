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

import com.crowsofwar.avatar.common.entity.ControlPoint;
import com.crowsofwar.avatar.common.entity.EntityArc;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class RenderAirGust extends RenderArc {
	
	public static final ResourceLocation TEXTURE = new ResourceLocation("avatarmod",
			"textures/entity/air-ribbon.png");
	
	private static final Random random = new Random();
	
	/**
	 * @param renderManager
	 */
	public RenderAirGust(RenderManager renderManager) {
		super(renderManager);
	}
	
	@Override
	protected void onDrawSegment(EntityArc arc, ControlPoint first, ControlPoint second) {
		
		World world = arc.world;
		AxisAlignedBB boundingBox = first.getBoundingBox();
		double spawnX = boundingBox.minX + random.nextDouble() * (boundingBox.maxX - boundingBox.minX);
		double spawnY = boundingBox.minY + random.nextDouble() * (boundingBox.maxY - boundingBox.minY);
		double spawnZ = boundingBox.minZ + random.nextDouble() * (boundingBox.maxZ - boundingBox.minZ);
		world.spawnParticle(EnumParticleTypes.CLOUD, spawnX, spawnY, spawnZ, 0, 0, 0);
		
	}
	
	@Override
	protected ResourceLocation getTexture() {
		return TEXTURE;
	}
	
}
