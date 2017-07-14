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

import static com.crowsofwar.avatar.common.bending.StatusControl.THROW_BUBBLE;

import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.data.ctx.BenderInfo;
import com.crowsofwar.avatar.common.entity.data.Behavior;
import com.crowsofwar.avatar.common.entity.data.OwnerAttribute;
import com.crowsofwar.avatar.common.entity.data.WaterBubbleBehavior;
import com.crowsofwar.avatar.common.util.AvatarDataSerializers;

import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityWaterBubble extends AvatarEntity {
	
	private static final DataParameter<WaterBubbleBehavior> SYNC_BEHAVIOR = EntityDataManager
			.createKey(EntityWaterBubble.class, WaterBubbleBehavior.DATA_SERIALIZER);
	private static final DataParameter<BenderInfo> SYNC_OWNER = EntityDataManager
			.createKey(EntityWaterBubble.class, AvatarDataSerializers.SERIALIZER_BENDER);
	
	private final OwnerAttribute ownerAttrib;
	
	/**
	 * Whether the water bubble will create a water source upon landing. Only
	 * set on server-side.
	 */
	private boolean sourceBlock;
	
	/**
	 * @param world
	 */
	public EntityWaterBubble(World world) {
		super(world);
		this.ownerAttrib = new OwnerAttribute(this, SYNC_OWNER);
		setSize(.8f, .8f);
		this.putsOutFires = true;
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_BEHAVIOR, new WaterBubbleBehavior.Drop());
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		velocity().mul(0.9);
		
		WaterBubbleBehavior currentBehavior = getBehavior();
		WaterBubbleBehavior nextBehavior = (WaterBubbleBehavior) currentBehavior.onUpdate(this);
		if (currentBehavior != nextBehavior) setBehavior(nextBehavior);
		
		if (ticksExisted % 5 == 0) {
			BlockPos down = getPosition().down();
			IBlockState downState = world.getBlockState(down);
			if (downState.getBlock() == Blocks.FARMLAND) {
				int moisture = downState.getValue(BlockFarmland.MOISTURE);
				if (moisture < 7) world.setBlockState(down,
						Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, moisture + 1));
			}
		}
		
		boolean inWaterSource = false;
		if (!world.isRemote && ticksExisted % 2 == 1 && ticksExisted > 10) {
			for (int x = 0; x <= 1; x++) {
				for (int z = 0; z <= 1; z++) {
					BlockPos pos = new BlockPos(posX + x * width, posY, posZ + z * width);
					IBlockState state = world.getBlockState(pos);
					if (state.getBlock() == Blocks.WATER && state.getValue(BlockLiquid.LEVEL) == 0) {
						inWaterSource = true;
						break;
					}
				}
			}
		}
		
		if (!world.isRemote && inWaterSource) {
			setDead();
			if (getOwner() != null) {
				BendingData data = Bender.create(getOwner()).getData();
				if (data != null) {
					data.removeStatusControl(StatusControl.THROW_BUBBLE);
				}
			}
		}
		
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		ownerAttrib.load(compound);
		setBehavior((WaterBubbleBehavior) Behavior.lookup(compound.getInteger("Behavior"), this));
		getBehavior().load(compound);
		setSourceBlock(compound.getBoolean("SourceBlock"));
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		ownerAttrib.save(compound);
		compound.setInteger("Behavior", getBehavior().getId());
		getBehavior().save(compound);
		compound.setBoolean("SourceBlock", sourceBlock);
	}
	
	public WaterBubbleBehavior getBehavior() {
		return dataManager.get(SYNC_BEHAVIOR);
	}
	
	public void setBehavior(WaterBubbleBehavior behavior) {
		dataManager.set(SYNC_BEHAVIOR, behavior);
	}
	
	@Override
	public EntityLivingBase getOwner() {
		return ownerAttrib.getOwner();
	}
	
	public void setOwner(EntityLivingBase player) {
		ownerAttrib.setOwner(player);
	}
	
	public boolean isSourceBlock() {
		return sourceBlock;
	}
	
	public void setSourceBlock(boolean sourceBlock) {
		this.sourceBlock = sourceBlock;
	}
	
	@Override
	public EntityLivingBase getController() {
		return getBehavior() instanceof WaterBubbleBehavior.PlayerControlled ? getOwner() : null;
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}
	
	@Override
	public boolean tryDestroy() {
		setBehavior(new WaterBubbleBehavior.Drop());
		if (getOwner() != null) {
			Bender.create(getOwner()).getData().removeStatusControl(THROW_BUBBLE);
		}
		return false;
	}
	
}
