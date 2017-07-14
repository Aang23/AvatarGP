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

import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.common.controls.IControlsHandler;
import com.crowsofwar.avatar.common.controls.KeybindingWrapper;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.gui.AvatarGui;
import com.crowsofwar.avatar.common.network.IPacketHandler;
import com.crowsofwar.gorecore.data.PlayerDataFetcher;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;

/**
 * Allows calling of side-specific code by using a common base class and
 * side-specific subclasses. It can be referenced via {@link AvatarMod#proxy}.
 * All classes or values accessed from here are safe to use on either side.
 * <br />
 * <br />
 * Is using Client proxy if running from a minecraft client. Uses server proxy
 * is running from server. <br />
 * <br />
 *
 */
public interface AvatarCommonProxy {
	
	/**
	 * Called from the main class, subclasses should initialize themselves here
	 * (fields, etc).
	 */
	public void preInit();
	
	public IControlsHandler getKeyHandler();
	
	/**
	 * Get a client-side packet handler safely. When the machine is running a
	 * minecraft client (even if in the integrated server thread), returns the
	 * packet handler for the client. Otherwise (this only happens on dedicated
	 * servers), returns null.
	 */
	public IPacketHandler getClientPacketHandler();
	
	/**
	 * Get client player's reach. Returns 0 on server.
	 */
	double getPlayerReach();
	
	/**
	 * Called during the FMLInitialization event
	 */
	void init();
	
	AvatarGui createClientGui(int id, EntityPlayer player, World world, int x, int y, int z);
	
	PlayerDataFetcher<AvatarPlayerData> getClientDataFetcher();
	
	/**
	 * Get client-side IThreadListener, null on server
	 * 
	 * @return
	 */
	IThreadListener getClientThreadListener();
	
	/**
	 * Get amount of particles. 0 = All, 1 = decreased, 2 = minimal
	 */
	int getParticleAmount();
	
	/**
	 * Creates a wrapper so that the keybinding can be used on both sides
	 * (KeyBinding is client SideOnly)
	 * <p>
	 * Looks up keybinding by name
	 */
	KeybindingWrapper createKeybindWrapper(String keybindName);

	/**
	 * Register the item models so they can be configured to use the correct textures
	 */
	public void registerItemModels();

}
