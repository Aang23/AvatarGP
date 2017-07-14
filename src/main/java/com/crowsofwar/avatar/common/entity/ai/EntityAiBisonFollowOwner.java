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

import static com.crowsofwar.gorecore.util.Vector.getEyePos;

import com.crowsofwar.avatar.common.entity.mob.EntitySkyBison;
import com.crowsofwar.avatar.common.item.AvatarItems;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityAiBisonFollowOwner extends EntityAIBase {
	
	private final EntitySkyBison bison;
	
	public EntityAiBisonFollowOwner(EntitySkyBison bison) {
		this.bison = bison;
		this.setMutexBits(1);
	}
	
	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		
		if (bison.isSitting()) return false;
		
		EntityPlayer owner = bison.getOwner();
		if (owner != null) {
			
			boolean holdingWhistle = false;
			for (ItemStack stack : owner.getHeldEquipment()) {
				if (stack.getItem() == AvatarItems.itemBisonWhistle) {
					holdingWhistle = true;
				}
			}
			
			if (bison.getLeashedToEntity() == owner) {
				return true;
			}
			
			if (holdingWhistle) {
				double maxDist = bison.getAttackTarget() == null ? 6 : 20;
				double maxDistSq = maxDist * maxDist;
				double distSq = bison.getDistanceSqToEntity(owner);
				return distSq >= maxDistSq && !bison.isSitting();
			}
			
		}
		
		return false;
		
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}
	
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		
		EntityPlayer owner = bison.getOwner();
		if (owner == null) return;
		
		double dist = bison.getDistanceToEntity(owner);
		
		Vector direction = getEyePos(owner).minus(getEyePos(bison)).normalize();
		Vector targetPos = getEyePos(bison).plus(direction.times(dist * 0.8));
		
		bison.getMoveHelper().setMoveTo(targetPos.x(), targetPos.y(), targetPos.z(), 1.0D);
		
	}
	
}
