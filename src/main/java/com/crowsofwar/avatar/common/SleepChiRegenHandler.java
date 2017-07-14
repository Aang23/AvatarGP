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
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.Chi;
import com.crowsofwar.avatar.common.data.ctx.Bender;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class SleepChiRegenHandler {
	
	private SleepChiRegenHandler() {}
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new SleepChiRegenHandler());
	}
	
	@SubscribeEvent
	public void onSlept(PlayerWakeUpEvent e) {
		EntityPlayer player = e.getEntityPlayer();
		BendingData data = Bender.getData(player);
		Chi chi = data.chi();
		
		chi.setAvailableChi(chi.getMaxChi() * CHI_CONFIG.availableThreshold);
		chi.changeTotalChi(STATS_CONFIG.sleepChiRegen);
	}
	
}
