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
package com.crowsofwar.avatar.common.bending;

import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.util.Raytrace;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

/**
 * Represents behavior needed for use of an ability by a mob. When most
 * abilities are activated, some sort of preparation or strategy is required.
 * For example, air gust is only useful when an enemy is too close, and requires
 * the user to aim at an enemy entity. This class wraps all of this behavior so
 * the ability can be activated at the appropriate time.
 * <p>
 * BendingAi is a subclass of EntityAIBase, meaning that a new instance is
 * applied per-entity in its tasks list. A new instance of a BendingAi is
 * acquired via the ability's {@link BendingAbility#getAi(EntityLiving, Bender)
 * getAi method} for the specific mob.
 * 
 * @author CrowsOfWar
 */
public abstract class BendingAi extends EntityAIBase {
	
	protected final BendingAbility ability;
	protected final EntityLiving entity;
	protected final Bender bender;
	
	protected int timeExecuting;
	
	protected BendingAi(BendingAbility ability, EntityLiving entity, Bender bender) {
		this.ability = ability;
		this.entity = entity;
		this.bender = bender;
		this.timeExecuting = 0;
	}
	
	@Override
	public void startExecuting() {
		timeExecuting = 0;
		startExec();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}
	
	@Override
	public void resetTask() {
		timeExecuting = 0;
	}
	
	@Override
	public void updateTask() {
		timeExecuting++;
	}
	
	@Override
	public final boolean shouldExecute() {
		EntityLivingBase target = entity.getAttackTarget();
		boolean targetInRange = target == null || entity.getDistanceSqToEntity(target) < 12 * 12;
		return bender.getData().getAbilityCooldown() == 0 && targetInRange && shouldExec();
	}
	
	protected abstract boolean shouldExec();
	
	protected abstract void startExec();
	
	/**
	 * Executes the ability's main code (the part used for players)
	 */
	protected void execAbility() {
		if (bender.getData().getAbilityCooldown() == 0) {
			Raytrace.Result raytrace = Raytrace.getTargetBlock(entity, ability.getRaytrace());
			AbilityContext ctx = new AbilityContext(bender.getData(), entity, bender, raytrace, ability);
			ability.execute(ctx);
		}
	}
	
	/**
	 * If the status control is present, uses up the status control
	 */
	protected void execStatusControl(StatusControl sc) {
		BendingData data = bender.getData();
		if (data.hasStatusControl(sc)) {
			Raytrace.Result raytrace = Raytrace.getTargetBlock(entity, ability.getRaytrace());
			if (sc.execute(new AbilityContext(data, entity, bender, raytrace, ability))) {
				data.removeStatusControl(sc);
			}
		}
	}
	
}
