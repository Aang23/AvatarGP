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

import com.crowsofwar.avatar.common.entity.ControlPoint;
import com.crowsofwar.avatar.common.entity.EntityArc;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

public class RenderFireArc extends RenderArc {
	
	private static final ResourceLocation fire = new ResourceLocation("avatarmod",
			"textures/entity/fire-ribbon.png");
	
	public RenderFireArc(RenderManager renderManager) {
		super(renderManager);
		enableFullBrightness();
	}
	
	@Override
	protected ResourceLocation getTexture() {
		return fire;
	}
	
	@Override
	protected void onDrawSegment(EntityArc arc, ControlPoint first, ControlPoint second) {
		// Parametric equation
		// For parameters, they will be same as linear equation: y = mx+b
		
		Vector m = second.position().minus(first.position());
		Vector b = first.position();
		double x = Math.random(); // 0-1
		Vector spawnAt = m.times(x).plus(b);
		Vector velocity = new Vector(0, 0, 0);
		
		// TODO [1.10] Re-introduce flame particle
		arc.world.spawnParticle(EnumParticleTypes.FLAME, spawnAt.x(), spawnAt.y(), spawnAt.z(),
				velocity.x() / 20, 0.05, velocity.z() / 20);
		// AvatarParticles.createParticle(arc.world, spawnAt.xCoord,
		// spawnAt.yCoord,
		// spawnAt.zCoord,
		// velocity.xCoord / 20, 0.05, velocity.zCoord / 20);
		
	}
	
}
