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

import com.crowsofwar.avatar.common.data.AvatarPlayerData;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class FallAbsorptionHandler {
	
	private FallAbsorptionHandler() {}
	
	@SubscribeEvent
	public void onFall(LivingFallEvent e) {
		Entity entity = e.getEntity();
		if (entity instanceof EntityPlayer && !entity.world.isRemote) {
			EntityPlayer player = (EntityPlayer) entity;
			AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(player);
			if (data.getFallAbsorption() != 0) {
				e.setDistance(e.getDistance() - data.getFallAbsorption());
				if (e.getDistance() < 0) e.setDistance(0);
				data.setFallAbsorption(0);
			}
		}
	}
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new FallAbsorptionHandler());
	}
	
}
