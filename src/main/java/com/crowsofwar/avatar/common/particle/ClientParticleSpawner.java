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
package com.crowsofwar.avatar.common.particle;

import java.util.Random;

import com.crowsofwar.avatar.AvatarMod;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * An implementation of {@link ParticleSpawner} which spawns particles
 * client-side.
 * <p>
 * This can be used on either side
 * 
 * @author CrowsOfWar
 */
public class ClientParticleSpawner implements ParticleSpawner {
	
	@Override
	public void spawnOneParticle(World world, EnumParticleTypes particle, double x, double y, double z,
			double velocityX, double velocityY, double velocityZ, int... parameters) {
		
		if (world.isRemote) {
			
			world.spawnParticle(particle, x, y, z, velocityX / 20, velocityY / 20, velocityZ / 20,
					parameters);
			
		}
		
	}
	
	@Override
	public void spawnParticles(World world, EnumParticleTypes particle, int minimum, int maximum, double x,
			double y, double z, double maxVelocityX, double maxVelocityY, double maxVelocityZ,
			int... parameters) {
		
		if (world.isRemote) {
			
			Random random = new Random();
			
			int particlesToSpawn = (int) ((random.nextGaussian() * (maximum - minimum)) + minimum);
			int particleSetting = 1 + AvatarMod.proxy.getParticleAmount();// 1+0=1,1+1=2,1+2=3
			particlesToSpawn /= particleSetting;
			if (particlesToSpawn == 0 && random.nextInt(8) == 0) particlesToSpawn = 1;
			
			for (int i = 0; i < particlesToSpawn; i++) {
				
				world.spawnParticle(particle, x, y, z, random(random, -maxVelocityX, maxVelocityX) / 20,
						random(random, -maxVelocityY, maxVelocityY) / 20,
						random(random, -maxVelocityZ, maxVelocityZ) / 20, parameters);
				
			}
			
		}
		
	}
	
	private double random(Random rand, double min, double max) {
		return min + rand.nextGaussian() * (max - min);
	}
	
}
