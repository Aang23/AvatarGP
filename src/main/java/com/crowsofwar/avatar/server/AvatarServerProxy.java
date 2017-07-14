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
package com.crowsofwar.avatar.server;

import com.crowsofwar.avatar.common.AvatarCommonProxy;
import com.crowsofwar.avatar.common.controls.IControlsHandler;
import com.crowsofwar.avatar.common.controls.KeybindingWrapper;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.gui.AvatarGui;
import com.crowsofwar.avatar.common.network.IPacketHandler;
import com.crowsofwar.gorecore.data.PlayerDataFetcher;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;

public class AvatarServerProxy implements AvatarCommonProxy {
	
	private AvatarKeybindingServer keys;
	
	@Override
	public void preInit() {
		keys = new AvatarKeybindingServer();
	}
	
	@Override
	public IControlsHandler getKeyHandler() {
		return keys;
	}
	
	@Override
	public IPacketHandler getClientPacketHandler() {
		return null;
	}
	
	@Override
	public double getPlayerReach() {
		return 0;
	}
	
	@Override
	public void init() {
		
	}
	
	@Override
	public AvatarGui createClientGui(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
	
	@Override
	public PlayerDataFetcher<AvatarPlayerData> getClientDataFetcher() {
		return null;
	}
	
	@Override
	public IThreadListener getClientThreadListener() {
		return null;
	}
	
	@Override
	public int getParticleAmount() {
		return 0;
	}
	
	@Override
	public KeybindingWrapper createKeybindWrapper(String keybindName) {
		return new KeybindingWrapper();
	}

	@Override
	public void registerItemModels() {}

}
