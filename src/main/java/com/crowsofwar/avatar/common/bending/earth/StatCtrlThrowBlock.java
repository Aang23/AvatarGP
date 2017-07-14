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

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.controls.AvatarControl;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityFloatingBlock;
import com.crowsofwar.avatar.common.entity.data.FloatingBlockBehavior;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class StatCtrlThrowBlock extends StatusControl {
	
	public StatCtrlThrowBlock() {
		super(2, AvatarControl.CONTROL_LEFT_CLICK_DOWN, CrosshairPosition.LEFT_OF_CROSSHAIR);
	}
	
	@Override
	public boolean execute(BendingContext ctx) {
		
		BendingController controller = BendingManager.getBending(BendingType.EARTHBENDING);
		
		EntityLivingBase entity = ctx.getBenderEntity();
		World world = entity.world;
		BendingData data = ctx.getData();
		
		EntityFloatingBlock floating = AvatarEntity.lookupControlledEntity(world, EntityFloatingBlock.class,
				entity);
		
		if (floating != null) {
			
			float yaw = (float) Math.toRadians(entity.rotationYaw);
			float pitch = (float) Math.toRadians(entity.rotationPitch);
			
			// Calculate force and everything
			double forceMult = data.getAbilityData(BendingAbility.ABILITY_PICK_UP_BLOCK).getLevel() >= 1 //
					? 35 : 25;
			Vector lookDir = Vector.toRectangular(yaw, pitch);
			floating.velocity().add(lookDir.times(forceMult));
			floating.setBehavior(new FloatingBlockBehavior.Thrown());
			
			data.removeStatusControl(PLACE_BLOCK);
			
		}
		
		return true;
		
	}
	
}
