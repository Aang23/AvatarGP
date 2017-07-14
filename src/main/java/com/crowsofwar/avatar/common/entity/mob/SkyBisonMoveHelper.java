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
package com.crowsofwar.avatar.common.entity.mob;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class SkyBisonMoveHelper extends EntityMoveHelper {
	
	private final EntitySkyBison entity;
	private int courseChangeCooldown;
	
	public SkyBisonMoveHelper(EntitySkyBison entity) {
		super(entity);
		this.entity = entity;
	}
	
	@Override
	public void onUpdateMoveHelper() {
		if (this.action == EntityMoveHelper.Action.MOVE_TO) {
			
			if (this.courseChangeCooldown-- <= 0) {
				this.courseChangeCooldown += this.entity.getRNG().nextInt(5) + 2;
				double x = this.posX - this.entity.posX;
				double y = this.posY - this.entity.posY;
				double z = this.posZ - this.entity.posZ;
				double distance = MathHelper.sqrt(x * x + y * y + z * z);
				
				if (entity.isSitting() || isNotColliding(this.posX, this.posY, this.posZ, distance)) {
					double mult = entity.getSpeedMultiplier();
					this.entity.motionX += x / distance * 0.1 * mult;
					this.entity.motionY += y / distance * 0.1 * mult;
					this.entity.motionZ += z / distance * 0.1 * mult;
					
					float f9 = (float) (MathHelper.atan2(z, x) * (180D / Math.PI)) - 90.0F;
					this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f9, 90.0F);
					
				} else {
					this.action = EntityMoveHelper.Action.WAIT;
				}
			}
		}
	}
	
	/**
	 * Checks if entity bounding box is not colliding with terrain
	 */
	private boolean isNotColliding(double x, double y, double z, double p_179926_7_) {
		double d0 = (x - this.entity.posX) / p_179926_7_;
		double d1 = (y - this.entity.posY) / p_179926_7_;
		double d2 = (z - this.entity.posZ) / p_179926_7_;
		AxisAlignedBB axisalignedbb = this.entity.getEntityBoundingBox();
		
		for (int i = 1; i < p_179926_7_; ++i) {
			axisalignedbb = axisalignedbb.offset(d0, d1, d2);
			
			if (!this.entity.world.getCollisionBoxes(this.entity, axisalignedbb).isEmpty()) {
				return false;
			}
		}
		
		return true;
	}
	
}
