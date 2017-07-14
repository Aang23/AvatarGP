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

import java.util.List;
import java.util.Random;

import com.crowsofwar.avatar.common.AvatarDamageSource;
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entityproperty.EntityPropertyMotion;
import com.crowsofwar.avatar.common.entityproperty.IEntityProperty;
import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityFlames extends AvatarEntity {
	
	private final IEntityProperty<Vector> propVelocity;
	
	/**
	 * The owner, null client side
	 */
	private EntityLivingBase owner;
	
	private boolean lightsFires;
	
	/**
	 * @param worldIn
	 */
	public EntityFlames(World worldIn) {
		super(worldIn);
		this.propVelocity = new EntityPropertyMotion(this);
		setSize(0.1f, 0.1f);
	}
	
	public EntityFlames(World world, EntityLivingBase owner) {
		this(world);
		this.owner = owner;
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		// TODO Support saving/loading of EntityFlames
		super.readEntityFromNBT(nbt);
		setDead();
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		setDead();
	}
	
	@Override
	public void onUpdate() {
		
		super.onUpdate();
		
		velocity().mul(0.94);
		
		if (velocity().sqrMagnitude() <= 0.5 * 0.5 || isCollided) setDead();
		
		Raytrace.Result raytrace = Raytrace.raytrace(world, position(), velocity().copy().normalize(), 0.3,
				true);
		if (raytrace.hitSomething()) {
			EnumFacing sideHit = raytrace.getSide();
			velocity().set(velocity().reflect(new Vector(sideHit)).times(0.5));
			
			// Try to light firest
			if (lightsFires && sideHit != EnumFacing.DOWN && !world.isRemote) {
				
				BlockPos bouncingOff = getPosition().add(-sideHit.getFrontOffsetX(),
						-sideHit.getFrontOffsetY(), -sideHit.getFrontOffsetZ());
				
				if (sideHit == EnumFacing.UP || world.getBlockState(bouncingOff).getBlock()
						.isFlammable(world, bouncingOff, sideHit)) {
					
					world.setBlockState(getPosition(), Blocks.FIRE.getDefaultState());
					
				}
				
			}
			
		}
		
		if (!world.isRemote) {
			BendingData data = Bender.create(owner).getData();
			AbilityData abilityData = data.getAbilityData(BendingAbility.ABILITY_FLAMETHROWER);
			
			List<Entity> collided = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox(),
					entity -> entity != owner && !(entity instanceof EntityFlames));
			
			for (Entity entity : collided) {
				
				entity.setFire((int) (3 * 1 + abilityData.getTotalXp() / 100f));
				
				// Add extra damage
				// Adding 0 since even though this doesn't affect health, will
				// cause mobs to aggro
				
				float additionalDamage = 0;
				if (abilityData.getTotalXp() >= 50) {
					additionalDamage = 2 + (abilityData.getTotalXp() - 50) / 25;
				}
				entity.attackEntityFrom(AvatarDamageSource.causeFlamethrowerDamage(entity, owner),
						additionalDamage);
				
			}
			
			abilityData.addXp(SKILLS_CONFIG.flamethrowerHit * collided.size());
			if (!collided.isEmpty()) setDead();
		}
		
		handleWaterMovement();
		if (inWater) {
			setDead();
			showExtinguished();
		}
		if (world.isRainingAt(getPosition())) {
			setDead();
			if (Math.random() < 0.3) showExtinguished();
		}
		
	}
	
	/**
	 * Plays an extinguishing sound and particles
	 */
	private void showExtinguished() {
		Random random = new Random();
		if (world.isRemote) {
			world.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ,
					(random.nextGaussian() - 0.5) * 0.05 + motionX / 10, random.nextGaussian() * 0.08,
					(random.nextGaussian() - 0.5) * 0.05 + motionZ / 10);
		}
		world.playSound(posX, posY, posZ, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
				SoundCategory.PLAYERS, 0.3f, random.nextFloat() * 0.3f + 1.1f, false);
	}
	
	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {}
	
	public boolean doesLightFires() {
		return lightsFires;
	}
	
	public void setLightsFires(boolean lightsFires) {
		this.lightsFires = lightsFires;
	}
	
}
