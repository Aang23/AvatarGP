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
package com.crowsofwar.avatar.common.entity.data;

import javax.annotation.Nullable;

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.AvatarLog.WarningType;
import com.crowsofwar.avatar.common.data.CachedEntity;
import com.crowsofwar.avatar.common.entity.AvatarEntity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;

/**
 * Like {@link CachedEntity}, but allows access to the server/client
 * counterparts of an AvatarEntity, on both sides.
 * <p>
 * Designed for use to have 2 entities having synced references to each other.
 * Uses DataManager to sync the entities' IDs and then performs lookup/caching.
 * <p>
 * NOTE: By default, if the entity is being loaded and the reference is null,
 * the SyncableEntityReference will setDead() the entity to prevent a NPE crash.
 * This can be disabled by calling {@link #allowNullSaving()}.
 * 
 * @author CrowsOfWar
 */
public class SyncableEntityReference<T extends AvatarEntity> {
	
	private final Entity using;
	private final DataParameter<Integer> sync;
	private final CachedEntity<T> cache;
	private boolean allowNullSaving;
	
	/**
	 * Create an entity reference.
	 * 
	 * @param entity
	 *            The entity that is USING the reference, usually
	 *            <code>this</code>. Not the entity being referenced
	 * @param sync
	 *            DataParameter used to sync. Should NOT be created specifically
	 *            for this SyncableEntityReference - use a constant. Will not
	 *            register to entity DataManager.
	 */
	public SyncableEntityReference(Entity entity, DataParameter<Integer> sync) {
		this.using = entity;
		this.sync = sync;
		this.cache = new CachedEntity<>(-1);
		this.allowNullSaving = false;
	}
	
	/**
	 * Enable saving a null reference. Normally, if the reference is null while
	 * being loaded, the entity is setDead() to try to prevent erroring entities
	 * from causing crashes.
	 */
	public void allowNullSaving() {
		allowNullSaving = true;
	}
	
	@Nullable
	public T getEntity() {
		// Cache may have an incorrect id; other side could have changed
		// dataManager id, but not the cached entity id.
		cache.setEntityId(using.getDataManager().get(sync));
		return cache.getEntity(using.world);
	}
	
	public void setEntity(@Nullable T entity) {
		cache.setEntity(entity);
		using.getDataManager().set(sync, cache.getEntityId());
	}
	
	/**
	 * Reads this reference from NBT. Please note, reads values directly from
	 * this compound (no sub-compound).
	 */
	public void readFromNBT(NBTTagCompound nbt) {
		cache.readFromNBT(nbt);
		using.getDataManager().set(sync, cache.getEntityId());
		if (!allowNullSaving && getEntity() == null) {
			using.setDead();
			AvatarLog.warn(WarningType.INVALID_SAVE,
					"Entity reference was null on load and removed entity for safety: " + using);
		}
	}
	
	/**
	 * Writes this reference from NBT. Please note, writes values directly from
	 * this compound (no sub-compound).
	 */
	public void writeToNBT(NBTTagCompound nbt) {
		cache.writeToNBT(nbt);
	}
	
}
