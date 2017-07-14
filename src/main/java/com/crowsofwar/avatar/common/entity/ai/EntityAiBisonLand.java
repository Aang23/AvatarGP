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

import static net.minecraft.util.math.MathHelper.floor;

import com.crowsofwar.avatar.common.entity.mob.EntitySkyBison;
import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntityMoveHelper.Action;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Bison lands when he is hungry. This allows the bison to eat grass and to
 * consume less food points. Considered a MOVEMENT task, so has mutex bits 1.
 * 
 * @author CrowsOfWar
 */
public class EntityAiBisonLand extends EntityAIBase {
	
	private final EntitySkyBison bison;
	
	public EntityAiBisonLand(EntitySkyBison bison) {
		this.bison = bison;
		setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute() {
		return bison.wantsGrass();
	}
	
	@Override
	public void startExecuting() {
		
		World world = bison.world;
		
		int tries = 0;
		Vector landing;
		boolean isValidPosition;
		do {
			
			landing = findLandingPoint().add(0, 1, 0);
			tries++;
			
			Block block = world.getBlockState(landing.toBlockPos().down()).getBlock();
			isValidPosition = (block == Blocks.GRASS || block == Blocks.TALLGRASS) && canFit(landing)
					&& canGetTo(landing);
			
		} while (!isValidPosition && tries < 5);
		
		if (isValidPosition) {
			
			landing.add(0, 1, 0);
			bison.getMoveHelper().setMoveTo(landing.x(), landing.y() - 1, landing.z(), 1);
			
		}
		
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		// Once got close to grass, close enough
		EntityMoveHelper mh = bison.getMoveHelper();
		if (bison.getDistanceSq(mh.getX(), mh.getY(), mh.getZ()) <= 5) {
			bison.getMoveHelper().action = Action.WAIT;
		}
		
		// Don't wander off until we have food!
		return !bison.isFull() && bison.isEatingGrass();
	}
	
	private Vector findLandingPoint() {
		
		double maxDist = 2;
		
		double x = bison.posX + (bison.getRNG().nextDouble() * 2 - 1) * maxDist;
		double z = bison.posZ + (bison.getRNG().nextDouble() * 2 - 1) * maxDist;
		
		int y = (int) bison.posY;
		while (!isSolidBlock(new BlockPos(x, y, z))) {
			y--;
		}
		return new Vector(x, y, z);
		
	}
	
	private boolean canFit(Vector pos) {
		
		double minX = pos.x() - bison.width / 2;
		double maxX = pos.x() + bison.width / 2;
		double minY = pos.y();
		double maxY = pos.y() + bison.height;
		double minZ = pos.z() - bison.width / 2;
		double maxZ = pos.z() + bison.width / 2;
		
		for (int x = floor(minX); x <= maxX; x++) {
			for (int y = floor(minY); y <= maxY; y++) {
				for (int z = floor(minZ); z <= maxZ; z++) {
					if (isSolidBlock(new BlockPos(x, y, z))) {
						return false;
					}
				}
			}
		}
		
		return true;
		
	}
	
	/**
	 * Figure out whether the bison can get to that position by raytracing
	 */
	private boolean canGetTo(Vector target) {
		Vector current = Vector.getEntityPos(bison);
		Vector direction = target.minus(current).normalize();
		double range = current.dist(target);
		Raytrace.Result raytrace = Raytrace.raytrace(bison.world, current, direction, range + 1, false);
		BlockPos blockPos = raytrace.getPos() == null ? null : raytrace.getPos().toBlockPos().up();
		return blockPos == null || blockPos.equals(target.toBlockPos());
	}
	
	private boolean isSolidBlock(BlockPos pos) {
		World world = bison.world;
		return world.isBlockNormalCube(pos, false);
	}
	
}
