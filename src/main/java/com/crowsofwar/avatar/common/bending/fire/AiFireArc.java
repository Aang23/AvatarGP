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

import static com.crowsofwar.avatar.common.util.AvatarUtils.normalizeAngle;
import static com.crowsofwar.gorecore.util.Vector.getEntityPos;
import static com.crowsofwar.gorecore.util.Vector.getRotationTo;
import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityFireArc;
import com.crowsofwar.avatar.common.entity.data.FireArcBehavior;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AiFireArc extends BendingAi {
	
	private int timeExecuting;
	
	private float velocityYaw, velocityPitch;
	
	/**
	 * @param ability
	 * @param entity
	 * @param bender
	 */
	protected AiFireArc(BendingAbility ability, EntityLiving entity, Bender bender) {
		super(ability, entity, bender);
		timeExecuting = 0;
		setMutexBits(2);
	}
	
	@Override
	protected void startExec() {
		velocityYaw = 0;
		velocityPitch = 0;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		
		if (entity.getAttackTarget() == null) return false;
		
		Vector target = getRotationTo(getEntityPos(entity), getEntityPos(entity.getAttackTarget()));
		float targetYaw = (float) toDegrees(target.y());
		float targetPitch = (float) toDegrees(target.x());
		
		float currentYaw = normalizeAngle(entity.rotationYaw);
		float currentPitch = normalizeAngle(entity.rotationPitch);
		
		float yawLeft = abs(normalizeAngle(currentYaw - targetYaw));
		float yawRight = abs(normalizeAngle(targetYaw - currentYaw));
		if (yawRight < yawLeft) {
			velocityYaw += yawRight / 10;
		} else {
			velocityYaw -= yawLeft / 10;
		}
		
		entity.rotationYaw += velocityYaw;
		entity.rotationPitch += velocityPitch;
		
		if (timeExecuting < 20) {
			entity.rotationYaw = targetYaw;
			entity.rotationPitch = targetPitch;
		}
		
		if (timeExecuting == 20) {
			BendingData data = bender.getData();
			data.chi().setMaxChi(10);
			data.chi().setTotalChi(10);
			data.chi().setAvailableChi(10);
			execAbility();
			data.setAbilityCooldown(80);
		}
		
		if (timeExecuting >= 80) {
			BendingData data = bender.getData();
			execStatusControl(StatusControl.THROW_FIRE);
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
				&& bender.getData().getAbilityCooldown() == 0;
	}
	
	@Override
	public void updateTask() {
		timeExecuting++;
	}
	
	@Override
	public void resetTask() {
		
		EntityFireArc fire = AvatarEntity.lookupEntity(entity.world, EntityFireArc.class, //
				arc -> arc.getBehavior() instanceof FireArcBehavior.PlayerControlled
						&& arc.getOwner() == entity);
		
		if (fire != null) {
			fire.setDead();
			bender.getData().removeStatusControl(StatusControl.THROW_FIRE);
		}
		
	}
	
}
