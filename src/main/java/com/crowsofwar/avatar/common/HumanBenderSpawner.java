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
package com.crowsofwar.avatar.common;

import com.crowsofwar.avatar.common.entity.mob.EntityAirbender;
import com.crowsofwar.avatar.common.entity.mob.EntityFirebender;
import com.crowsofwar.avatar.common.entity.mob.EntityHumanBender;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class HumanBenderSpawner {
	
	private HumanBenderSpawner() {}
	
	public static void register() {
		MinecraftForge.TERRAIN_GEN_BUS.register(new HumanBenderSpawner());
	}
	
	@SubscribeEvent
	public void modifyVillageSpawner(InitMapGenEvent e) {
		
		if (e.getType() == EventType.VILLAGE) {
			// TODO See if this messes up superflat world options
			e.setNewGen(new MapGenVillageWithHumanbenders());
		}
		
	}
	
	private static class MapGenVillageWithHumanbenders extends MapGenVillage {
		
		public MapGenVillageWithHumanbenders() {
			super();
		}
		
		public MapGenVillageWithHumanbenders(Map<String, String> map) {
			super(map);
		}
		
		@Override
		public synchronized boolean generateStructure(World worldIn, Random randomIn, ChunkPos chunkCoord) {
			boolean result = super.generateStructure(worldIn, randomIn, chunkCoord);
			if (result) {
				
				// This list contains villagers in that structure
				List<EntityVillager> villagers = worldIn.getEntities(EntityVillager.class, villager -> {
					return new ChunkPos(villager.getPosition()).equals(chunkCoord);
				});
				
				// To attempt to have all humanbenders be same type, check if
				// there are nearby humanbenders
				// If there are just copy their type
				AxisAlignedBB aabb = new AxisAlignedBB(chunkCoord.getBlock(-30, 50, -30),
						chunkCoord.getBlock(30, 150, 30));
				List<EntityHumanBender> nearbyBenders = worldIn.getEntitiesWithinAABB(EntityHumanBender.class,
						aabb);
				
				double chance = 100;
				Random rand = new Random();
				if (!villagers.isEmpty() && rand.nextDouble() * 100 < chance) {
					
					Village village = worldIn.getVillageCollection()
							.getNearestVillage(chunkCoord.getBlock(0, 0, 0), 200);
					
					boolean firebender;
					
					if (nearbyBenders.isEmpty()) {
						firebender = new Random().nextBoolean();
					} else {
						firebender = nearbyBenders.get(0) instanceof EntityFirebender;
					}
					
					EntityHumanBender bender = firebender ? new EntityFirebender(worldIn)
							: new EntityAirbender(worldIn);
					bender.copyLocationAndAnglesFrom(villagers.get(0));
					worldIn.spawnEntity(bender);
					
				}
				
			}
			return result;
		}
		
	}
	
}
