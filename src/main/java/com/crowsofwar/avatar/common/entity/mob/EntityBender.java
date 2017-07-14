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

import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.data.EntityBenderData;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public abstract class EntityBender extends EntityCreature implements Bender {
	
	private EntityBenderData data;
	
	/**
	 * @param world
	 */
	public EntityBender(World world) {
		super(world);
		data = new EntityBenderData(this);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		data.readFromNbt(nbt);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		data.writeToNbt(nbt);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		data.decrementCooldown();
	}
	
	@Override
	public EntityLivingBase getEntity() {
		return this;
	}
	
	@Override
	public BendingData getData() {
		return data;
	}
	
	@Override
	public boolean isCreativeMode() {
		return false;
	}
	
	@Override
	public boolean isFlying() {
		return false;
	}
	
	@Override
	public boolean isPlayer() {
		return false;
	}
	
	@Override
	public boolean consumeWaterLevel(int amount) {
		return false;
	}
	
}
