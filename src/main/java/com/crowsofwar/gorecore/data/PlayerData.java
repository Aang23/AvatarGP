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

import com.crowsofwar.gorecore.util.GoreCoreNBTInterfaces;
import com.crowsofwar.gorecore.util.GoreCoreNBTInterfaces.MapUser;
import com.crowsofwar.gorecore.util.GoreCoreNBTUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

public abstract class PlayerData implements GoreCoreNBTInterfaces.ReadableWritable {
	
	public static final MapUser<UUID, PlayerData> MAP_USER = new MapUser<UUID, PlayerData>() {
		@Override
		public UUID createK(NBTTagCompound nbt, Object[] constructArgsK) {
			return GoreCoreNBTUtil.readUUIDFromNBT(nbt, "KeyUUID");
		}
		
		@Override
		public PlayerData createV(NBTTagCompound nbt, UUID key, Object[] constructArgsV) {
			try {
				PlayerData data = ((Class<? extends PlayerData>) constructArgsV[0])
						.getConstructor(DataSaver.class, UUID.class, EntityPlayer.class)
						.newInstance(constructArgsV[1], key, null);
				data.readFromNBT(nbt);
				return data;
			} catch (Exception e) {
				FMLLog.severe("GoreCore> An error occured while creating new player data!");
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		public void writeK(NBTTagCompound nbt, UUID obj) {
			GoreCoreNBTUtil.writeUUIDToNBT(nbt, "KeyUUID", obj);
		}
		
		@Override
		public void writeV(NBTTagCompound nbt, PlayerData obj) {
			obj.writeToNBT(nbt);
		}
	};
	
	protected UUID playerID;
	protected DataSaver dataSaver;
	/**
	 * The player entity this player-data is attached to.
	 * <p>
	 * May be null on server.
	 * <p>
	 * Is not null {@link #shouldBeDecached() by default} on client.
	 */
	private EntityPlayer playerEntity;
	
	/**
	 * Creates new GC player data.
	 * <p>
	 * Please note, the subclass must create a constructor identical to this
	 * one, passing the arguments to <code>super</code>. This is because
	 * GoreCore player data is created with reflection.
	 * 
	 * @param dataSaver
	 *            Where data is saved to
	 * @param playerID
	 *            The account UUID of the player this player-data is for
	 * @param playerEntity
	 *            The player entity. May be null.
	 */
	public PlayerData(DataSaver dataSaver, UUID playerID, EntityPlayer playerEntity) {
		construct(dataSaver, playerID, playerEntity);
	}
	
	/**
	 * Called from constructor to initialize data. Override to change
	 * constructor.
	 */
	protected void construct(DataSaver dataSaver, UUID playerID, EntityPlayer playerEntity) {
		if (dataSaver == null)
			FMLLog.severe("GoreCore> Player data was created with a null dataSaver - this is a bug! Debug:");
		if (playerID == null)
			FMLLog.severe("GoreCore> Player data was created with a null playerID - this is a bug! Debug:");
		if (dataSaver == null || playerID == null) Thread.dumpStack();
		
		this.dataSaver = dataSaver;
		this.playerID = playerID;
		this.playerEntity = playerEntity;
	}
	
	protected void saveChanges() {
		dataSaver.saveChanges();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.playerID = GoreCoreNBTUtil.readUUIDFromNBT(nbt, "PlayerID");
		readPlayerDataFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		GoreCoreNBTUtil.writeUUIDToNBT(nbt, "PlayerID", playerID);
		writePlayerDataToNBT(nbt);
	}
	
	protected abstract void readPlayerDataFromNBT(NBTTagCompound nbt);
	
	protected abstract void writePlayerDataToNBT(NBTTagCompound nbt);
	
	public UUID getPlayerID() {
		return playerID;
	}
	
	/**
	 * Returns whether this player data should be de-cached on a client-side
	 * Player Data Cache.
	 * <p>
	 * This is only used by {@link PlayerDataFetcherClient}.
	 * <p>
	 * By default, returns true if the player entity is dead (unloaded).
	 */
	public boolean shouldBeDecached() {
		return playerEntity.isDead;
	}
	
	/**
	 * The player entity this player-data is attached to.
	 * <p>
	 * May be null on server.
	 * <p>
	 * Is not null {@link #shouldBeDecached() by default} on client.
	 */
	public EntityPlayer getPlayerEntity() {
		return playerEntity;
	}
	
	public void setPlayerEntity(EntityPlayer player) {
		this.playerEntity = player;
	}
	
	/**
	 * Get the world this player data is in.
	 * <p>
	 * Uses {@link #getPlayerEntity()} for the world. It may be null on servers.
	 */
	public World getWorld() {
		return getPlayerEntity() == null ? null : getPlayerEntity().getEntityWorld();
	}
	
}
