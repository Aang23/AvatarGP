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

/**
 * An implementation of {@link DataSaver DataSaver} where data is not stored
 * anywhere.
 * 
 * @author CrowsOfWar
 */
public class DataSaverDontSave implements DataSaver {
	
	@Override
	public int getInt(String key) {
		return 0;
	}
	
	@Override
	public void setInt(String key, int value) {}
	
	@Override
	public String getString(String key) {
		return null;
	}
	
	@Override
	public void setString(String key, String value) {}
	
	@Override
	public float getFloat(String key) {
		return 0;
	}
	
	@Override
	public void setFloat(String key, float value) {}
	
	@Override
	public double getDouble(String key) {
		return 0;
	}
	
	@Override
	public void setDouble(String key, double value) {}
	
	@Override
	public long getLong(String key) {
		return 0;
	}
	
	@Override
	public void setLong(String key, long value) {}
	
	@Override
	public void saveChanges() {}
	
}
