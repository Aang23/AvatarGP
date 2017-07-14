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
package com.crowsofwar.avatar.common.data.ctx;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.crowsofwar.gorecore.util.AccountUUIDs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Stores information about a {@link Bender} so that he/she can be found later.
 * 
 * @author CrowsOfWar
 */
public class BenderInfo {
	
	private final boolean player;
	private final UUID id;
	
	/**
	 * Creates bender info with null info, should only be used by NoBenderInfo
	 */
	protected BenderInfo() {
		player = false;
		id = null;
	}
	
	public BenderInfo(EntityLivingBase entity) {
		this(Bender.create(entity));
	}
	
	public BenderInfo(Bender bender) {
		player = bender.isPlayer();
		if (player) {
			id = AccountUUIDs.getId(bender.getName()).getUUID();
		} else {
			id = bender.getEntity().getPersistentID();
		}
	}
	
	public BenderInfo(boolean player, UUID id) {
		this.player = player;
		this.id = id;
	}
	
	public boolean isPlayer() {
		return player;
	}
	
	@Nullable
	public UUID getId() {
		return id;
	}
	
	public Bender find(World world) {
		if (id == null) return null;
		
		if (player) {
			return new PlayerBender(AccountUUIDs.findEntityFromUUID(world, id));
		} else {
			List<Entity> entities = world.loadedEntityList;
			for (Entity entity : entities) {
				if (entity.getPersistentID().equals(id)) {
					return (Bender) entity;
				}
			}
			return null;
		}
	}
	
	/**
	 * Writes to the NBT tag. Values are written directly onto the NBT.
	 */
	public void writeToNbt(NBTTagCompound nbt) {
		nbt.setBoolean("Player", player);
		nbt.setUniqueId("Id", id == null ? new UUID(0, 0) : id);
	}
	
	public static BenderInfo readFromNbt(NBTTagCompound nbt) {
		UUID id = nbt.getUniqueId("Id");
		id = id.getLeastSignificantBits() == 0 && id.getMostSignificantBits() == 0 ? null : id;
		return new BenderInfo(nbt.getBoolean("Player"), id);
	}
	
}
