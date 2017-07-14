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
package com.crowsofwar.avatar.common.bending.fire;

import static com.crowsofwar.gorecore.util.Vector.getEntityPos;
import static com.crowsofwar.gorecore.util.Vector.getRotationTo;
import static java.lang.Math.toDegrees;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityFireball;
import com.crowsofwar.avatar.common.entity.data.FireballBehavior;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AiFireball extends BendingAi {
	
	private int timeExecuting;
	
	/**
	 * @param ability
	 * @param entity
	 * @param bender
	 */
	protected AiFireball(BendingAbility ability, EntityLiving entity, Bender bender) {
		super(ability, entity, bender);
		timeExecuting = 0;
		setMutexBits(2);
	}
	
	@Override
	protected void startExec() {
		BendingData data = bender.getData();
		data.chi().setMaxChi(10);
		data.chi().setTotalChi(10);
		data.chi().setAvailableChi(10);
		execAbility();
		data.setAbilityCooldown(100);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		
		if (entity.getAttackTarget() == null) return false;
		
		Vector rotations = getRotationTo(getEntityPos(entity), getEntityPos(entity.getAttackTarget()));
		entity.rotationYaw = (float) toDegrees(rotations.y());
		entity.rotationPitch = (float) toDegrees(rotations.x());
		
		if (timeExecuting >= 40) {
			BendingData data = bender.getData();
			execStatusControl(StatusControl.THROW_FIREBALL);
			timeExecuting = 0;
			return false;
		} else {
			return true;
		}
		
	}
	
	@Override
	protected boolean shouldExec() {
		EntityLivingBase target = entity.getAttackTarget();
		return target != null && entity.getDistanceSqToEntity(target) > 4 * 4
				&& bender.getData().getAbilityCooldown() == 0 && entity.getRNG().nextBoolean();
	}
	
	@Override
	public void updateTask() {
		timeExecuting++;
	}
	
	@Override
	public void resetTask() {
		
		EntityFireball fireball = AvatarEntity.lookupEntity(entity.world, EntityFireball.class, //
				fire -> fire.getBehavior() instanceof FireballBehavior.PlayerControlled
						&& fire.getOwner() == entity);
		
		if (fireball != null) {
			fireball.setDead();
			bender.getData().removeStatusControl(StatusControl.THROW_FIREBALL);
		}
		
	}
	
}
