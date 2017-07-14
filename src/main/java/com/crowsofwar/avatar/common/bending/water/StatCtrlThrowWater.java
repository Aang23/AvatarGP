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

import java.util.List;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.controls.AvatarControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.EntityWaterArc;
import com.crowsofwar.avatar.common.entity.data.WaterArcBehavior;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class StatCtrlThrowWater extends StatusControl {
	
	public StatCtrlThrowWater() {
		super(3, AvatarControl.CONTROL_LEFT_CLICK, CrosshairPosition.LEFT_OF_CROSSHAIR);
	}
	
	@Override
	public boolean execute(BendingContext ctx) {
		
		EntityLivingBase entity = ctx.getBenderEntity();
		BendingData data = ctx.getData();
		World world = ctx.getWorld();
		AbilityData abilityData = data.getAbilityData(BendingAbility.ABILITY_WATER_ARC);
		
		int lvl = abilityData.getLevel();
		double velocity = 6;
		if (lvl >= 2) {
			velocity = 10;
		}
		if (abilityData.isMasterPath(AbilityTreePath.SECOND)) {
			velocity = 8;
		}
		
		AxisAlignedBB boundingBox = new AxisAlignedBB(entity.posX - 5, entity.posY - 5, entity.posZ - 5,
				entity.posX + 5, entity.posY + 5, entity.posZ + 5);
		List<EntityWaterArc> existing = world.getEntitiesWithinAABB(EntityWaterArc.class, boundingBox,
				arc -> arc.getOwner() == entity
						&& arc.getBehavior() instanceof WaterArcBehavior.PlayerControlled);
		
		for (EntityWaterArc arc : existing) {
			arc.setBehavior(new WaterArcBehavior.Thrown());
			
			Vector force = Vector.toRectangular(Math.toRadians(entity.rotationYaw),
					Math.toRadians(entity.rotationPitch));
			force.mul(velocity);
			arc.velocity().add(force);
			arc.setBehavior(new WaterArcBehavior.Thrown());
			
		}
		
		return true;
		
	}
	
}
