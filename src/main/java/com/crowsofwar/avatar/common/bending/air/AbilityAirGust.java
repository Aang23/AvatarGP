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

import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.EntityAirGust;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityAirGust extends AirAbility {
	
	/**
	 * @param controller
	 */
	public AbilityAirGust() {
		super("air_gust");
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		
		EntityLivingBase bender = ctx.getBenderEntity();
		World world = ctx.getWorld();
		
		if (!ctx.consumeChi(STATS_CONFIG.chiAirGust)) return;
		
		Vector look = Vector.toRectangular(Math.toRadians(bender.rotationYaw),
				Math.toRadians(bender.rotationPitch));
		Vector pos = Vector.getEyePos(bender);
		
		EntityAirGust gust = new EntityAirGust(world);
		gust.velocity().set(look.times(25));
		gust.setPosition(pos.x(), pos.y(), pos.z());
		gust.setOwner(bender);
		gust.setDestroyProjectiles(ctx.isMasterLevel(FIRST));
		gust.setAirGrab(ctx.isMasterLevel(SECOND));
		
		world.spawnEntity(gust);
	}
	
	@Override
	public int getCooldown(AbilityContext ctx) {
		return ctx.getLevel() >= 1 ? 30 : 60;
	}
	
	@Override
	public BendingAi getAi(EntityLiving entity, Bender bender) {
		return new AiAirGust(this, entity, bender);
	}
	
}
