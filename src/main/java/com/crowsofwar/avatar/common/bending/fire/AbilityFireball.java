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
import static com.crowsofwar.gorecore.util.Vector.getEyePos;
import static com.crowsofwar.gorecore.util.Vector.getLookRectangular;

import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.EntityFireball;
import com.crowsofwar.avatar.common.entity.data.FireballBehavior;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityFireball extends FireAbility {
	
	public AbilityFireball() {
		super("fireball");
		requireRaytrace(2.5, false);
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		
		EntityLivingBase entity = ctx.getBenderEntity();
		World world = ctx.getWorld();
		BendingData data = ctx.getData();
		
		if (data.hasStatusControl(StatusControl.THROW_FIREBALL)) return;
		
		if (ctx.consumeChi(STATS_CONFIG.chiFireball)) {
			
			Vector target;
			if (ctx.isLookingAtBlock()) {
				target = ctx.getLookPos();
			} else {
				Vector playerPos = getEyePos(entity);
				target = playerPos.plus(getLookRectangular(entity).times(2.5));
			}
			
			float xp = data.getAbilityData(this).getTotalXp();
			float damage = STATS_CONFIG.fireballSettings.damage;
			damage *= ctx.getLevel() >= 2 ? 2.5f : 1f;
			
			EntityFireball fireball = new EntityFireball(world);
			fireball.position().set(target);
			fireball.setOwner(entity);
			fireball.setBehavior(new FireballBehavior.PlayerControlled());
			fireball.setDamage(damage);
			if (ctx.isMasterLevel(AbilityTreePath.SECOND)) fireball.setSize(20);
			world.spawnEntity(fireball);
			
			data.addStatusControl(StatusControl.THROW_FIREBALL);
			
		}
		
	}
	
	@Override
	public BendingAi getAi(EntityLiving entity, Bender bender) {
		return new AiFireball(this, entity, bender);
	}
	
}
