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

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AvatarEntityItem extends EntityItem {
	
	private static final DataParameter<Boolean> SYNC_RESIST_FIRE = EntityDataManager
			.createKey(AvatarEntityItem.class, DataSerializers.BOOLEAN);
	
	public AvatarEntityItem(World world) {
		super(world);
	}
	
	public AvatarEntityItem(World worldIn, double x, double y, double z, ItemStack stack) {
		super(worldIn, x, y, z, stack);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_RESIST_FIRE, false);
	}
	
	public boolean resistsFire() {
		return dataManager.get(SYNC_RESIST_FIRE);
	}
	
	public void setResistFire(boolean resistFire) {
		dataManager.set(SYNC_RESIST_FIRE, resistFire);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (resistsFire() && source.isFireDamage()) {
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}
	
}
