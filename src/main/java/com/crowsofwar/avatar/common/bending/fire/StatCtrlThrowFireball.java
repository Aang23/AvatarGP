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

import static com.crowsofwar.avatar.common.bending.StatusControl.CrosshairPosition.LEFT_OF_CROSSHAIR;
import static com.crowsofwar.avatar.common.controls.AvatarControl.CONTROL_LEFT_CLICK;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityFireball;
import com.crowsofwar.avatar.common.entity.data.FireballBehavior;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class StatCtrlThrowFireball extends StatusControl {
	
	public StatCtrlThrowFireball() {
		super(10, CONTROL_LEFT_CLICK, LEFT_OF_CROSSHAIR);
	}
	
	@Override
	public boolean execute(BendingContext ctx) {
		EntityLivingBase entity = ctx.getBenderEntity();
		World world = ctx.getWorld();
		
		double size = 6;
		
		EntityFireball fireball = AvatarEntity.lookupControlledEntity(world, EntityFireball.class, entity);
		
		if (fireball != null) {
			AbilityData abilityData = ctx.getData().getAbilityData(BendingAbility.ABILITY_FIREBALL);
			double speedMult = abilityData.getLevel() >= 1 ? 25 : 15;
			fireball.velocity().add(Vector.getLookRectangular(entity).mul(speedMult));
			fireball.setBehavior(new FireballBehavior.Thrown());
		}
		
		return true;
	}
	
}
