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

import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.common.entity.mob.EntitySkyBison;
import com.crowsofwar.avatar.common.network.packets.PacketSBisonInventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

/**
 * Causes the player to open the sky bison inventory instead of their own while
 * riding a bison.
 * 
 * @author CrowsOfWar
 */
public class AvatarInventoryOverride {
	
	private AvatarInventoryOverride() {}
	
	@SubscribeEvent
	public void onInventoryOpen(KeyInputEvent e) {
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		KeyBinding keybind = mc.gameSettings.keyBindInventory;
		
		// don't use isPressed() as that marks it as "not pressed" if it was
		if (keybind.isKeyDown()) {
			
			if (player.getRidingEntity() instanceof EntitySkyBison) {
				EntitySkyBison bison = (EntitySkyBison) player.getRidingEntity();
				if (bison.canPlayerViewInventory(player)) {
					
					AvatarMod.network.sendToServer(new PacketSBisonInventory());
					// mark key as not pressed to avoid vanilla behavior
					keybind.isPressed();
					
				}
			}
			
		}
	}
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new AvatarInventoryOverride());
	}
	
}
