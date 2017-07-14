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
package com.crowsofwar.avatar.common.bending.air;

import com.crowsofwar.avatar.common.AvatarParticles;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.particle.ClientParticleSpawner;
import com.crowsofwar.avatar.common.particle.ParticleSpawner;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLivingBase;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AirParticleSpawner extends TickHandler {
	
	private static final ParticleSpawner particles = new ClientParticleSpawner();
	
	@Override
	public boolean tick(BendingContext ctx) {
		EntityLivingBase target = ctx.getBenderEntity();
		Bender bender = ctx.getBender();
		
		Vector pos = Vector.getEntityPos(target);
		pos.setY(pos.y() + 1.3);
		
		particles.spawnParticles(target.world, AvatarParticles.getParticleAir(), 1, 1, pos,
				new Vector(0.7, 0.2, 0.7));
		
		return target.isInWater() || target.onGround || bender.isFlying();
		
	}
	
}
