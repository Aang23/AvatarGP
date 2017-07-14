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

import static com.crowsofwar.avatar.common.config.ConfigChi.CHI_CONFIG;

import java.util.List;

import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.data.Chi;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.util.Raytrace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class AvatarPlayerTick {
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		// Also forces loading of data on client
		AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(e.player);
		if (data != null) {
			
			EntityPlayer player = e.player;
			
			if (!player.world.isRemote && player.ticksExisted == 0) {
				data.saveAll();
			}
			
			data.decrementCooldown();
			if (!player.world.isRemote) {
				Chi chi = data.chi();
				chi.changeTotalChi(CHI_CONFIG.regenPerSecond / 20f);
				
				if (chi.getAvailableChi() < chi.getMaxChi() * CHI_CONFIG.availableThreshold) {
					chi.changeAvailableChi(CHI_CONFIG.availablePerSecond / 20f);
				}
				
			}
			
			if (e.phase == Phase.START) {
				List<TickHandler> tickHandlers = data.getAllTickHandlers();
				if (tickHandlers != null) {
					BendingContext ctx = new BendingContext(data, new Raytrace.Result());
					for (TickHandler handler : tickHandlers) {
						if (handler.tick(ctx)) {
							// Can use this since the list is a COPY of the
							// underlying list
							data.removeTickHandler(handler);
						}
					}
				}
			}
			
		}
		
	}
	
}
