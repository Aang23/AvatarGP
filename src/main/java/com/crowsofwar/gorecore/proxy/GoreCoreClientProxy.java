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
package com.crowsofwar.gorecore.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import java.io.File;

public class GoreCoreClientProxy extends GoreCoreCommonProxy {
	
	@Override
	protected File createUUIDCacheFile() {
		return new File(Minecraft.getMinecraft().mcDataDir, "GoreCore_ClientUUIDCache.txt");
	}
	
	@Override
	protected File createMinecraftDir() {
		return new File(Minecraft.getMinecraft().mcDataDir, ".");
	}
	
	@Override
	public boolean isPlayerWalking(EntityPlayer player) {
		if (player == Minecraft.getMinecraft().player) {
			GameSettings gs = Minecraft.getMinecraft().gameSettings;
			return gs.keyBindForward.isKeyDown() || gs.keyBindBack.isKeyDown() || gs.keyBindLeft.isKeyDown()
					|| gs.keyBindRight.isKeyDown();
		}
		
		return false;
	}
	
	@Override
	public void sideSpecifics() {}
	
	@Override
	public String translate(String key, Object... args) {
		return I18n.format(key, args);
	}
	
	@Override
	public EntityPlayer getClientSidePlayer() {
		return Minecraft.getMinecraft().player;
	}
	
	@Override
	public String getKeybindingDisplayName(String name) {
		
		KeyBinding[] allKeybindings = Minecraft.getMinecraft().gameSettings.keyBindings;
		for (KeyBinding kb : allKeybindings) {
			if (kb.getKeyDescription().equals(name)) {
				return kb.getDisplayName();
			}
		}
		
		return null;
	}
	
}
