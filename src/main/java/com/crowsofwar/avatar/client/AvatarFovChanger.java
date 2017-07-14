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
package com.crowsofwar.avatar.client;

import com.crowsofwar.avatar.common.item.AvatarItems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AvatarFovChanger {
	
	private AvatarFovChanger() {}
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new AvatarFovChanger());
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onUpdateFOV(FOVUpdateEvent event) {
		
		EntityPlayer entity = event.getEntity();
		
		ItemStack activeStack = entity.getActiveItemStack();
		
		if (entity.isHandActive() && activeStack.getItem() == AvatarItems.itemBisonWhistle) {
			
			float progress = activeStack.getItem().getMaxItemUseDuration(activeStack)
					- entity.getItemInUseCount();
			float neededProgress = 20;
			
			float percent = progress / neededProgress;
			if (percent > 1) percent = 1;
			float inversePercent = 1 - percent;
			
			float fov = event.getFov();
			fov *= 1 - (1 - inversePercent * inversePercent) * 0.3F;
			event.setNewfov(fov);
			
		}
		
	}
	
}
