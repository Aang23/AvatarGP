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

import com.crowsofwar.avatar.AvatarMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AvatarItems {
	
	public static CreativeTabs tabItems = new CreativeTabs("avatar.items") {
		@Override
		public ItemStack getTabIconItem() {
			return stackScroll;
		}
	};

	public static List<Item> allItems;
	public static ItemScroll itemScroll;
	public static ItemWaterPouch itemWaterPouch;
	public static ItemBisonWhistle itemBisonWhistle;
	public static ItemBisonSaddle itemBisonSaddle;
	public static ItemBisonArmor itemBisonArmor;
	
	private static ItemStack stackScroll;

	private AvatarItems() {}
	
	public static void init() {
		allItems = new ArrayList<>();
		addItem(itemScroll = new ItemScroll());
		addItem(itemWaterPouch = new ItemWaterPouch());
		addItem(itemBisonWhistle = new ItemBisonWhistle());
		addItem(itemBisonArmor = new ItemBisonArmor());
		addItem(itemBisonSaddle = new ItemBisonSaddle());
		
		stackScroll = new ItemStack(itemScroll);
		MinecraftForge.EVENT_BUS.register(new AvatarItems());

	}

	private static void addItem(Item item) {
		item.setRegistryName("avatarmod", item.getUnlocalizedName().substring(5));
		item.setUnlocalizedName(item.getRegistryName().toString());
        allItems.add(item);
    }

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> e) {
		Item[] itemsArr = allItems.toArray(new Item[allItems.size()]);
		e.getRegistry().registerAll(itemsArr);
		AvatarMod.proxy.registerItemModels();
	}

}
