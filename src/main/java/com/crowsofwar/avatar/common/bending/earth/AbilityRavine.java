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
package com.crowsofwar.avatar.common.bending.earth;

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.entity.EntityRavine;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityRavine extends EarthAbility {
	
	/**
	 * @param controller
	 */
	public AbilityRavine() {
		super("ravine");
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		
		float chi = STATS_CONFIG.chiRavine;
		if (ctx.isMasterLevel(AbilityTreePath.FIRST)) {
			chi *= 1.5f;
		}
		
		if (ctx.consumeChi(chi)) {
			
			AbilityData abilityData = ctx.getData().getAbilityData(this);
			float xp = abilityData.getTotalXp();
			
			EntityLivingBase entity = ctx.getBenderEntity();
			World world = ctx.getWorld();
			
			Vector look = Vector.toRectangular(Math.toRadians(entity.rotationYaw), 0);
			
			double mult = ctx.getLevel() >= 1 ? 14 : 8;
			
			EntityRavine ravine = new EntityRavine(world);
			ravine.setOwner(entity);
			ravine.setPosition(entity.posX, entity.posY, entity.posZ);
			ravine.velocity().set(look.times(mult));
			ravine.setDamageMult(.75f + xp / 100);
			ravine.setDistance(ctx.getLevel() >= 2 ? 16 : 10);
			ravine.setBreakBlocks(ctx.isMasterLevel(AbilityTreePath.FIRST));
			ravine.setDropEquipment(ctx.isMasterLevel(AbilityTreePath.SECOND));
			world.spawnEntity(ravine);
			
		}
		
	}
	
}
