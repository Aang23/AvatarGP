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

import java.util.UUID;
import java.util.function.Consumer;

import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.data.ctx.BenderInfo;
import com.crowsofwar.avatar.common.data.ctx.NoBenderInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.World;

/**
 * Designed to use with an entity. Manages a synchronized owner property,
 * allowing retrieval and "storage" of a player entity.
 * 
 * @author CrowsOfWar
 */
public class OwnerAttribute {
	
	private final DataParameter<BenderInfo> sync;
	private final Entity entity;
	private final World world;
	private final Consumer<EntityLivingBase> setOwnerCallback;
	
	private EntityLivingBase ownerCached;
	
	/**
	 * Create a new owner attribute.
	 * 
	 * @param entity
	 *            Entity which has this attribute
	 * @param sync
	 *            Synchronization key. You don't have to register to entity's
	 *            data manager.
	 */
	public OwnerAttribute(Entity entity, DataParameter<BenderInfo> sync) {
		this(entity, sync, player -> {
		});
	}
	
	/**
	 * Create a new owner attribute.
	 * 
	 * @param entity
	 *            Entity which has this attribute
	 * @param sync
	 *            Synchronization key. You don't have to register to entity's
	 *            data manager.
	 * @param setOwnerCallback
	 *            Called when the owner has been changed.
	 */
	public OwnerAttribute(Entity entity, DataParameter<BenderInfo> sync,
			Consumer<EntityLivingBase> setOwnerCallback) {
		this.entity = entity;
		this.sync = sync;
		this.world = entity.world;
		this.setOwnerCallback = setOwnerCallback;
		this.entity.getDataManager().register(sync, new NoBenderInfo());
	}
	
	public void save(NBTTagCompound nbt) {
		getOwnerInfo().writeToNbt(nbt);
	}
	
	public void load(NBTTagCompound nbt) {
		setOwnerInfo(BenderInfo.readFromNbt(nbt));
		getOwner(); // Look up owner in world
	}
	
	public BenderInfo getOwnerInfo() {
		return entity.getDataManager().get(sync);
	}
	
	public void setOwnerInfo(BenderInfo info) {
		entity.getDataManager().set(sync, info);
	}
	
	/**
	 * Get owner. Null if player entity cannot be found. Only the owner's name
	 * is synced, so may be null on client but not server.
	 * <p>
	 * Detail: If the cached owner is null, but owner name is not, attempts to
	 * look for a player in the world with that name. Will then call
	 * {@link #setOwner(EntityPlayer)}.
	 */
	public EntityLivingBase getOwner() {
		
		if (isCacheInvalid()) {
			Bender bender = getOwnerInfo().find(world);
			if (bender != null) {
				ownerCached = bender.getEntity();
			}
		}
		
		return ownerCached;
	}
	
	/**
	 * Set the owner to the given player.
	 * <p>
	 * Also sets owner's BendingState FloatingBlock to this one.
	 * 
	 * @param owner
	 *            Owner to set to. Can set to null...
	 */
	public void setOwner(EntityLivingBase owner) {
		this.ownerCached = owner;
		setOwnerInfo(owner == null ? new NoBenderInfo() : new BenderInfo(owner));
		
		if (owner != null) {
			setOwnerCallback.accept(owner);
		}
		
	}
	
	/**
	 * Get the owner as a Bender. Null if owner not present.
	 */
	public Bender getOwnerBender() {
		EntityLivingBase owner = getOwner();
		if (owner == null) {
			return null;
		} else {
			return Bender.create(owner);
		}
	}
	
	public UUID getId() {
		return getOwnerInfo().getId();
	}
	
	/**
	 * Checks the cache's validity. If it is invalid, resets the cache and
	 * returns true.
	 * <p>
	 * Invalid conditions:
	 * <ul>
	 * <li>Cached owner is null
	 * <li>Cached owner is dead
	 * <li>Cached owner is not the correct owner
	 * <li>There is not supposed to be an owner
	 */
	private boolean isCacheInvalid() {
		
		UUID id = getOwnerInfo().getId();
		boolean idConsistent = id != null && getOwnerInfo().getId().equals(id);
		
		if (ownerCached == null || ownerCached.isDead || !idConsistent || getOwnerInfo() == null) {
			ownerCached = null;
			return true;
		}
		return false;
	}
	
}
