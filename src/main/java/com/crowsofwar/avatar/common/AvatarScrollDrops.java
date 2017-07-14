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
package com.crowsofwar.avatar.common;

import static com.crowsofwar.avatar.common.config.ConfigMobs.MOBS_CONFIG;

import com.crowsofwar.avatar.common.item.AvatarItems;
import com.crowsofwar.avatar.common.item.ItemScroll;
import com.crowsofwar.avatar.common.item.ItemScroll.ScrollType;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AvatarScrollDrops {
	
	private AvatarScrollDrops() {}
	
	@SubscribeEvent
	public void onMobDeath(LivingDropsEvent e) {
		
		EntityLivingBase entity = e.getEntityLiving();
		DamageSource source = e.getSource();
		
		if (e.isRecentlyHit()) {
			
			double chance = MOBS_CONFIG.getScrollDropChance(entity);
			ScrollType type = MOBS_CONFIG.getScrollType(entity);
			
			double random = Math.random() * 100;
			if (random < chance) {
				
				ItemStack stack = new ItemStack(AvatarItems.itemScroll);
				ItemScroll.setScrollType(stack, type);
				
				EntityItem entityItem = new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ,
						stack);
				entityItem.setDefaultPickupDelay();
				e.getDrops().add(entityItem);
				
			}
			
		}
		
	}
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new AvatarScrollDrops());
	}
	
}
