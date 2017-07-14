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

import net.minecraft.nbt.NBTTagCompound;

/**
 * Wraps NBTTagCompounds to be usable with {@link DataSaver}.
 * 
 * @author CrowsOfWar
 */
public class DataSaverNBT implements DataSaver {
	
	private NBTTagCompound nbt;
	
	/**
	 * Creates a new NBTTagCompound DataSaver wrapper using a new
	 * NBTTagCompound.
	 */
	public DataSaverNBT() {
		this(new NBTTagCompound());
	}
	
	/**
	 * Creates a new NBTTagCompound DataSaver wrapper using the specified
	 * NBTTagCompound.
	 */
	public DataSaverNBT(NBTTagCompound nbt) {
		this.nbt = nbt;
	}
	
	@Override
	public int getInt(String key) {
		return nbt.getInteger(key);
	}
	
	@Override
	public void setInt(String key, int value) {
		nbt.setInteger(key, value);
	}
	
	@Override
	public String getString(String key) {
		return nbt.getString(key);
	}
	
	@Override
	public void setString(String key, String value) {
		nbt.setString(key, value);
	}
	
	@Override
	public float getFloat(String key) {
		return nbt.getFloat(key);
	}
	
	@Override
	public void setFloat(String key, float value) {
		nbt.setFloat(key, value);
	}
	
	@Override
	public double getDouble(String key) {
		return nbt.getDouble(key);
	}
	
	@Override
	public void setDouble(String key, double value) {
		nbt.setDouble(key, value);
	}
	
	@Override
	public long getLong(String key) {
		return nbt.getLong(key);
	}
	
	@Override
	public void setLong(String key, long value) {
		nbt.setLong(key, value);
	}
	
	@Override
	public void saveChanges() {}
	
}
