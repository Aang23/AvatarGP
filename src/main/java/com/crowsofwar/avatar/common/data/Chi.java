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

import static com.crowsofwar.gorecore.util.GoreCoreNBTUtil.nestedCompound;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Represents a bender's energy to use abilities. Chi is required to execute an
 * ability and also will regenerate over time.
 * <p>
 * Chi is somewhat simple; it is a bar with a current and maximum amount.
 * However, only a certain portion of the bar is usable at one time. This is
 * referred to as {@link #getAvailableChi() available chi}. The other chi can't
 * be used, until the available mark increases.
 * 
 * @author CrowsOfWar
 */
public class Chi {
	
	private final BendingData data;
	
	// These fields are not for modification directly; use getters/setters
	private float max;
	private float total;
	private float availableMark;
	
	public Chi(BendingData data) {
		this.data = data;
		
		// Default values for testing
		this.max = 20;
		this.total = 10;
		this.availableMark = 8;
		
	}
	
	/**
	 * Gets the current amount of chi. Some may not be usable.
	 * 
	 * @see #setTotalChi(float)
	 */
	public float getTotalChi() {
		return total;
	}
	
	/**
	 * Sets the current amount of chi. Some may not be usable.
	 * 
	 * @see #getTotalChi()
	 */
	public void setTotalChi(float total) {
		this.total = total;
		save();
	}
	
	/**
	 * Adds the given amount of chi. The available chi is not affected. Accepts
	 * negative amounts (subtraction).
	 */
	public void changeTotalChi(float amount) {
		float prev = total;
		if (total + amount > max) {
			total = max;
		} else if (total + amount < 0) {
			total = 0;
		} else {
			total += amount;
		}
		availableMark += total - prev;
		save();
	}
	
	/**
	 * Gets the maximum amount of chi possible. However, not all of this chi
	 * would be usable at one time
	 * 
	 * @see #setMaxChi(float)
	 */
	public float getMaxChi() {
		return max;
	}
	
	/**
	 * Sets the maximum amount of chi possible. However, not all of this chi
	 * would be usable at one time
	 * 
	 * @see #getMaxChi()
	 */
	public void setMaxChi(float max) {
		this.max = max;
		if (max < total) setTotalChi(max);
		save();
	}
	
	/**
	 * Gets the current available amount of chi.
	 * 
	 * @see #setAvailableChi(float)
	 */
	public float getAvailableChi() {
		return total - availableMark;
	}
	
	/**
	 * Moves the available chi mark so the amount of available chi is now at the
	 * requested value.
	 * 
	 * @see #getAvailableChi()
	 */
	public void setAvailableChi(float available) {
		if (available > total) available = total;
		this.availableMark = total - available;
		save();
	}
	
	/**
	 * Adds the given amount of available chi. The total chi is not affected
	 * (just moves available chi mark).
	 */
	public void changeAvailableChi(float amount) {
		setAvailableChi(getAvailableChi() + amount);
	}
	
	/**
	 * Gets the maximum amount of available chi, at this available mark
	 */
	public float getAvailableMaxChi() {
		return max - availableMark;
	}
	
	/**
	 * Tries to consume the amount of available chi; returns whether there was
	 * enough.
	 */
	public boolean consumeChi(float amount) {
		float available = getAvailableChi();
		if (available >= amount) {
			changeTotalChi(-amount);
			changeAvailableChi(-amount);
			return true;
		}
		return false;
	}
	
	private void save() {
		checkConsistency();
		data.save(DataCategory.CHI);
	}
	
	/**
	 * Ensures that variables do not conflict with each other or otherwise are
	 * invalid
	 */
	private void checkConsistency() {
		if (total < 0) total = 0;
		if (total > max) total = max;
		
		if (availableMark > total) availableMark = total;
		if (availableMark < 0) availableMark = 0;
	}
	
	/**
	 * Reads the chi information to NBT. This creates a subcompound in the
	 * parameter
	 */
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = nestedCompound(compound, "ChiData");
		this.max = nbt.getFloat("Max");
		this.total = nbt.getFloat("Current");
		this.availableMark = nbt.getFloat("AvailableMark");
		if (max == 0) {
			max = 20;
			total = 10;
			availableMark = 8;
		}
	}
	
	/**
	 * Writes the chi information to NBT. This creates a subcompound in the
	 * parameter
	 */
	public void writeToNBT(NBTTagCompound compound) {
		NBTTagCompound nbt = nestedCompound(compound, "ChiData");
		nbt.setFloat("Max", max);
		nbt.setFloat("Current", total);
		nbt.setFloat("AvailableMark", availableMark);
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(max);
		buf.writeFloat(total);
		buf.writeFloat(availableMark);
	}
	
	public void fromBytes(ByteBuf buf) {
		max = buf.readFloat();
		total = buf.readFloat();
		availableMark = buf.readFloat();
	}
	
}
