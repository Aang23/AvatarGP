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
package com.crowsofwar.avatar.common.data;

import javax.annotation.Nullable;

import com.crowsofwar.avatar.common.entity.AvatarEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Represents an AvatarEntity which is stored by ID but also cached for
 * performance.
 * <p>
 * Note: is not synced; designed to be manipulated by someone with a
 * synchronized ID.
 * 
 * @author CrowsOfWar
 */
public class CachedEntity<T extends AvatarEntity> {
	
	private T cachedEntity;
	private int entityId;
	
	public CachedEntity(int id) {
		this.entityId = id;
	}
	
	/**
	 * Reads this cached entity from NBT. Warning: Will use values directly from
	 * this compound, so make sure that a sub-compound is used specifically for
	 * this cached entity.
	 */
	public void readFromNBT(NBTTagCompound nbt) {
		entityId = nbt.getInteger("EntityId");
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("EntityId", entityId);
	}
	
	public void fromBytes(ByteBuf buf) {
		entityId = buf.readInt();
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityId);
	}
	
	public int getEntityId() {
		return entityId;
	}
	
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	
	/**
	 * Gets the entity, searching for it if necessary. Then returns the entity.
	 * <p>
	 * Null if entity cannot be found.
	 */
	public @Nullable T getEntity(World world) {
		if (checkCacheValidity() && entityId > -1) {
			cachedEntity = AvatarEntity.lookupEntity(world, entityId);
		}
		return cachedEntity;
	}
	
	/**
	 * Sets the entity Id and cache. Can be set to null.
	 */
	public void setEntity(@Nullable T entity) {
		cachedEntity = entity;
		entityId = entity == null ? -1 : entity.getAvId();
	}
	
	/**
	 * Checks whether the cached entity is invalid (null or dead). If so, sets
	 * to null and returns true. Else returns false.
	 * 
	 * @return whether cache is invalid; if true the cached entity is null
	 */
	private boolean checkCacheValidity() {
		if (entityId < 0 || cachedEntity == null || cachedEntity.isDead
				|| cachedEntity.getAvId() != entityId) {
			cachedEntity = null;
			return true;
		}
		
		return false;
	}
	
}
