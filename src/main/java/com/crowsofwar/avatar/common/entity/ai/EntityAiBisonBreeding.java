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
package com.crowsofwar.avatar.common.entity.ai;

import com.crowsofwar.avatar.common.entity.data.AnimalCondition;
import com.crowsofwar.avatar.common.entity.data.BisonSpawnData;
import com.crowsofwar.avatar.common.entity.mob.EntitySkyBison;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Random;

import static com.crowsofwar.avatar.common.config.ConfigMobs.MOBS_CONFIG;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityAiBisonBreeding extends EntityAIBase {
	
	private final EntitySkyBison bison;
	
	public EntityAiBisonBreeding(EntitySkyBison bison) {
		this.bison = bison;
		setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute() {
		AnimalCondition cond = bison.getCondition();
		return !cond.isSterile() && cond.getBreedTimer() == 0 && cond.isAdult();
	}
	
	@Override
	public void startExecuting() {
		bison.setLoveParticles(true);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		
		if (!shouldExecute()) {
			bison.setLoveParticles(false);
			return false;
		}
		
		double range = 100;
		
		Vector pos = Vector.getEntityPos(bison);
		Vector min = pos.minus(range / 2, range / 2, range / 2);
		Vector max = pos.plus(range / 2, range / 2, range / 2);
		
		AxisAlignedBB aabb = new AxisAlignedBB(min.toMinecraft(), max.toMinecraft());
		
		EntitySkyBison nearest = bison.world.findNearestEntityWithinAABB(EntitySkyBison.class, aabb,
				bison);
		
		if (nearest != null) {
			if (getNearbyBison(nearest) < 15) {
				bison.getMoveHelper().setMoveTo(nearest.posX, nearest.posY, nearest.posZ, 1);
				// 7 obtained through real-world testing
				if (bison.getDistanceSqToEntity(nearest) <= 7) {
					
					spawnBaby(nearest);
					
					bison.getCondition().setBreedTimer(generateBreedTimer());
					nearest.getCondition().setBreedTimer(generateBreedTimer());
					bison.setLoveParticles(false);
					nearest.setLoveParticles(false);
					
					return true;
					
				}
			}
		}
		
		return true;
		
	}
	
	private void spawnBaby(EntitySkyBison mate) {
		
		World world = bison.world;
		AnimalCondition cond = bison.getCondition();
		EntitySkyBison child = new EntitySkyBison(world);
		
		if (child != null) {
			
			// Spawn the baby
			child.getCondition().setAge(0);
			child.setLocationAndAngles(bison.posX, bison.posY, bison.posZ, 0, 0);
			child.onInitialSpawn(world.getDifficultyForLocation(bison.getPosition()),
					new BisonSpawnData(true));
			world.spawnEntity(child);
			
			// Spawn heart particles
			Random random = bison.getRNG();
			for (int i = 0; i < 7; ++i) {
				
				double mx = random.nextGaussian() * 0.02D;
				double my = random.nextGaussian() * 0.02D;
				double mz = random.nextGaussian() * 0.02D;
				
				double dx = random.nextDouble() * bison.width * 2 - bison.width;
				double dy = 0.5 + random.nextDouble() * bison.height;
				double dz = random.nextDouble() * bison.width * 2 - bison.width;
				
				world.spawnParticle(EnumParticleTypes.HEART, bison.posX + dx, bison.posY + dy,
						bison.posZ + dz, mx, my, mz);
				
			}
			
			// Spawn XP orbs
			if (world.getGameRules().getBoolean("doMobLoot")) {
				world.spawnEntity(
						new EntityXPOrb(world, bison.posX, bison.posY, bison.posZ, random.nextInt(7) + 1));
			}
			
		}
	}
	
	private int generateBreedTimer() {
		Random random = bison.getRNG();
		float min = MOBS_CONFIG.bisonBreedMinMinutes;
		float max = MOBS_CONFIG.bisonBreedMaxMinutes;
		float minutes = min + random.nextFloat() * (max - min);
		return (int) (minutes * 1200);
	}
	
	private int getNearbyBison(EntitySkyBison otherBison) {
		
		World world = bison.world;
		
		AxisAlignedBB aabb = new AxisAlignedBB(bison.posX - 32, 0, bison.posZ - 32, bison.posX + 32, 255,
				bison.posZ + 32);
		
		return world.getEntitiesWithinAABB(EntitySkyBison.class, aabb, b -> b != bison && b != otherBison)
				.size();
		
	}
	
}
