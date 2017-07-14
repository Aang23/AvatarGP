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

import java.util.Random;

import com.crowsofwar.avatar.common.entity.mob.EntitySkyBison;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityAiBisonWander extends EntityAIBase {
	
	private final EntitySkyBison entity;
	
	public EntityAiBisonWander(EntitySkyBison entity) {
		this.entity = entity;
		this.setMutexBits(1);
	}
	
	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		
		if (entity.isSitting()) return false;
		if (entity.getControllingPassenger() != null) return false;
		if (entity.wantsGrass()) return false;
		
		EntityMoveHelper moveHelper = entity.getMoveHelper();
		
		if (!moveHelper.isUpdating()) {
			return true;
		} else {
			double dx = moveHelper.getX() - this.entity.posX;
			double dy = moveHelper.getY() - this.entity.posY;
			double dz = moveHelper.getZ() - this.entity.posZ;
			double distToTargetSq = dx * dx + dy * dy + dz * dz;
			return distToTargetSq < 1.0D || distToTargetSq > 3600.0D;
		}
		
	}
	
	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}
	
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		Random random = entity.getRNG();
		Vector original = entity.getOriginalPos();
		double x = original.x() + (random.nextFloat() * 2 - 1) * 32;
		double y = original.y() + (random.nextFloat() * 2 - 1) * 32;
		double z = original.z() + (random.nextFloat() * 2 - 1) * 32;
		
		this.entity.getMoveHelper().setMoveTo(x, y, z, 1.0D);
	}
	
}
