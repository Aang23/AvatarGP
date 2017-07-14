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
import java.util.function.Function;

import com.crowsofwar.gorecore.util.AccountUUIDs;

import net.minecraft.world.World;

/**
 * Manages player data fetching on the server instance.
 * <p>
 * This is done through world data, which is responsible for actually storing,
 * saving, and instantiating data.
 * 
 * @param <T>
 * 
 * @author CrowsOfWar
 */
public class PlayerDataFetcherServer<T extends PlayerData> implements PlayerDataFetcher<T> {
	
	private final Function<World, WorldDataPlayers<T>> worldDataFetcher;
	
	public PlayerDataFetcherServer(Function<World, WorldDataPlayers<T>> worldDataFetcher) {
		this.worldDataFetcher = worldDataFetcher;
	}
	
	@Override
	public T fetch(World world, UUID accountId) {
		
		if (world == null) throw new IllegalArgumentException("Cannot get client player data for null world");
		if (accountId == null)
			throw new IllegalArgumentException("Cannot get client player data for null player ID");
		
		T data = worldDataFetcher.apply(world).getPlayerData(accountId);
		data.setPlayerEntity(AccountUUIDs.findEntityFromUUID(world, accountId));
		return data;
	}
	
}
