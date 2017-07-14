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
package com.crowsofwar.avatar.common.data.ctx;

import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.item.AvatarItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class PlayerBender implements Bender {
	
	private final EntityPlayer player;
	
	public PlayerBender(EntityPlayer player) {
		this.player = player;
	}
	
	@Override
	public EntityLivingBase getEntity() {
		return player;
	}
	
	@Override
	public BendingData getData() {
		return AvatarPlayerData.fetcher().fetch(player);
	}
	
	@Override
	public boolean isCreativeMode() {
		return player.capabilities.isCreativeMode;
	}
	
	@Override
	public boolean isFlying() {
		return player.capabilities.isFlying;
	}
	
	@Override
	public boolean consumeWaterLevel(int amount) {
		
		int total = 0;
		InventoryPlayer inv = player.inventory;
		
		int inventorySlots = 36;
		for (int i = 0; i < inventorySlots; i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.getItem() == AvatarItems.itemWaterPouch) {
				total += stack.getMetadata();
			}
		}
		
		if (total >= amount) {
			
			// Reduce water pouch level
			if (!isCreativeMode()) {
				int i = 0;
				while (amount > 0) {
					ItemStack stack = inv.getStackInSlot(i);
					if (stack.getItem() == AvatarItems.itemWaterPouch) {
						int oldMetadata = stack.getMetadata();
						int newMetadata = stack.getMetadata() - amount;
						if (newMetadata < 0) newMetadata = 0;
						amount -= oldMetadata - newMetadata;
						stack.setItemDamage(newMetadata);
					}
					i++;
				}
			}
			
			return true;
			
		} else {
			return false;
		}
		
	}
	
}
