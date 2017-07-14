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
package com.crowsofwar.avatar.common.bending.water;

import static com.crowsofwar.avatar.common.bending.StatusControl.CrosshairPosition.BELOW_CROSSHAIR;
import static com.crowsofwar.avatar.common.controls.AvatarControl.CONTROL_JUMP;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.util.AvatarUtils;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLivingBase;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class StatCtrlSkateJump extends StatusControl {
	
	public StatCtrlSkateJump() {
		super(9, CONTROL_JUMP, BELOW_CROSSHAIR);
	}
	
	@Override
	public boolean execute(BendingContext ctx) {
		BendingData data = ctx.getData();
		EntityLivingBase entity = ctx.getBenderEntity();
		if (data.hasTickHandler(TickHandler.WATER_SKATE)) {
			data.removeTickHandler(TickHandler.WATER_SKATE);
			
			Vector velocity = Vector.getLookRectangular(entity);
			velocity.mul(1.5);
			entity.motionX = velocity.x() * 2;
			entity.motionY = velocity.y();
			entity.motionZ = velocity.z() * 2;
			AvatarUtils.afterVelocityAdded(entity);
			
			data.setFallAbsorption(6);
			AbilityData abilityData = data.getAbilityData(BendingAbility.ABILITY_WATER_SKATE);
			if (abilityData.isMasterPath(AbilityTreePath.SECOND)) {
				data.addTickHandler(TickHandler.SMASH_GROUND);
			}
			
		}
		
		return true;
	}
	
}
