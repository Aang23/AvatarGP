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
package com.crowsofwar.avatar.common;

import static com.crowsofwar.avatar.common.config.ConfigClient.CLIENT_CONFIG;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Manages registration of custom particles
 * 
 * @author CrowsOfWar
 */
public class AvatarParticles {
	
	private static EnumParticleTypes particleFlames, particleAir;
	private static Map<Integer, EnumParticleTypes> lookup;
	
	public static void register() {
		lookup = new HashMap<>();
		particleFlames = addParticle("flames");
		particleAir = addParticle("air");
	}
	
	private static EnumParticleTypes addParticle(String particleName) {
		
		EnumParticleTypes particle = EnumHelper.addEnum(EnumParticleTypes.class,
				"AVATAR_" + particleName.toUpperCase(),
				new Class<?>[] { String.class, int.class, boolean.class },
				"avatar" + particleName.substring(0, 1).toUpperCase()
						+ particleName.substring(1).toLowerCase(),
				nextParticleId(), true);
		
		lookup.put(particle.getParticleID(), particle);
		return particle;
		
	}
	
	private static int nextParticleId() {
		EnumParticleTypes[] allParticles = EnumParticleTypes.values();
		int maxId = -1;
		for (EnumParticleTypes particle : allParticles) {
			if (particle.getParticleID() > maxId) {
				maxId = particle.getParticleID();
			}
		}
		return maxId + 1;
	}
	
	public static EnumParticleTypes getParticleFlames() {
		return CLIENT_CONFIG.useCustomParticles ? particleFlames : EnumParticleTypes.FLAME;
	}
	
	public static EnumParticleTypes getParticleAir() {
		return CLIENT_CONFIG.useCustomParticles ? particleAir : EnumParticleTypes.CLOUD;
	}
	
	/**
	 * Looks up that particle. Returns null if none found.
	 */
	public static EnumParticleTypes lookup(int id) {
		return lookup.get(id);
	}
	
}
