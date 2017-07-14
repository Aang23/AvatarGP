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

import com.crowsofwar.avatar.common.entity.mob.EntityHumanBender;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class RenderHumanBender extends RenderLiving<EntityHumanBender> {
	
	private final ResourceLocation[] locations;
	
	/**
	 * @param renderManager
	 * @param texture
	 *            Name of the texture file to be used, without the ending
	 *            "_#.png". E.g. "airbender"
	 */
	public RenderHumanBender(RenderManager renderManager, String texture, int textures) {
		super(renderManager, new ModelBiped(0, 0, 64, 64), 0.5f);
		
		locations = new ResourceLocation[textures];
		for (int i = 0; i < textures; i++) {
			locations[i] = new ResourceLocation("avatarmod", "textures/mob/" + texture + "_" + i + ".png");
		}
		
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityHumanBender entity) {
		return locations[entity.getSkin()];
	}
	
}
