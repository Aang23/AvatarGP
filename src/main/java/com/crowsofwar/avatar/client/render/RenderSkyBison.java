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

import com.crowsofwar.avatar.common.entity.mob.EntitySkyBison;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class RenderSkyBison extends RenderLiving<EntitySkyBison> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation("avatarmod",
			"textures/mob/flyingbison.png");
	
	/**
	 * @param rendermanager
	 */
	public RenderSkyBison(RenderManager rm) {
		super(rm, new ModelFlyingBison(), 0);
		// shadowSize not important; adjusted based on size below
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntitySkyBison entity) {
		return TEXTURE;
	}
	
	@Override
	public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw,
			float partialTicks) {
		
		EntitySkyBison bison = (EntitySkyBison) entity;
		shadowSize = 2.5f * bison.getCondition().getSizeMultiplier();
		super.doRenderShadowAndFire(entity, x, y, z, yaw, partialTicks);
		
	}
	
}
