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

import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.common.network.packets.PacketCParticles;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

/**
 * A particle spawner which operates on the server thread. It sends packets to
 * clients about particles.
 * <p>
 * Avoid using spawnOneParticle as velocity might be unpredicted.
 * 
 * @author CrowsOfWar
 */
public class NetworkParticleSpawner implements ParticleSpawner {
	
	@Override
	public void spawnOneParticle(World world, EnumParticleTypes particle, double x, double y, double z,
			double velocityX, double velocityY, double velocityZ, int... parameters) {
		
		// Velocity -> max velocity... results in not expected velocity client
		// side.
		spawnParticles(world, particle, 1, 1, x, y, z, velocityX, velocityY, velocityZ, parameters);
		
	}
	
	@Override
	public void spawnParticles(World world, EnumParticleTypes particle, int minimum, int maximum, double x,
			double y, double z, double maxVelocityX, double maxVelocityY, double maxVelocityZ,
			int... parameters) {
		
		if (!world.isRemote) {
			TargetPoint point = new TargetPoint(world.provider.getDimension(), x, y, z, 64);
			
			AvatarMod.network.sendToAllAround(new PacketCParticles(particle, minimum, maximum, x, y, z,
					maxVelocityX / 20, maxVelocityY / 20, maxVelocityZ / 20), point);
		}
		
	}
	
}
