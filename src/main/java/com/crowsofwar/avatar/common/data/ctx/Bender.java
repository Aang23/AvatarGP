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

import com.crowsofwar.avatar.common.data.BendingData;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * A wrapper for any mob/player that can bend to provide greater abstraction
 * over useful methods.
 * 
 * @author CrowsOfWar
 */
public interface Bender {
	
	/**
	 * For players, returns the username. For mobs, returns the mob's name (e.g.
	 * Chicken).
	 */
	default String getName() {
		return getEntity().getName();
	}
	
	/**
	 * Return this bender in entity form
	 */
	EntityLivingBase getEntity();
	
	/**
	 * Get the world this entity is currently in
	 */
	default World getWorld() {
		return getEntity().world;
	}
	
	/**
	 * Returns whether this bender is a player
	 */
	default boolean isPlayer() {
		return getEntity() instanceof EntityPlayer;
	}
	
	default BenderInfo getInfo() {
		return new BenderInfo(this);
	}
	
	BendingData getData();
	
	boolean isCreativeMode();
	
	boolean isFlying();
	
	/**
	 * If any water pouches are in the inventory, checks if there is enough
	 * water. If there is, consumes the total amount of water in those pouches
	 * and returns true.
	 */
	boolean consumeWaterLevel(int amount);
	
	/**
	 * Creates an appropriate Bender instance for that entity
	 */
	public static Bender create(EntityLivingBase entity) {
		if (entity == null) {
			return null;
		} else if (entity instanceof Bender) {
			return (Bender) entity;
		} else if (entity instanceof EntityPlayer) {
			return new PlayerBender((EntityPlayer) entity);
		} else {
			throw new IllegalArgumentException("Unsure how to create bender for entity " + entity);
		}
	}
	
	public static BendingData getData(EntityLivingBase entity) {
		Bender bender = create(entity);
		return bender == null ? null : bender.getData();
	}
	
	public static boolean isBenderSupported(EntityLivingBase entity) {
		return entity == null || entity instanceof EntityPlayer || entity instanceof Bender;
	}
	
}
