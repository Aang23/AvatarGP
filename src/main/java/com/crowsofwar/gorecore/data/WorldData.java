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

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.fml.common.FMLLog;

/**
 * A base class for WorldSavedData.
 * 
 * @author CrowsOfWar
 */
public abstract class WorldData extends WorldSavedData implements DataSaver {

	/**
	 * The world that this data belongs to.
	 * <p>
	 * If the world data was constructed because it was first loaded by vanilla,
	 * will still be null until
	 * {@link #getDataForWorld(Class, String, World, boolean)} is called.
	 */
	private World world;
	
	/**
	 * Data stored via the {@link DataSaver} methods. FIXME never saves...?
	 */
	private DataSaverNBT storedData;
	
	public WorldData(String key) {
		super(key);
		this.storedData = new DataSaverNBT();
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	/**
	 * Marks the world data dirty so that it will be saved to disk.
	 */
	@Override
	public void saveChanges() {
		markDirty();
	}
	
	/**
	 * Use to make an easy implementation of getDataForWorld:
	 * 
	 * <pre>
	 * public static MyWorldData getDataForWorld(World world) {
	 * 	return getDataForWorld(MyWorldData.class, "MyWorldData", world, true);
	 * }
	 * </pre>
	 * 
	 * @param worldDataClass
	 *            The class object of your world data
	 * @param key
	 *            The key to store the world data under
	 * @param world
	 *            The world to get world data for
	 * @param separatePerDimension
	 *            Whether world data is saved for each dimension or for all
	 *            dimensions
	 * @return World data, retrieved using the specified options
	 */
	protected static <T extends WorldData> T getDataForWorld(Class<T> worldDataClass, String key, World world,
			boolean separatePerDimension) {
		try {
			MapStorage ms = separatePerDimension ? world.getPerWorldStorage() : world.getMapStorage();
			T data = worldDataClass.cast(ms.getOrLoadData(worldDataClass, key));
			
			if (data == null) {
				// TODO [1.10] Not sure if this is actually called anymore- need
				// to check.
				data = worldDataClass.getConstructor(String.class).newInstance(key);
				data.setDirty(true);
				ms.setData(key, data);
			}
			
			data.setWorld(world);
			
			return data;
		} catch (Exception e) {
			FMLLog.bigWarning("GoreCore> Could not create World Data class!");
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public int getInt(String key) {
		return storedData.getInt(key);
	}
	
	@Override
	public void setInt(String key, int value) {
		storedData.setInt(key, value);
	}
	
	@Override
	public String getString(String key) {
		return storedData.getString(key);
	}
	
	@Override
	public void setString(String key, String value) {
		storedData.setString(key, value);
	}
	
	@Override
	public float getFloat(String key) {
		return storedData.getFloat(key);
	}
	
	@Override
	public void setFloat(String key, float value) {
		storedData.setFloat(key, value);
	}
	
	@Override
	public double getDouble(String key) {
		return storedData.getDouble(key);
	}
	
	@Override
	public void setDouble(String key, double value) {
		storedData.setDouble(key, value);
	}
	
	@Override
	public long getLong(String key) {
		return storedData.getLong(key);
	}
	
	@Override
	public void setLong(String key, long value) {
		storedData.setLong(key, value);
	}
	
}
