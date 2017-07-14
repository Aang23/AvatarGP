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

import static com.crowsofwar.avatar.common.bending.BendingAbility.ABILITY_RAVINE;
import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import java.util.List;

import com.crowsofwar.avatar.common.AvatarDamageSource;
import com.crowsofwar.avatar.common.config.ConfigStats;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.BenderInfo;
import com.crowsofwar.avatar.common.entity.data.OwnerAttribute;
import com.crowsofwar.avatar.common.entityproperty.EntityPropertyMotion;
import com.crowsofwar.avatar.common.entityproperty.IEntityProperty;
import com.crowsofwar.avatar.common.util.AvatarDataSerializers;
import com.crowsofwar.avatar.common.util.AvatarUtils;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityRavine extends AvatarEntity {
	
	private static final DataParameter<BenderInfo> SYNC_OWNER = EntityDataManager
			.createKey(EntityRavine.class, AvatarDataSerializers.SERIALIZER_BENDER);
	
	private final IEntityProperty<Vector> propVelocity;
	private Vector initialPosition;
	private final OwnerAttribute ownerAttr;
	
	private float damageMult;
	private double maxTravelDistanceSq;
	private boolean breakBlocks;
	private boolean dropEquipment;
	
	/**
	 * @param world
	 */
	public EntityRavine(World world) {
		super(world);
		
		this.propVelocity = new EntityPropertyMotion(this);
		setSize(1, 1);
		
		this.damageMult = 1;
		this.ownerAttr = new OwnerAttribute(this, SYNC_OWNER);
		
	}
	
	public void setDamageMult(float mult) {
		this.damageMult = mult;
	}
	
	public void setDistance(double dist) {
		maxTravelDistanceSq = dist * dist;
	}
	
	public void setBreakBlocks(boolean breakBlocks) {
		this.breakBlocks = breakBlocks;
	}
	
	public void setDropEquipment(boolean dropEquipment) {
		this.dropEquipment = dropEquipment;
	}
	
	@Override
	public EntityLivingBase getOwner() {
		return ownerAttr.getOwner();
	}
	
	public void setOwner(EntityLivingBase owner) {
		ownerAttr.setOwner(owner);
	}
	
	public double getSqrDistanceTravelled() {
		return position().sqrDist(initialPosition);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		setDead();
	}
	
	@Override
	public void onEntityUpdate() {
		
		super.onEntityUpdate();
		
		if (initialPosition == null) {
			initialPosition = position().copy();
		}
		
		Vector position = position();
		Vector velocity = velocity();
		
		Vector nowPos = position.add(velocity.times(0.05));
		setPosition(nowPos.x(), nowPos.y(), nowPos.z());
		
		if (!world.isRemote && getSqrDistanceTravelled() > maxTravelDistanceSq) {
			setDead();
		}
		
		BlockPos above = getPosition().offset(EnumFacing.UP);
		BlockPos below = getPosition().offset(EnumFacing.DOWN);
		Block belowBlock = world.getBlockState(below).getBlock();
		
		if (ticksExisted % 3 == 0) world.playSound(posX, posY, posZ,
				world.getBlockState(below).getBlock().getSoundType().getBreakSound(),
				SoundCategory.PLAYERS, 1, 1, false);
		
		if (!world.getBlockState(below).isNormalCube()) {
			setDead();
		}
		
		if (!world.isRemote && !ConfigStats.STATS_CONFIG.bendableBlocks.contains(belowBlock)) {
			setDead();
		}
		
		// Destroy if in a block
		IBlockState inBlock = world.getBlockState(getPosition());
		if (inBlock.isFullBlock()) {
			setDead();
		}
		
		// Destroy non-solid blocks in the ravine
		BlockPos inPos = getPosition();
		if (inBlock.getBlock() != Blocks.AIR && !inBlock.isFullBlock()) {
			
			if (inBlock.getBlockHardness(world, getPosition()) == 0) {
				
				breakBlock(getPosition());
				
			} else {
				
				setDead();
				
			}
			
		}
		
		// amount of entities which were successfully attacked
		int attacked = 0;
		
		// Push collided entities back
		if (!world.isRemote) {
			List<Entity> collided = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox(),
					entity -> entity != getOwner());
			if (!collided.isEmpty()) {
				for (Entity entity : collided) {
					if (attackEntity(entity)) {
						attacked++;
					}
				}
			}
		}
		
		if (!world.isRemote && getOwner() != null) {
			BendingData data = ownerAttr.getOwnerBender().getData();
			if (data != null) {
				data.getAbilityData(ABILITY_RAVINE).addXp(SKILLS_CONFIG.ravineHit * attacked);
			}
		}
		
		if (!world.isRemote && breakBlocks) {
			BlockPos last = new BlockPos(prevPosX, prevPosY, prevPosZ);
			if (!last.equals(getPosition()) && !last.equals(initialPosition.toBlockPos())) {
				
				world.destroyBlock(last.down(), true);
				
				double travel = Math.sqrt(getSqrDistanceTravelled() / maxTravelDistanceSq);
				double chance = -(travel - 0.5) * (travel - 0.5) + 0.25;
				chance *= 2;
				
				if (rand.nextDouble() <= chance) {
					world.destroyBlock(last.down(2), true);
				}
				
			}
		}
		
	}
	
	private boolean attackEntity(Entity entity) {
		
		if (!(entity instanceof EntityItem && entity.ticksExisted <= 10)) {
			
			Vector push = velocity().copy().setY(1).mul(STATS_CONFIG.ravineSettings.push);
			entity.addVelocity(push.x(), push.y(), push.z());
			AvatarUtils.afterVelocityAdded(entity);
			
			if (dropEquipment && entity instanceof EntityLivingBase) {
				
				EntityLivingBase elb = (EntityLivingBase) entity;
				
				for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
					
					ItemStack stack = elb.getItemStackFromSlot(slot);
					if (!stack.isEmpty()) {
						double chance = slot.getSlotType() == Type.HAND ? 40 : 20;
						if (rand.nextDouble() * 100 <= chance) {
							elb.entityDropItem(stack, 0);
							elb.setItemStackToSlot(slot, ItemStack.EMPTY);
						}
					}
					
				}
				
			}
			
			DamageSource ds = AvatarDamageSource.causeRavineDamage(entity, getOwner());
			float damage = STATS_CONFIG.ravineSettings.damage * damageMult;
			return entity.attackEntityFrom(ds, damage);
			
		}
		
		return false;
		
	}
	
}
