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

import com.crowsofwar.avatar.common.AvatarParticles;
import com.crowsofwar.avatar.common.entity.EntityFlames;
import com.crowsofwar.avatar.common.particle.ParticleSpawner;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class RenderFlames extends Render<EntityFlames> {
	
	private final Random random;
	private final ParticleSpawner particleSpawner;
	
	/**
	 * @param renderManager
	 */
	public RenderFlames(RenderManager renderManager, ParticleSpawner particle) {
		super(renderManager);
		this.random = new Random();
		this.particleSpawner = particle;
	}
	
	@Override
	public void doRender(EntityFlames entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		
		particleSpawner.spawnParticles(entity.world, AvatarParticles.getParticleFlames(), 1, 1,
				Vector.getEntityPos(entity), new Vector(0.02, 0.01, 0.02));
		
		// entity.world.spawnParticle(AvatarParticles.getParticleFlames(),
		// entity.posX,
		// entity.posY,
		// entity.posZ,
		// // entity.world.spawnParticle(EnumParticleTypes.FLAME,
		// entity.posX, entity.posY,
		// // entity.posZ,
		// (random.nextGaussian() - 0.5) * 0.02, random.nextGaussian() * 0.01,
		// (random.nextGaussian() - 0.5) * 0.02);
		
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityFlames entity) {
		return null;
	}
	
}
