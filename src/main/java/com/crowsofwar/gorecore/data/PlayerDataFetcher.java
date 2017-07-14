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
package com.crowsofwar.gorecore.data;

import java.util.UUID;

import com.crowsofwar.gorecore.util.AccountUUIDs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Responsible for retrieving player data from an arbitrary storing mechanism.
 * Implementations will generally return the player data upon success and null
 * upon failure.
 * 
 * @param <T>
 *            Type of player data
 * 
 * @author CrowsOfWar
 */
public interface PlayerDataFetcher<T extends PlayerData> {
	
	/**
	 * Retrieves player data from that world and with the given Minecraft
	 * Account UUID.
	 * 
	 * @param world
	 *            World to fetch from
	 * @param accountId
	 *            UUID of the player
	 * 
	 * @see AccountUUIDs
	 */
	T fetch(World world, UUID accountId);
	
	/**
	 * Retrieves player data from that world and with the given player name.
	 * Internally looks up the account UUID of the player.
	 * 
	 * @param world
	 *            World to fetch from
	 * @param playerName
	 *            UUID of the player
	 */
	default T fetch(World world, String playerName) {
		if (world == null) throw new IllegalArgumentException("Cannot get player-data with null World");
		if (playerName == null)
			throw new IllegalArgumentException("Cannot get player-data with null player name");
		return fetch(world, AccountUUIDs.getId(playerName).getUUID());
	}
	
	/**
	 * Retrieves player data from that world and with the given player entity.
	 * Internally looks up the account UUID of the player.
	 * 
	 * @param world
	 *            World to fetch from
	 * @param playerName
	 *            UUID of the player
	 */
	default T fetch(EntityPlayer player) {
		if (player == null) throw new IllegalArgumentException("Cannot get Player-Data for null player");
		return fetch(player.world, player.getName());
	}
	
}
