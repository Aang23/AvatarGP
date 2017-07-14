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

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.controls.AvatarControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityFireArc;
import com.crowsofwar.avatar.common.entity.data.FireArcBehavior;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLivingBase;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class StatCtrlThrowFire extends StatusControl {
	
	public StatCtrlThrowFire() {
		super(6, AvatarControl.CONTROL_LEFT_CLICK_DOWN, CrosshairPosition.LEFT_OF_CROSSHAIR);
	}
	
	@Override
	public boolean execute(BendingContext ctx) {
		
		EntityLivingBase entity = ctx.getBenderEntity();
		BendingData data = ctx.getData();
		
		EntityFireArc fire = AvatarEntity.lookupEntity(ctx.getWorld(), EntityFireArc.class, //
				arc -> arc.getBehavior() instanceof FireArcBehavior.PlayerControlled
						&& arc.getOwner() == ctx.getBenderEntity());
		
		if (fire != null) {
			
			AbilityData abilityData = data.getAbilityData(BendingAbility.ABILITY_FIRE_ARC);
			
			Vector force = Vector.toRectangular(Math.toRadians(entity.rotationYaw),
					Math.toRadians(entity.rotationPitch));
			force.mul(abilityData.getLevel() >= 1 ? 12 : 8);
			fire.velocity().add(force);
			fire.setBehavior(new FireArcBehavior.Thrown());
			
			return true;
			
		}
		
		return false;
	}
	
}
