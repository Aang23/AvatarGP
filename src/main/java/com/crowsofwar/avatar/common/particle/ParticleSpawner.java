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

import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * Handles particle spawning.
 * 
 * @author CrowsOfWar
 */
public interface ParticleSpawner {
	
	/**
	 * Spawn a particle in the world manually. It's generally recommended to
	 * {@link #spawnParticles(World, ParticleType, int, double, double, double, double, double, double, int...)
	 * spawn multiple particles}.
	 * 
	 * @param world
	 *            The world
	 * @param particle
	 *            The type of particle
	 * @param x
	 *            X-position of particle
	 * @param y
	 *            Y-position of particle
	 * @param z
	 *            Z-position of particle
	 * @param velocityX
	 *            Velocity X of particle in m/s
	 * @param velocityY
	 *            Velocity Y of particle in m/s
	 * @param velocityZ
	 *            Velocity Z of particle in m/s
	 * @param parameters
	 *            Extra parameters for the particle effect
	 */
	void spawnOneParticle(World world, EnumParticleTypes particle, double x, double y, double z,
			double velocityX, double velocityY, double velocityZ, int... parameters);
	
	/**
	 * Spawn a particle in the world manually. It's generally recommended to
	 * {@link #spawnParticles(World, ParticleType, int, Vector, Vector, int...)
	 * spawn multiple particles}.
	 * 
	 * @param world
	 *            The world
	 * @param particle
	 *            The type of particle
	 * @param position
	 *            Position of particle
	 * @param velocity
	 *            Velocity of particle in m/s
	 * @param parameters
	 *            Extra parameters for the particle effect
	 */
	default void spawnOneParticle(World world, EnumParticleTypes particle, Vector position, Vector velocity,
			int... parameters) {
		spawnOneParticle(world, particle, position.x(), position.y(), position.z(), velocity.x(),
				velocity.y(), velocity.z(), parameters);
	}
	
	/**
	 * Spawn multiple particles in the world. This is better than spawning
	 * particles manually since it can be optimized for different settings.
	 * 
	 * @param world
	 *            The world
	 * @param particle
	 *            Type of particle
	 * @param minimum
	 *            Minimum amount of particles to spawn
	 * @param maximum
	 *            Maximum amount of particles to spawn
	 * @param x
	 *            X-position to spawn at
	 * @param y
	 *            Y-position to spawn at
	 * @param z
	 *            Z-position to spawn at
	 * @param maxVelocityX
	 *            Maximum velocity X in m/s
	 * @param maxVelocityY
	 *            Maximum velocity Y in m/s
	 * @param maxVelocityZ
	 *            Maximum velocity Z in m/s
	 * @param parameters
	 *            Extra parameters for the particle effect
	 */
	void spawnParticles(World world, EnumParticleTypes particle, int minimum, int maximum, double x, double y,
			double z, double maxVelocityX, double maxVelocityY, double maxVelocityZ, int... parameters);
	
	/**
	 * Spawn multiple particles in the world. This is better than spawning
	 * particles manually since it can be optimized for different settings.
	 * 
	 * @param world
	 *            The world
	 * @param particle
	 *            Type of particle
	 * @param minimum
	 *            Minimum amount of particles to spawn
	 * @param maximum
	 *            Maximum amount of particles to spawn
	 * @param position
	 *            Position to spawn at
	 * @param maxVelocity
	 *            Maximum velocity of particles in m/s
	 * @param parameters
	 *            Extra parameters for the particle effect
	 */
	default void spawnParticles(World world, EnumParticleTypes particle, int minimum, int maximum,
			Vector position, Vector maxVelocity, int... parameters) {
		spawnParticles(world, particle, minimum, maximum, position.x(), position.y(), position.z(),
				maxVelocity.x(), maxVelocity.y(), maxVelocity.z(), parameters);
	}
	
}
