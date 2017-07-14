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
package com.crowsofwar.avatar.client.particles;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AvatarParticleFlames extends AvatarParticle {
	
	private static final ParticleFrame[] FRAMES = new ParticleFrame[7];
	static {
		for (int i = 0; i < FRAMES.length; i++) {
			FRAMES[i] = new ParticleFrame(32 * i, 0, 32, 32);
		}
	}
	
	public AvatarParticleFlames(int particleID, World world, double x, double y, double z, double velX,
			double velY, double velZ, int... parameters) {
		
		this(world, x, y, z, velX, velY, velZ);
		
	}
	
	protected AvatarParticleFlames(World world, double x, double y, double z, double velX, double velY,
			double velZ) {
		
		super(world, x, y, z, velX, velY, velZ);
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.setParticleTextureIndex(4);
		this.setSize(0.02F, 0.02F);
		this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F + 0.3f;
		this.motionX = velX * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
		this.motionY = velY * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
		this.motionZ = velZ * 0.20000000298023224D + (Math.random() * 2.0D - 1.0D) * 0.019999999552965164D;
		this.particleAge = 0;
		this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		
		enableAdditiveBlending();
		
	}
	
	@Override
	protected ParticleFrame[] getTextureFrames() {
		return FRAMES;
	}
	
}
