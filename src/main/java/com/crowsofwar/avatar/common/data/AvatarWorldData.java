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

import java.util.ArrayList;
import java.util.List;

import com.crowsofwar.avatar.common.util.AvatarUtils;
import com.crowsofwar.gorecore.data.PlayerData;
import com.crowsofwar.gorecore.data.WorldDataPlayers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AvatarWorldData extends WorldDataPlayers<AvatarPlayerData> {
	
	public static final String WORLD_DATA_KEY = "Avatar";
	private int nextEntityId;
	
	private List<ScheduledDestroyBlock> scheduledDestroyBlocks;
	private List<TemporaryWaterLocation> temporaryWater;
	
	public AvatarWorldData() {
		super(WORLD_DATA_KEY);
		nextEntityId = 1;
		scheduledDestroyBlocks = new ArrayList<>();
		temporaryWater = new ArrayList<>();
	}
	
	public AvatarWorldData(String key) {
		this();
	}
	
	@Override
	public Class<? extends PlayerData> playerDataClass() {
		return AvatarPlayerData.class;
	}
	
	public static AvatarWorldData getDataFromWorld(World world) {
		return getDataForWorld(AvatarWorldData.class, WORLD_DATA_KEY, world, false);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		nextEntityId = nbt.getInteger("NextEntityId");
		
		AvatarUtils.readList(scheduledDestroyBlocks, compound -> {
			
			BlockPos pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"),
					compound.getInteger("z"));
			return new ScheduledDestroyBlock(this, pos, compound.getInteger("Ticks"),
					compound.getBoolean("Drop"), compound.getInteger("Fortune"));
			
		}, nbt, "DestroyBlocks");
		
		AvatarUtils.readList(temporaryWater, c -> {
			
			BlockPos pos = new BlockPos(c.getInteger("x"), c.getInteger("y"), c.getInteger("z"));
			return new TemporaryWaterLocation(this, pos, c.getInteger("Dimension"), c.getInteger("Ticks"));
			
		}, nbt, "TemporaryWater");
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("NextEntityId", nextEntityId);
		AvatarUtils.writeList(scheduledDestroyBlocks, (compound, sdb) -> {
			compound.setInteger("x", sdb.pos.getX());
			compound.setInteger("y", sdb.pos.getY());
			compound.setInteger("z", sdb.pos.getZ());
			compound.setInteger("Ticks", sdb.ticks);
			compound.setBoolean("Drop", sdb.drop);
			compound.setInteger("Fortune", sdb.fortune);
		}, nbt, "DestroyBlocks");
		AvatarUtils.writeList(temporaryWater, (c, water) -> {
			c.setInteger("x", water.getPos().getX());
			c.setInteger("y", water.getPos().getY());
			c.setInteger("z", water.getPos().getZ());
			c.setInteger("Ticks", water.getTicks());
			c.setInteger("Dimension", water.getDimension());
		}, nbt, "TemporaryWater");
		return nbt;
	}
	
	public int nextEntityId() {
		return ++nextEntityId;
	}
	
	public List<ScheduledDestroyBlock> getScheduledDestroyBlocks() {
		return scheduledDestroyBlocks;
	}
	
	public List<TemporaryWaterLocation> geTemporaryWaterLocations() {
		return temporaryWater;
	}
	
	public void addTemporaryWaterLocation(BlockPos pos) {
		temporaryWater.add(new TemporaryWaterLocation(this, pos, getWorld().provider.getDimension(), 15));
	}
	
}
