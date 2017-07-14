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
package com.crowsofwar.avatar.common.entity.ai;

import com.crowsofwar.avatar.common.item.AvatarItems;
import com.crowsofwar.avatar.common.item.ItemScroll.ScrollType;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import static com.crowsofwar.gorecore.util.Vector.getEntityPos;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityAiGiveScroll extends EntityAIBase {
	
	private final EntityLiving entity;
	private final ScrollType scrollType;
	private EntityLivingBase target;
	private int ticksExecuting;
	
	public EntityAiGiveScroll(EntityLiving entity, ScrollType scrollType) {
		this.entity = entity;
		this.scrollType = scrollType;
		setMutexBits(1);
	}
	
	/**
	 * Starts giving scroll to the target and returns true. However, if this is
	 * already executing, rejects and returns false.
	 */
	public boolean giveScrollTo(EntityLivingBase player) {
		if (target == null || !target.isEntityAlive()) {
			this.target = player;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldExecute() {
		return target != null && target.isEntityAlive() && entity.getAttackTarget() == null;
	}
	
	@Override
	public void startExecuting() {
		ticksExecuting = 0;
		entity.getLookHelper().setLookPositionWithEntity(target, 10, 10);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		
		entity.getLookHelper().setLookPosition(target.posX, target.posY + target.getEyeHeight(), target.posZ,
				entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
		
		ticksExecuting++;
		if (ticksExecuting >= 50) {
			
			World world = entity.world;
			
			Vector velocity = getEntityPos(target).minus(getEntityPos(entity)).normalize().times(0.3);
			ItemStack scrollStack = new ItemStack(AvatarItems.itemScroll, 1, scrollType.id());
			
			EntityItem entityItem = new EntityItem(world, entity.posX, entity.posY + entity.getEyeHeight(),
					entity.posZ, scrollStack);
			entityItem.setDefaultPickupDelay();
			entityItem.motionX = velocity.x();
			entityItem.motionY = velocity.y();
			entityItem.motionZ = velocity.z();
			world.spawnEntity(entityItem);
			
			target = null;
			
		}
		
		return shouldExecute();
		
	}
	
}
