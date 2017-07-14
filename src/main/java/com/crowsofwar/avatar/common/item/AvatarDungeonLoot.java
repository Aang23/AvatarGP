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

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;
import static net.minecraft.world.storage.loot.LootTableList.*;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryEmpty;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AvatarDungeonLoot {
	
	private AvatarDungeonLoot() {}
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new AvatarDungeonLoot());
	}
	
	@SubscribeEvent
	public void onLootLoad(LootTableLoadEvent e) {
		
		if (!STATS_CONFIG.addDungeonLoot) {
			return;
		}
		
		if (isLootTable(e, CHESTS_NETHER_BRIDGE, CHESTS_END_CITY_TREASURE, CHESTS_STRONGHOLD_CORRIDOR)) {
			addLoot(e, 50, //
					new LootItem(AvatarItems.itemBisonWhistle, 20),
					new LootItem(AvatarItems.itemBisonSaddle, 20).withMetadata(1),
					new LootItem(AvatarItems.itemBisonSaddle, 10).withMetadata(0),
					new LootItem(AvatarItems.itemBisonSaddle, 10).withMetadata(2),
					new LootItem(AvatarItems.itemBisonSaddle, 5).withMetadata(3),
					new LootItem(AvatarItems.itemWaterPouch, 30));
			addLoot(e, 120, //
					new LootItem(AvatarItems.itemScroll, 20), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(1), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(2), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(3), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(4));
		}
		
		if (isLootTable(e, CHESTS_STRONGHOLD_LIBRARY, CHESTS_ABANDONED_MINESHAFT, CHESTS_SIMPLE_DUNGEON)) {
			addLoot(e, 50, //
					new LootItem(AvatarItems.itemBisonArmor, 20).withMetadata(1),
					new LootItem(AvatarItems.itemBisonArmor, 10).withMetadata(0),
					new LootItem(AvatarItems.itemBisonArmor, 10).withMetadata(2),
					new LootItem(AvatarItems.itemBisonArmor, 5).withMetadata(3),
					new LootItem(AvatarItems.itemWaterPouch, 30));
			addLoot(e, 100, //
					new LootItem(AvatarItems.itemScroll, 20), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(1), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(2), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(3), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(4));
		}
		
		if (isLootTable(e, CHESTS_VILLAGE_BLACKSMITH, CHESTS_IGLOO_CHEST, CHESTS_DESERT_PYRAMID,
				CHESTS_JUNGLE_TEMPLE)) {
			addLoot(e, 20, //
					new LootItem(AvatarItems.itemScroll, 20), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(1), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(2), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(3), //
					new LootItem(AvatarItems.itemScroll, 5).withMetadata(4));
		}
		
		if (isLootTable(e, CHESTS_SPAWN_BONUS_CHEST)) {
			addLoot(e, 0, new LootItem(AvatarItems.itemScroll, 1));
			addLoot(e, 0, new LootItem(AvatarItems.itemScroll, 1));
			addLoot(e, 0, new LootItem(AvatarItems.itemScroll, 1));
		}
		
	}
	
	private boolean isLootTable(LootTableLoadEvent e, ResourceLocation... names) {
		for (ResourceLocation name : names) {
			if (e.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	private void addLoot(LootTableLoadEvent e, int emptyWeight, LootItem... items) {
		
		String lootPoolName = "custom_avatar_loot_pools";
		int j = 2;
		while (e.getTable().getPool(lootPoolName) != null) {
			lootPoolName = "custom_avatar_loot_pools_" + j;
			j++;
		}
		
		LootPool pool = new LootPool(new LootEntry[0], new LootCondition[0], new RandomValueRange(1, 1),
				new RandomValueRange(1, 1), lootPoolName);
		
		pool.addEntry(new LootEntryEmpty(emptyWeight, 1, new LootCondition[0], "empty"));
		
		for (int i = 0; i < items.length; i++) {
			LootItem item = items[i];
			
			LootCondition[] conditions = new LootCondition[0];
			LootFunction stackSize = new SetCount(conditions,
					new RandomValueRange(item.minStack, item.maxStack));
			LootFunction metadata = new SetMetadata(conditions, new RandomValueRange(item.metadata));
			LootFunction nbt = new SetNBT(conditions, item.nbt);
			
			pool.addEntry(new LootEntryItem(item.item, item.weight, 1,
					new LootFunction[] { stackSize, metadata, nbt }, conditions, "custom_" + i));
			
		}
		
		e.getTable().addPool(pool);
	}
	
	private static class LootItem {
		
		private final Item item;
		private final int weight;
		private int minStack, maxStack;
		private int metadata;
		private NBTTagCompound nbt;
		
		public LootItem(Item item, int weight) {
			this.item = item;
			this.weight = weight;
			this.metadata = 0;
			this.minStack = 1;
			this.maxStack = 1;
			this.nbt = new NBTTagCompound();
		}
		
		private LootItem withMetadata(int metadata) {
			this.metadata = metadata;
			return this;
		}
		
		private LootItem withStackSize(int min, int max) {
			this.minStack = min;
			this.maxStack = max;
			return this;
		}
		
		private LootItem withNbt(NBTTagCompound nbt) {
			this.nbt = nbt;
			return this;
		}
		
	}
	
}
