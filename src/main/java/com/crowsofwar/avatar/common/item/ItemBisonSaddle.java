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
package com.crowsofwar.avatar.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ItemBisonSaddle extends Item implements AvatarItem {
	
	public ItemBisonSaddle() {
		setUnlocalizedName("bison_saddle");
		setMaxStackSize(1);
		setCreativeTab(AvatarItems.tabItems);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@Override
	public Item item() {
		return this;
	}
	
	@Override
	public String getModelName(int meta) {
		SaddleTier tier = SaddleTier.fromId(meta);
		String tierName = tier == null ? "null" : tier.name().toLowerCase();
		return "bison_saddle_" + tierName;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		SaddleTier tier = SaddleTier.fromId(stack.getMetadata());
		String tierName = tier == null ? "null" : tier.name().toLowerCase();
		return super.getUnlocalizedName(stack) + "." + tierName;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		
		for (int i = 0; i <= 3; i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
		
	}
	
	public enum SaddleTier {
		
		BASIC(2, 1),
		STURDY(4, 2),
		STUDDED(6, 4),
		MAJESTIC(10, 6);
		
		private final float armorPoints;
		private final int maxPassengers;
		
		private SaddleTier(float armorPoints, int maxPassengers) {
			this.armorPoints = armorPoints;
			this.maxPassengers = maxPassengers;
		}
		
		public float getArmorPoints() {
			return armorPoints;
		}
		
		public int getMaxPassengers() {
			return maxPassengers;
		}
		
		public int id() {
			return ordinal();
		}
		
		/**
		 * Finds the tier with the given id
		 * 
		 * @throws IllegalArgumentException
		 *             when id is {@link #isValidId(int) invalid}
		 */
		public static SaddleTier fromId(int id) {
			if (!isValidId(id)) {
				throw new IllegalArgumentException("No SaddleTier for id " + id);
			}
			return values()[id];
		}
		
		public static boolean isValidId(int id) {
			return id >= 0 && id < values().length;
		}
		
	}
	
}
