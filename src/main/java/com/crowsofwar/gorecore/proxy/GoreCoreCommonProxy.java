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

import java.io.File;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public class GoreCoreCommonProxy {
	
	private File uuidCacheFile;
	private File minecraftDirectory;
	
	public GoreCoreCommonProxy() {
		uuidCacheFile = createUUIDCacheFile();
		minecraftDirectory = createMinecraftDir();
	}
	
	public final File getUUIDCacheFile() {
		return uuidCacheFile;
	}
	
	protected File createUUIDCacheFile() {
		return new File("GoreCore_ServerUUIDCache.txt");
	}
	
	public final File getMinecraftDir() {
		return minecraftDirectory;
	}
	
	protected File createMinecraftDir() {
		return new File(".");
	}
	
	/**
	 * Returns whether that person is currently walking. This only works for the
	 * person who is playing Minecraft.
	 */
	public boolean isPlayerWalking(EntityPlayer player) {
		return false;
	}
	
	public void sideSpecifics() {
		
	}
	
	public String translate(String key, Object... args) {
		// TODO [1.10] find out way to translate server side
		return I18n.format(key, args);
	}
	
	public EntityPlayer getClientSidePlayer() {
		return null;
	}
	
	public String getKeybindingDisplayName(String name) {
		return null;
	}
	
}
