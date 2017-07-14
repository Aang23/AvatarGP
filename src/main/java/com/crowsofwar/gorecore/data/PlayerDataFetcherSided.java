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

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Player data fetcher class which delegates to a sided player data fetcher.
 * This is done by checking the current thread; works with integrated server.
 * 
 * @author CrowsOfWar
 */
public class PlayerDataFetcherSided<T extends PlayerData> implements PlayerDataFetcher<T> {
	
	private PlayerDataFetcher<T> clientDelegate, serverDelegate;
	
	public PlayerDataFetcherSided(PlayerDataFetcher<T> client, PlayerDataFetcher<T> server) {
		clientDelegate = client;
		serverDelegate = server;
	}
	
	@Override
	public T fetch(World world, UUID accountId) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.CLIENT)
			return clientDelegate.fetch(world, accountId);
		else
			return serverDelegate.fetch(world, accountId);
	}
	
}
