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
package com.crowsofwar.avatar.common.bending.air;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

import static com.crowsofwar.gorecore.util.Vector.getEntityPos;
import static com.crowsofwar.gorecore.util.Vector.getRotationTo;
import static java.lang.Math.toDegrees;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AiAirblade extends BendingAi {
	
	/**
	 * @param ability
	 * @param entity
	 * @param bender
	 */
	protected AiAirblade(BendingAbility ability, EntityLiving entity, Bender bender) {
		super(ability, entity, bender);
	}
	
	@Override
	protected void startExec() {
		EntityLivingBase target = entity.getAttackTarget();
		Vector rotations = getRotationTo(getEntityPos(entity), getEntityPos(target));
		entity.rotationYaw = (float) toDegrees(rotations.y());
		entity.rotationPitch = (float) toDegrees(rotations.x());
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		entity.rotationYaw = entity.rotationYawHead;
		if (timeExecuting >= 40 && entity.getAttackTarget() != null) {
			Vector rotations = getRotationTo(getEntityPos(entity), getEntityPos(entity.getAttackTarget()));
			entity.rotationYaw = (float) toDegrees(rotations.y());
			entity.rotationPitch = (float) toDegrees(rotations.x());
			execAbility();
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean shouldExec() {
		
		EntityLivingBase target = entity.getAttackTarget();
		
		if (target != null) {
			double dist = entity.getDistanceSqToEntity(target);
			return dist >= 4 * 4;
		}
		
		return false;
		
	}
	
}
