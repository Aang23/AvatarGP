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

import static com.crowsofwar.avatar.common.item.ItemScroll.getScrollType;
import static net.minecraft.item.ItemStack.EMPTY;

import java.util.Arrays;
import java.util.List;

import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.item.AvatarItems;
import com.crowsofwar.avatar.common.item.ItemScroll;
import com.crowsofwar.avatar.common.item.ItemScroll.ScrollType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ContainerGetBending extends Container {
	
	private final GetBendingInventory inventory;
	
	private int invIndex, hotbarIndex;
	private float incompatibleMsgTicks;
	
	public ContainerGetBending(EntityPlayer player) {
		
		inventory = new GetBendingInventory();
		incompatibleMsgTicks = -1;
		
		addSlotToContainer(new ScrollSlot(inventory, 0, -18, -18));
		addSlotToContainer(new ScrollSlot(inventory, 1, -18, -18));
		addSlotToContainer(new ScrollSlot(inventory, 2, -18, -18));
		
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
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		
		Slot slot = inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			
			if (index >= 0 && index <= 2) {
				if (!mergeItemStack(stack, 1, 37, true)) {
					return EMPTY;
				}
			} else {
				if (!mergeItemStack(stack, 0, 3, true)) {
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
		
		if (!player.world.isRemote) {
			clearContainer(player, player.world, inventory);
		}
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	public int getSize() {
		return inventory.getSizeInventory();
	}
	
	public int getInvIndex() {
		return invIndex;
	}
	
	public int getHotbarIndex() {
		return hotbarIndex;
	}
	
	/**
	 * Returns the ticks left to display incompatible scrolls message, or -1 if
	 * no display
	 */
	public float getIncompatibleMsgTicks() {
		return incompatibleMsgTicks;
	}
	
	public void decrementIncompatibleMsgTicks(float amount) {
		incompatibleMsgTicks -= amount;
	}
	
	/**
	 * Returns the BendingTypes that can be unlocked by the scrolls which are
	 * currently in the slots.
	 */
	public List<BendingType> getEligibleTypes() {
		
		BendingType foundType = null;
		
		for (int i = 0; i <= 2; i++) {
			Slot slot = getSlot(i);
			
			// No possible unlocks if there aren't 3 scrolls
			if (!slot.getHasStack()) {
				return Arrays.asList();
			}
			
			// If the scroll isn't universal, then we found the scroll type used
			// Possible since all scroll stacks in the inventory must all be
			// compatible (or they couldn't be added)
			// Don't return here b/c didn't check if all slots aren't empty
			BendingType type = ItemScroll.getScrollType(slot.getStack()).getBendingType();
			if (type != null) {
				foundType = type;
			}
			
		}
		
		if (foundType == null) {
			// Didn't find scroll of a specific type
			// all universal scrolls
			return Arrays.asList(BendingType.allExceptError());
		} else {
			// Found scroll of specific type
			return Arrays.asList(foundType);
		}
		
	}
	
	private class ScrollSlot extends Slot {
		
		public ScrollSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean isItemValid(ItemStack stack) {
			if (stack.getItem() == AvatarItems.itemScroll) {
				
				ScrollType type1 = getScrollType(stack);
				
				for (int i = 0; i <= 2; i++) {
					ItemStack stack2 = getSlot(i).getStack();
					if (!stack2.isEmpty()) {
						ScrollType type2 = getScrollType(stack2);
						if (!type1.isCompatibleWith(type2)) {
							incompatibleMsgTicks = 100;
							return false;
						}
					}
				}
				
				return true;
				
			}
			
			return false;
		}
		
	}
	
}
