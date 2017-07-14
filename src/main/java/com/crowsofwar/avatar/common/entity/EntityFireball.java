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

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.data.ctx.BenderInfo;
import com.crowsofwar.avatar.common.entity.data.Behavior;
import com.crowsofwar.avatar.common.entity.data.FireballBehavior;
import com.crowsofwar.avatar.common.entity.data.OwnerAttribute;
import com.crowsofwar.avatar.common.util.AvatarDataSerializers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityFireball extends AvatarEntity {
	
	public static final DataParameter<BenderInfo> SYNC_OWNER = EntityDataManager
			.createKey(EntityFireball.class, AvatarDataSerializers.SERIALIZER_BENDER);
	public static final DataParameter<FireballBehavior> SYNC_BEHAVIOR = EntityDataManager
			.createKey(EntityFireball.class, FireballBehavior.DATA_SERIALIZER);
	public static final DataParameter<Integer> SYNC_SIZE = EntityDataManager.createKey(EntityFireball.class,
			DataSerializers.VARINT);
	
	private final OwnerAttribute ownerAttr;
	private AxisAlignedBB expandedHitbox;
	
	private float damage;
	
	/**
	 * @param world
	 */
	public EntityFireball(World world) {
		super(world);
		this.ownerAttr = new OwnerAttribute(this, SYNC_OWNER);
		setSize(.8f, .8f);
	}
	
	@Override
	public void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_BEHAVIOR, new FireballBehavior.Idle());
		dataManager.register(SYNC_SIZE, 30);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		setBehavior((FireballBehavior) getBehavior().onUpdate(this));
		
		if (inWater) {
			removeStatCtrl();
			int particles = rand.nextInt(4) + 5;
			for (int i = 0; i < particles; i++) {
				world.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ, rand.nextGaussian() * .05,
						rand.nextDouble() * .2, rand.nextGaussian() * .05);
			}
			world.playSound(posX, posY, posZ, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
					SoundCategory.PLAYERS, 1, rand.nextFloat() * 0.3f + 1.1f, false);
			setDead();
		}
		
		// TODO Temporary fix to avoid extra fireballs
		// Add hook or something
		if (getOwner() == null) {
			setDead();
		}
		
	}
	
	@Override
	public EntityLivingBase getOwner() {
		return ownerAttr.getOwner();
	}
	
	public void setOwner(EntityLivingBase owner) {
		ownerAttr.setOwner(owner);
	}
	
	public FireballBehavior getBehavior() {
		return dataManager.get(SYNC_BEHAVIOR);
	}
	
	public void setBehavior(FireballBehavior behavior) {
		dataManager.set(SYNC_BEHAVIOR, behavior);
	}
	
	@Override
	public EntityLivingBase getController() {
		return getBehavior() instanceof FireballBehavior.PlayerControlled ? getOwner() : null;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public void setDamage(float damage) {
		this.damage = damage;
	}
	
	public int getSize() {
		return dataManager.get(SYNC_SIZE);
	}
	
	public void setSize(int size) {
		dataManager.set(SYNC_SIZE, size);
	}
	
	@Override
	public void onCollideWithSolid() {
		
		float explosionSize = STATS_CONFIG.fireballSettings.explosionSize;
		explosionSize *= getSize() / 30f;
		boolean destroyObsidian = false;
		
		if (getOwner() != null) {
			AbilityData abilityData = Bender.getData(getOwner())
					.getAbilityData(BendingAbility.ABILITY_FIREBALL);
			if (abilityData.isMasterPath(AbilityTreePath.FIRST)) {
				destroyObsidian = true;
			}
		}
		
		Explosion explosion = new Explosion(world, this, posX, posY, posZ, explosionSize,
				!world.isRemote, STATS_CONFIG.fireballSettings.damageBlocks);
		if (!ForgeEventFactory.onExplosionStart(world, explosion)) {
			
			explosion.doExplosionA();
			explosion.doExplosionB(true);
			
		}
		
		if (destroyObsidian) {
			for (EnumFacing dir : EnumFacing.values()) {
				BlockPos pos = getPosition().offset(dir);
				if (world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN) {
					world.destroyBlock(pos, true);
				}
			}
		}
		
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		ownerAttr.load(nbt);
		setDamage(nbt.getFloat("Damage"));
		setBehavior((FireballBehavior) Behavior.lookup(nbt.getInteger("Behavior"), this));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		ownerAttr.save(nbt);
		nbt.setFloat("Damage", getDamage());
		nbt.setInteger("Behavior", getBehavior().getId());
	}
	
	public AxisAlignedBB getExpandedHitbox() {
		return this.expandedHitbox;
	}
	
	@Override
	public void setEntityBoundingBox(AxisAlignedBB bb) {
		super.setEntityBoundingBox(bb);
		expandedHitbox = bb.expand(0.35, 0.35, 0.35);
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 0 || pass == 1;
	}
	
	private void removeStatCtrl() {
		if (getOwner() != null) {
			BendingData data = Bender.create(getOwner()).getData();
			data.removeStatusControl(StatusControl.THROW_FIREBALL);
		}
	}
	
	@Override
	public boolean tryDestroy() {
		removeStatCtrl();
		return true;
	}
	
}
