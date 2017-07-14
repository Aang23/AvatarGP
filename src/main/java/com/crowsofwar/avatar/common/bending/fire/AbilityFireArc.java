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

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.EntityFireArc;
import com.crowsofwar.avatar.common.entity.data.FireArcBehavior;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityFireArc extends FireAbility {
	
	/**
	 * @param controller
	 */
	public AbilityFireArc() {
		super("fire_arc");
		requireRaytrace(-1, false);
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		
		if (ctx.consumeChi(STATS_CONFIG.chiFireArc)) {
			
			EntityLivingBase entity = ctx.getBenderEntity();
			World world = ctx.getWorld();
			BendingData data = ctx.getData();
			
			Vector lookPos;
			if (ctx.isLookingAtBlock()) {
				lookPos = ctx.getLookPos();
			} else {
				Vector look = Vector.getLookRectangular(entity);
				lookPos = Vector.getEyePos(entity).plus(look.times(3));
			}
			
			EntityFireArc fire = new EntityFireArc(world);
			fire.setPosition(lookPos.x(), lookPos.y(), lookPos.z());
			fire.setBehavior(new FireArcBehavior.PlayerControlled());
			fire.setOwner(entity);
			fire.setDamageMult(ctx.getLevel() >= 2 ? 2 : 1);
			fire.setCreateBigFire(ctx.isMasterLevel(AbilityTreePath.FIRST));
			
			world.spawnEntity(fire);
			
			data.addStatusControl(StatusControl.THROW_FIRE);
			
		}
		
	}
	
	@Override
	public BendingAi getAi(EntityLiving entity, Bender bender) {
		return new AiFireArc(this, entity, bender);
	}
	
}
