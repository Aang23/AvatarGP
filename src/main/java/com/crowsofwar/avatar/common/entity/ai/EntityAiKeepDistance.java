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

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.Vec3d;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityAiKeepDistance extends EntityAIBase {
	
	private final EntityCreature entity;
	private final double maxSafeDistance;
	private final double speed;
	private Path path;
	
	public EntityAiKeepDistance(EntityCreature entity, double maxSafeDistance, double speed) {
		this.entity = entity;
		this.maxSafeDistance = maxSafeDistance;
		this.speed = speed;
	}
	
	@Override
	public boolean shouldExecute() {
		EntityLivingBase target = entity.getAttackTarget();
		if (target != null && entity.getDistanceSqToEntity(target) <= maxSafeDistance * maxSafeDistance) {
			
			Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entity, 16, 7,
					new Vec3d(target.posX, target.posY, target.posZ));
			
			if (vec3d == null) {
				return false;
			} else if (entity.getDistanceSq(vec3d.x, vec3d.y, vec3d.z) < entity
					.getDistanceSqToEntity(target)) {
				return false;
			} else {
				path = entity.getNavigator().getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
				return path != null;
			}
			
		} else {
			return false;
		}
	}
	
	@Override
	public void startExecuting() {
		entity.getNavigator().setPath(path, speed);
	}
	
}
