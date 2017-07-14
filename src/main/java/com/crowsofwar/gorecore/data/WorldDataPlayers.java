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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.crowsofwar.gorecore.GoreCore;
import com.crowsofwar.gorecore.util.AccountUUIDs;
import com.crowsofwar.gorecore.util.GoreCoreNBTUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLLog;

/**
 * A world data class which comes equipped with the ability to save and load
 * player data.
 * 
 * @param T
 *            The type of your player data
 * 
 * @author CrowsOfWar
 */
public abstract class WorldDataPlayers<T extends PlayerData> extends WorldData {
	
	private Map<UUID, PlayerData> players;
	
	public WorldDataPlayers(String key) {
		super(key);
		this.players = new HashMap<UUID, PlayerData>();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.players = GoreCoreNBTUtil.readMapFromNBT(nbt, PlayerData.MAP_USER, "PlayerData", new Object[] {},
				new Object[] { playerDataClass(), this });
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		GoreCoreNBTUtil.writeMapToNBT(nbt, players, PlayerData.MAP_USER, "PlayerData");
		return nbt;
	}
	
	/**
	 * Gets the player data for that player, creating it if necessary.
	 * 
	 * @param player
	 *            The UUID of the player to get data for
	 * @return Player data for that player
	 */
	public T getPlayerData(UUID player) {
		if (players.containsKey(player)) {
			T data = getPlayerDataWithoutCreate(player);
			if (getWorld() != null)
				data.setPlayerEntity(AccountUUIDs.findEntityFromUUID(getWorld(), player));
			return data;
		} else {
			T data = createNewPlayerData(player);
			players.put(player, data);
			if (getWorld() != null)
				data.setPlayerEntity(AccountUUIDs.findEntityFromUUID(getWorld(), player));
			saveChanges();
			return data;
		}
	}
	
	/**
	 * Gets the player data for the player. If the player data has not been
	 * created, then this will return null.
	 * 
	 * @param player
	 *            The UUID of the player to get data for
	 * @return Player data for the player, or null if it does not exist
	 */
	public T getPlayerDataWithoutCreate(UUID player) {
		T data = (T) players.get(player);
		if (data != null && data.getPlayerEntity() == null) {
			data.setPlayerEntity(AccountUUIDs.findEntityFromUUID(getWorld(), player));
		}
		return data;
	}
	
	public abstract Class<? extends PlayerData> playerDataClass();
	
	private T createNewPlayerData(UUID player) {
		try {
			
			EntityPlayer playerEntity = AccountUUIDs.findEntityFromUUID(getWorld(), player);
			if (playerEntity == null)
				GoreCore.LOGGER.warn("WARNING: playerEntity was null while creating new player data");
			PlayerData data = playerDataClass()
					.getConstructor(DataSaver.class, UUID.class, EntityPlayer.class)
					.newInstance(this, player, playerEntity);
			return (T) data;
			
		} catch (Exception e) {
			FMLLog.warning("GoreCore> Found an error when trying to make new player data!");
			e.printStackTrace();
			return null;
		}
	}
	
}
