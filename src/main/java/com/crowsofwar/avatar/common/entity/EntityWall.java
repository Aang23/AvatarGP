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
package com.crowsofwar.avatar.common.entity;

import static com.crowsofwar.gorecore.util.GoreCoreNBTUtil.nestedCompound;
import static java.lang.Math.abs;
import static net.minecraft.util.EnumFacing.NORTH;

import com.crowsofwar.avatar.common.entity.data.SyncableEntityReference;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityWall extends AvatarEntity {
	
	private static final DataParameter<Integer> SYNC_DIRECTION = EntityDataManager.createKey(EntityWall.class,
			DataSerializers.VARINT);
	private static final DataParameter<Integer>[] SYNC_SEGMENTS;
	static {
		SYNC_SEGMENTS = new DataParameter[5];
		for (int i = 0; i < SYNC_SEGMENTS.length; i++) {
			SYNC_SEGMENTS[i] = EntityDataManager.createKey(EntityWall.class, DataSerializers.VARINT);
		}
	}
	
	/**
	 * All the segments in this wall. MUST be fixed-length, as the data
	 * parameters must be same for both sides.
	 */
	private final SyncableEntityReference<EntityWallSegment>[] segments;
	
	private int nextSegment = 0;
	
	/**
	 * @param world
	 */
	public EntityWall(World world) {
		super(world);
		this.segments = new SyncableEntityReference[5];
		for (int i = 0; i < segments.length; i++) {
			segments[i] = new SyncableEntityReference(this, SYNC_SEGMENTS[i]);
		}
		setSize(0, 0);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_DIRECTION, NORTH.ordinal());
		for (int i = 0; i < SYNC_SEGMENTS.length; i++) {
			dataManager.register(SYNC_SEGMENTS[i], -1);
		}
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		// Sync y-velocity with slowest moving wall segment
		double slowest = Integer.MAX_VALUE;
		for (SyncableEntityReference<EntityWallSegment> ref : segments) {
			EntityWallSegment seg = ref.getEntity();
			if (abs(seg.velocity().y()) < abs(slowest)) {
				slowest = seg.velocity().y();
			}
		}
		
		// Now sync all wall segment speeds
		for (SyncableEntityReference<EntityWallSegment> ref : segments) {
			ref.getEntity().velocity().setY(slowest);
		}
		
		// Lowest top pos of all the segments
		double lowest = Integer.MAX_VALUE;
		for (SyncableEntityReference<EntityWallSegment> ref : segments) {
			EntityWallSegment seg = ref.getEntity();
			double topPos = seg.position().y() + seg.height;
			if (topPos < lowest) {
				lowest = topPos;
			}
		}
		for (SyncableEntityReference<EntityWallSegment> ref : segments) {
			EntityWallSegment seg = ref.getEntity();
			seg.position().setY(lowest - seg.height);
		}
		
		velocity().set(Vector.ZERO);
		this.noClip = true;
		move(MoverType.SELF, 0, slowest / 20, 0);
	}
	
	/**
	 * To be used ONLY by {@link EntityWallSegment}
	 */
	void addSegment(EntityWallSegment segment) {
		segments[nextSegment].setEntity(segment);
		nextSegment++;
	}
	
	public EntityWallSegment getSegment(int i) {
		return segments[i].getEntity();
	}
	
	public EnumFacing getDirection() {
		return EnumFacing.values()[dataManager.get(SYNC_DIRECTION)];
	}
	
	public void setDirection(EnumFacing direction) {
		if (direction.getAxis().isVertical())
			throw new IllegalArgumentException("Cannot face up/down: " + direction);
		this.dataManager.set(SYNC_DIRECTION, direction.ordinal());
	}
	
	@Override
	public void setDead() {
		for (SyncableEntityReference<EntityWallSegment> ref : segments) {
			// don't use setDead() as that will trigger this being called again
			EntityWallSegment entity = ref.getEntity();
			if (entity != null) {
				// Avoid setDead() as that will call wall.setDead()
				entity.isDead = true;
				entity.dropBlocks();
			}
		}
		super.setDead();
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		for (int i = 0; i < segments.length; i++)
			segments[i].readFromNBT(nestedCompound(nbt, "Wall" + i));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		for (int i = 0; i < segments.length; i++)
			segments[i].writeToNBT(nestedCompound(nbt, "Wall" + i));
	}
	
	@Override
	protected boolean canCollideWith(Entity entity) {
		return super.canCollideWith(entity) && !(entity instanceof EntityWall)
				&& !(entity instanceof EntityWallSegment);
	}
	
	@Override
	public boolean tryDestroy() {
		return false;
	}
	
}
