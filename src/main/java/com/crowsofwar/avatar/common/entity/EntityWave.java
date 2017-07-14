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
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import java.util.List;

import com.crowsofwar.avatar.common.AvatarDamageSource;
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.data.ctx.BenderInfo;
import com.crowsofwar.avatar.common.entity.data.OwnerAttribute;
import com.crowsofwar.avatar.common.util.AvatarDataSerializers;
import com.crowsofwar.gorecore.util.BackedVector;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityWave extends Entity {
	
	private static final DataParameter<BenderInfo> SYNC_OWNER = EntityDataManager.createKey(EntityWave.class,
			AvatarDataSerializers.SERIALIZER_BENDER);
	private static final DataParameter<Float> SYNC_SIZE = EntityDataManager.createKey(EntityWave.class,
			DataSerializers.FLOAT);
	
	private final Vector internalVelocity;
	private final Vector internalPosition;
	private final OwnerAttribute ownerAttr;
	
	private float damageMult;
	private int timeOnLand;
	
	public EntityWave(World world) {
		super(world);
		//@formatter:off
		this.internalVelocity = new BackedVector(x -> this.motionX = x / 20, y -> this.motionY = y / 20, z -> this.motionZ = z / 20,
				() -> this.motionX * 20, () -> this.motionY * 20, () -> this.motionZ * 20);
		this.internalPosition = new Vector();
		
		setSize(2, 2);
		
		damageMult = 1;
		
		ownerAttr = new OwnerAttribute(this, SYNC_OWNER);
		
	}
	
	@Override
	protected void entityInit() {
		dataManager.register(SYNC_SIZE, 2f);
	}
	
	public void setDamageMultiplier(float damageMult) {
		this.damageMult = damageMult;
	}
	
	public float getWaveSize() {
		return dataManager.get(SYNC_SIZE);
	}
	
	public void setWaveSize(float size) {
		dataManager.set(SYNC_SIZE, size);
	}
	
	@Override
	public void onUpdate() {
		
		setSize(getWaveSize() * 0.75f, 2);
		
		EntityLivingBase owner = getOwner();
		
		Vector move = velocity().dividedBy(20);
		Vector newPos = getVecPosition().add(move);
		setPosition(newPos.x(), newPos.y(), newPos.z());
		
		if (!world.isRemote) {
			List<Entity> collided = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox(), entity -> entity != owner);
			for (Entity entity : collided) {
				Vector motion = velocity().dividedBy(20).times(STATS_CONFIG.waveSettings.push);
				motion.setY(0.4);
				entity.addVelocity(motion.x(), motion.y(), motion.z());
				entity.attackEntityFrom(AvatarDamageSource.causeWaveDamage(entity, owner), STATS_CONFIG.waveSettings.damage * damageMult);
			}
			if (!collided.isEmpty()) {
				BendingData data = Bender.create(owner).getData();
				if (data != null) {
					data.getAbilityData(BendingAbility.ABILITY_WAVE).addXp(SKILLS_CONFIG.waveHit);
				}
			}
		}
		
		if (ticksExisted > 7000) {
			setDead();
		}
		if (!world.isRemote && world.getBlockState(getPosition()).getBlock() != Blocks.WATER) {
			timeOnLand++;
			if (timeOnLand >= maxTimeOnLand()) {
				setDead();
			}
		}
		
	}
	
	private int maxTimeOnLand() {
		if (getOwner() != null) {
			AbilityData data = Bender.getData(getOwner()).getAbilityData(BendingAbility.ABILITY_WAVE);
			if (data.isMasterPath(AbilityTreePath.FIRST)) {
				return 30;
			}
		}
		return 0;
	}
	
	public Vector getVecPosition() {
		return internalPosition.set(posX, posY, posZ);
	}
	
	/**
	 * Get velocity in m/s. Any modifications to this vector will modify the entity motion fields.
	 */
	public Vector velocity() {
		return internalVelocity;
	}
	
	public EntityLivingBase getOwner() {
		return ownerAttr.getOwner();
	}
	
	public void setOwner(EntityLivingBase owner) {
		ownerAttr.setOwner(owner);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		setDead();
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		// TODO Save/load waves??
		setDead();
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}
	
}
