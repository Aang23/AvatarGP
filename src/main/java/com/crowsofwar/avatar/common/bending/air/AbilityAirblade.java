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

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;
import static com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath.FIRST;
import static com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath.SECOND;
import static java.lang.Math.abs;

import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.EntityAirblade;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityAirblade extends AirAbility {
	
	public AbilityAirblade() {
		super("airblade");
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		
		EntityLivingBase bender = ctx.getBenderEntity();
		World world = ctx.getWorld();
		
		if (!ctx.consumeChi(STATS_CONFIG.chiAirblade)) return;
		
		double pitchDeg = bender.rotationPitch;
		if (abs(pitchDeg) > 30) {
			pitchDeg = pitchDeg / abs(pitchDeg) * 30;
		}
		float pitch = (float) Math.toRadians(pitchDeg);
		
		Vector look = Vector.toRectangular(Math.toRadians(bender.rotationYaw), pitch);
		Vector spawnAt = Vector.getEntityPos(bender).add(look).add(0, 1, 0);
		spawnAt.add(look);
		
		AbilityData abilityData = ctx.getData().getAbilityData(this);
		float xp = abilityData.getTotalXp();
		
		EntityAirblade airblade = new EntityAirblade(world);
		airblade.setPosition(spawnAt.x(), spawnAt.y(), spawnAt.z());
		airblade.velocity().set(look.times(ctx.getLevel() >= 1 ? 30 : 20));
		airblade.setDamage(STATS_CONFIG.airbladeSettings.damage * (1 + xp * .015f));
		airblade.setOwner(bender);
		airblade.setPierceArmor(abilityData.isMasterPath(SECOND));
		airblade.setChainAttack(abilityData.isMasterPath(FIRST));
		
		float chopBlocks = -1;
		if (abilityData.getLevel() >= 1) {
			chopBlocks = 0;
		}
		if (abilityData.isMasterPath(SECOND)) {
			chopBlocks = 2;
		}
		airblade.setChopBlocksThreshold(chopBlocks);
		
		world.spawnEntity(airblade);
		
	}
	
	@Override
	public BendingAi getAi(EntityLiving entity, Bender bender) {
		return new AiAirblade(this, entity, bender);
	}
	
}
