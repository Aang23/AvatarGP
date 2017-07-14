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

import static com.crowsofwar.avatar.common.bending.BendingAbility.ABILITY_WALL;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.EntityWallSegment;
import com.crowsofwar.avatar.common.util.Raytrace;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public abstract class WallBehavior extends Behavior<EntityWallSegment> {
	
	public static DataSerializer<WallBehavior> SERIALIZER = new Behavior.BehaviorSerializer<>();
	
	public static void register() {
		DataSerializers.registerSerializer(SERIALIZER);
		registerBehavior(Drop.class);
		registerBehavior(Rising.class);
		registerBehavior(Waiting.class);
	}
	
	public static class Drop extends WallBehavior {
		
		@Override
		public Behavior onUpdate(EntityWallSegment entity) {
			entity.velocity().add(0, -7.0 / 20, 0);
			if (entity.onGround) {
				entity.dropBlocks();
				entity.setDead();
			}
			return this;
		}
		
		@Override
		public void fromBytes(PacketBuffer buf) {}
		
		@Override
		public void toBytes(PacketBuffer buf) {}
		
		@Override
		public void load(NBTTagCompound nbt) {}
		
		@Override
		public void save(NBTTagCompound nbt) {}
		
	}
	
	public static class Rising extends WallBehavior {
		
		private int ticks = 0;
		
		@Override
		public Behavior onUpdate(EntityWallSegment entity) {
			
			if (entity.getWall() == null) {
				return this;
			}
			
			// not 0 since client missed 0th tick
			if (ticks == 1) {
				
				int maxHeight = 0;
				for (int i = 0; i < 5; i++) {
					EntityWallSegment seg = entity.getWall().getSegment(i);
					if (seg.height > maxHeight) maxHeight = (int) seg.height;
				}
				
				entity.velocity().set(0, STATS_CONFIG.wallMomentum / 5 * maxHeight, 0);
				
			} else {
				entity.velocity().setY(entity.velocity().y() * 0.9);
			}
			
			// For some reason, the same entity instance is on server/client,
			// but has different world reference when this is called...?
			if (!entity.world.isRemote) ticks++;
			
			return ticks > 5 && entity.velocity().y() <= 0.2 ? new Waiting() : this;
		}
		
		@Override
		public void fromBytes(PacketBuffer buf) {}
		
		@Override
		public void toBytes(PacketBuffer buf) {}
		
		@Override
		public void load(NBTTagCompound nbt) {}
		
		@Override
		public void save(NBTTagCompound nbt) {}
		
	}
	
	public static class Waiting extends WallBehavior {
		
		private int ticks = 0;
		
		@Override
		public Behavior onUpdate(EntityWallSegment entity) {
			entity.velocity().set(0, 0, 0);
			ticks++;
			
			boolean drop = ticks >= STATS_CONFIG.wallWaitTime * 20;
			
			BendingData data = Bender.getData(entity.getOwner());
			AbilityData abilityData = data.getAbilityData(ABILITY_WALL);
			if (abilityData.isMaxLevel() && abilityData.getPath() == AbilityTreePath.SECOND) {
				
				drop = entity.getOwner().isDead || ticks >= STATS_CONFIG.wallWaitTime2 * 20;
				
				BendingContext ctx = new BendingContext(data, entity.getOwner(),
						Bender.create(entity.getOwner()), new Raytrace.Result());
				
				if (!entity.world.isRemote && !ctx.consumeChi(STATS_CONFIG.chiWallOneSecond / 20)) {
					drop = true;
				}
				
			}
			
			return drop ? new Drop() : this;
		}
		
		@Override
		public void fromBytes(PacketBuffer buf) {}
		
		@Override
		public void toBytes(PacketBuffer buf) {}
		
		@Override
		public void load(NBTTagCompound nbt) {}
		
		@Override
		public void save(NBTTagCompound nbt) {}
		
	}
	
}
