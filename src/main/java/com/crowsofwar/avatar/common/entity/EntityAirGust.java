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
package com.crowsofwar.avatar.common.entity;

import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.util.AvatarUtils.afterVelocityAdded;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityAirGust extends EntityArc {
	
	public static final Vector ZERO = new Vector(0, 0, 0);
	
	private boolean airGrab, destroyProjectiles;
	
	public EntityAirGust(World world) {
		super(world);
		setSize(0.5f, 0.5f);
		putsOutFires = true;
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		airGrab = nbt.getBoolean("AirGrab");
		destroyProjectiles = nbt.getBoolean("DestroyProjectiles");
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("AirGrab", airGrab);
		nbt.setBoolean("DestroyProjectiles", destroyProjectiles);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		ControlPoint first = getControlPoint(0);
		ControlPoint second = getControlPoint(1);
		if (first.position().sqrDist(second.position()) >= getControlPointMaxDistanceSq()
				|| ticksExisted > 80) {
			setDead();
		}
	}
	
	@Override
	protected void onCollideWithEntity(Entity entity) {
		EntityLivingBase owner = getOwner();
		if (!entity.world.isRemote && entity != owner) {
			
			BendingData data = Bender.create(owner).getData();
			float xp = 0;
			if (data != null) {
				AbilityData abilityData = data.getAbilityData(BendingAbility.ABILITY_AIR_GUST);
				xp = abilityData.getTotalXp();
				abilityData.addXp(SKILLS_CONFIG.airGustHit);
			}
			
			Vector velocity = velocity().times(0.15).times(1 + xp / 200.0);
			velocity.setY(airGrab ? -1 : 1);
			velocity.mul(airGrab ? -0.8 : 1);
			
			entity.addVelocity(velocity.x(), velocity.y(), velocity.z());
			afterVelocityAdded(entity);
			
			setDead();
			
			if (entity instanceof AvatarEntity) {
				if (((AvatarEntity) entity).tryDestroy()) {
					entity.setDead();
				}
			}
			
		}
	}
	
	@Override
	protected boolean canCollideWith(Entity entity) {
		return true;
	}
	
	@Override
	public void onCollideWithSolid() {
		if (tryDestroy()) {
			setDead();
		}
	}
	
	@Override
	protected Vector getGravityVector() {
		return ZERO;
	}
	
	@Override
	protected ControlPoint createControlPoint(float size) {
		return new AirGustControlPoint(this, 0.5f, 0, 0, 0);
	}
	
	@Override
	public int getAmountOfControlPoints() {
		return 2;
	}
	
	@Override
	protected double getControlPointMaxDistanceSq() {
		return 400; // 20
	}
	
	@Override
	protected double getControlPointTeleportDistanceSq() {
		// Note: Is not actually called.
		// Set dead as soon as reached sq-distance
		return 200;
	}
	
	public boolean doesAirGrab() {
		return airGrab;
	}
	
	public void setAirGrab(boolean airGrab) {
		this.airGrab = airGrab;
	}
	
	public boolean doesDestroyProjectiles() {
		return destroyProjectiles;
	}
	
	public void setDestroyProjectiles(boolean destroyProjectiles) {
		this.destroyProjectiles = destroyProjectiles;
	}
	
	public static class AirGustControlPoint extends ControlPoint {
		
		public AirGustControlPoint(EntityArc arc, float size, double x, double y, double z) {
			super(arc, size, x, y, z);
		}
		
		@Override
		public void onUpdate() {
			super.onUpdate();
			if (arc.getControlPoint(0) == this) {
				float expansionRate = 1f / 20;
				size += expansionRate;
			}
		}
		
	}
	
}
