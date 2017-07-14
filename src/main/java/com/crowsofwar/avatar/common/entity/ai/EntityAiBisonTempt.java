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
package com.crowsofwar.avatar.common.entity.ai;

import static com.crowsofwar.avatar.common.config.ConfigMobs.MOBS_CONFIG;
import static net.minecraft.item.ItemStack.EMPTY;

import java.util.List;

import com.crowsofwar.avatar.common.entity.mob.EntitySkyBison;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper.Action;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityAiBisonTempt extends EntityAIBase {
	
	private final EntitySkyBison bison;
	private final double maxDistSq;
	private EntityPlayer following;
	
	public EntityAiBisonTempt(EntitySkyBison bison, double maxDist) {
		this.bison = bison;
		this.maxDistSq = maxDist * maxDist;
		this.following = null;
		setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute() {
		
		if (bison.getCondition().getFoodPoints() >= 25) return false;
		
		List<EntityPlayer> players = bison.world.getEntities(EntityPlayer.class, player -> {
			if (bison.getDistanceSqToEntity(player) > maxDistSq) return false;
			return isHoldingFood(player);
		});
		
		players.sort((p1, p2) -> {
			double d1 = bison.getDistanceSqToEntity(p1);
			double d2 = bison.getDistanceSqToEntity(p2);
			return d1 < d2 ? -1 : (d1 > d2 ? 1 : 0);
		});
		
		if (!players.isEmpty()) {
			following = players.get(0);
			return true;
		} else {
			return false;
		}
		
	}
	
	@Override
	public void startExecuting() {
		bison.getMoveHelper().setMoveTo(following.posX,
				following.posY + following.eyeHeight - bison.getEyeHeight(), following.posZ, 1);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		
		if (!following.isDead && bison.getDistanceSqToEntity(following) <= maxDistSq
				&& isHoldingFood(following)) {
			
			bison.getMoveHelper().setMoveTo(following.posX,
					following.posY + following.eyeHeight - bison.getEyeHeight(), following.posZ, 1);
			return true;
			
		} else {
			following = null;
			bison.getMoveHelper().action = Action.WAIT;
			return false;
		}
	}
	
	private boolean isHoldingFood(EntityPlayer player) {
		for (EnumHand hand : EnumHand.values()) {
			ItemStack stack = player.getHeldItem(hand);
			if (stack != EMPTY) {
				if (MOBS_CONFIG.isBisonFood(stack.getItem())) {
					return true;
				}
			}
		}
		return false;
	}
	
}
