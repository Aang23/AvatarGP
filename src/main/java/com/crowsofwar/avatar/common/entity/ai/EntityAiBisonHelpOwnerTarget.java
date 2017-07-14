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

import com.crowsofwar.avatar.common.entity.mob.EntitySkyBison;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAiBisonHelpOwnerTarget extends EntityAITarget {
	
	private final EntitySkyBison bison;
	EntityLivingBase theTarget;
	private int timestamp;
	
	public EntityAiBisonHelpOwnerTarget(EntitySkyBison bison) {
		super(bison, false);
		this.bison = bison;
	}
	
	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		
		EntityLivingBase owner = this.bison.getOwner();
		
		if (owner == null) {
			return false;
		} else {
			// getRevengeTarget() : entity that was last attacked
			this.theTarget = owner.getRevengeTarget();
			int i = owner.getRevengeTimer();
			return i != this.timestamp && isSuitableTarget(this.theTarget, false);
		}
		
	}
	
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.taskOwner.setAttackTarget(this.theTarget);
		EntityLivingBase entitylivingbase = this.bison.getOwner();
		
		if (entitylivingbase != null) {
			this.timestamp = entitylivingbase.getRevengeTimer();
		}
		
		super.startExecuting();
	}
}
