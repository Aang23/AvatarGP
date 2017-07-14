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

import java.util.Random;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityAirBubble;

import net.minecraft.entity.EntityLiving;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AiAirBubble extends BendingAi {
	
	private final Random random;
	
	/**
	 * @param ability
	 * @param entity
	 * @param bender
	 */
	protected AiAirBubble(BendingAbility ability, EntityLiving entity, Bender bender) {
		super(ability, entity, bender);
		this.random = new Random();
	}
	
	@Override
	protected void startExec() {
		execAbility();
	}
	
	@Override
	protected boolean shouldExec() {
		
		boolean underAttack = entity.getCombatTracker().getCombatDuration() <= 100 || true;
		boolean already = AvatarEntity.lookupEntity(entity.world, EntityAirBubble.class,
				bubble -> bubble.getOwner() == entity) != null;
		boolean lowHealth = entity.getHealth() / entity.getMaxHealth() <= 0.25f || entity.getHealth() < 10;
		
		// 2% chance to create air bubble every tick
		return !already && underAttack && lowHealth && random.nextDouble() <= 0.02;
		
	}
	
}
