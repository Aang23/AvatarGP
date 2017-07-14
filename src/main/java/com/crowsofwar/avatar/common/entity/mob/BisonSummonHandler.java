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
package com.crowsofwar.avatar.common.entity.mob;

import java.util.List;
import java.util.Random;

import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;

import net.minecraft.entity.EntityLivingBase;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class BisonSummonHandler extends TickHandler {
	
	@Override
	public boolean tick(BendingContext ctx) {
		
		if (ctx.getWorld().isRemote) return false;
		
		BendingData data = ctx.getData();
		
		int cooldown = data.getPetSummonCooldown();
		if (cooldown <= 0) {
			
			trySummonBison(ctx.getBenderEntity());
			return true;
			
		} else {
			
			data.setPetSummonCooldown(cooldown - 1);
			return false;
			
		}
		
	}
	
	private boolean trySummonBison(EntityLivingBase player) {
		
		List<EntitySkyBison> entities = player.world.getEntities(EntitySkyBison.class,
				bison -> bison.getOwner() == player);
		
		if (!entities.isEmpty()) {
			EntitySkyBison bison = entities.get(0);
			Random random = new Random();
			
			// Find suitable location near player
			for (int i = 0; i < 5; i++) {
				
				double x = player.posX + (random.nextDouble() * 2 - 1) * 15;
				double y = player.posY + (random.nextDouble() * 2 - 1) * 5;
				double z = player.posZ + (random.nextDouble() * 2 - 1) * 15;
				
				if (bison.attemptTeleport(x, y, z)) {
					return true;
				}
				
			}
		}
		
		return false;
		
	}
	
}
