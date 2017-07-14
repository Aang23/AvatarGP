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

import static com.crowsofwar.avatar.common.bending.BendingAbility.ABILITY_FLAMETHROWER;
import static com.crowsofwar.avatar.common.config.ConfigChi.CHI_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;
import static com.crowsofwar.gorecore.util.Vector.getEyePos;
import static com.crowsofwar.gorecore.util.Vector.getVelocityMpS;
import static java.lang.Math.toRadians;

import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.Chi;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.EntityFlames;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class FlamethrowerUpdateTick extends TickHandler {
	
	@Override
	public boolean tick(BendingContext ctx) {
		
		BendingData data = ctx.getData();
		EntityLivingBase entity = ctx.getBenderEntity();
		Bender bender = ctx.getBender();
		
		AbilityData abilityData = data.getAbilityData(ABILITY_FLAMETHROWER);
		AbilityTreePath path = abilityData.getPath();
		float totalXp = abilityData.getTotalXp();
		int level = abilityData.getLevel();
		
		int flamesPerSecond = level == 0 ? 6 : 10;
		if (level == 3 && path == AbilityTreePath.FIRST) {
			flamesPerSecond = 15;
		}
		if (level == 3 && path == AbilityTreePath.SECOND) {
			flamesPerSecond = 8;
		}
		
		if (!entity.world.isRemote && Math.random() < flamesPerSecond / 20.0) {
			
			Chi chi = data.chi();
			float required = STATS_CONFIG.chiFlamethrowerSecond / flamesPerSecond;
			if (level == 3 && path == AbilityTreePath.FIRST) {
				required *= 1.5f;
			}
			if (level == 3 && path == AbilityTreePath.SECOND) {
				required *= 2;
			}
			
			boolean infinite = bender.isCreativeMode() && CHI_CONFIG.infiniteInCreative;
			
			if (infinite || chi.consumeChi(required)) {
				
				Vector eye = getEyePos(entity);
				
				World world = ctx.getWorld();
				
				double speedMult = 6 + 5 * totalXp / 100;
				double randomness = 20 - 10 * totalXp / 100;
				boolean lightsFires = false;
				if (level == 3 && path == AbilityTreePath.FIRST) {
					speedMult = 15;
					randomness = 1;
				}
				if (level == 3 && path == AbilityTreePath.SECOND) {
					speedMult = 8;
					randomness = 20;
					lightsFires = true;
				}
				
				double yawRandom = entity.rotationYaw + (Math.random() * 2 - 1) * randomness;
				double pitchRandom = entity.rotationPitch + (Math.random() * 2 - 1) * randomness;
				Vector look = Vector.toRectangular(toRadians(yawRandom), toRadians(pitchRandom));
				
				EntityFlames flames = new EntityFlames(world, entity);
				flames.velocity().set(look.times(speedMult).plus(getVelocityMpS(entity)));
				flames.setPosition(eye.x(), eye.y(), eye.z());
				flames.setLightsFires(lightsFires);
				world.spawnEntity(flames);
				
				world.playSound(null, entity.getPosition(), SoundEvents.ITEM_FIRECHARGE_USE,
						SoundCategory.PLAYERS, 0.2f, 0.8f);
				
			} else {
				// not enough chi
				return true;
			}
		}
		
		return false;
		
	}
	
}
