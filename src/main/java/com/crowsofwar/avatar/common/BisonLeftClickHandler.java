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

import com.crowsofwar.avatar.common.entity.mob.EntitySkyBison;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class BisonLeftClickHandler {
	
	private BisonLeftClickHandler() {}
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new BisonLeftClickHandler());
	}
	
	@SubscribeEvent
	public void onLeftClickBison(LivingAttackEvent e) {
		Entity interacted = e.getEntity();
		DamageSource damage = e.getSource();
		if (damage.getTrueSource() instanceof EntityPlayer && interacted instanceof EntitySkyBison) {
			EntitySkyBison bison = (EntitySkyBison) interacted;
			EntityPlayer player = (EntityPlayer) damage.getTrueSource();
			if (bison.onLeftClick(player)) {
				e.setCanceled(true);
			}
		}
	}
	
}
