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
package com.crowsofwar.avatar.common.gui;

import static net.minecraft.item.ItemStack.EMPTY;

import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.item.AvatarItems;
import com.crowsofwar.avatar.common.item.ItemScroll.ScrollType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ContainerSkillsGui extends Container {
	
	private final EntityPlayer player;
	private final SkillsGuiInventory inventory;
	
	private int invIndex, hotbarIndex;
	
	public ContainerSkillsGui(EntityPlayer player, BendingType type) {
		this.player = player;
		
		inventory = new SkillsGuiInventory();
		
		addSlotToContainer(new Slot(inventory, 0, 100, 100) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				ScrollType scrollType = ScrollType.fromId(stack.getMetadata());
				Item item = stack.getItem();
				Slot other = getSlot(1);
				return item == AvatarItems.itemScroll && scrollType.accepts(type) && !other.getHasStack();
			}
		});
		// Second scroll slot
		addSlotToContainer(new Slot(inventory, 1, 100, 100) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				ScrollType scrollType = ScrollType.fromId(stack.getMetadata());
				Item item = stack.getItem();
				Slot other = getSlot(0);
				return item == AvatarItems.itemScroll && scrollType.accepts(type) && !other.getHasStack();
			}
		});
		
		// Main inventory
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 9; c++) {
				int id = c + r * 9 + 9;
				Slot slot = new Slot(player.inventory, id, 100, 100);
				addSlotToContainer(slot);
				if (r == 0 && c == 0) {
					invIndex = slot.slotNumber;
				}
			}
		}
		
		// Hotbar
		for (int i = 0; i < 9; i++) {
			Slot slot = new Slot(player.inventory, i, 100, 100);
			addSlotToContainer(slot);
			if (i == 0) {
				hotbarIndex = slot.slotNumber;
			}
		}
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		
		Slot slot = inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			ItemStack copy = stack.copy();
			
			if (index == 0) {
				if (!mergeItemStack(stack, 1, 37, true)) {
					return EMPTY;
				}
			} else {
				if (!mergeItemStack(stack, 0, 1, true)) {
					return EMPTY;
				}
			}
			
			return stack;
			
		}
		
		return EMPTY;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		ItemStack scroll = inventory.getStackInSlot(0);
		if (scroll != EMPTY) {
			player.dropItem(scroll, false);
			inventory.setInventorySlotContents(0, EMPTY);
		}
		
		ItemStack scroll2 = inventory.getStackInSlot(1);
		if (scroll2 != EMPTY) {
			player.dropItem(scroll2, false);
			inventory.setInventorySlotContents(1, EMPTY);
		}
		
	}
	
	public int getInvIndex() {
		return invIndex;
	}
	
	public int getHotbarIndex() {
		return hotbarIndex;
	}
	
}
