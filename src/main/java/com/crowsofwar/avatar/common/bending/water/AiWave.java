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
package com.crowsofwar.avatar.common.bending.water;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.gorecore.util.Vector;
import com.crowsofwar.gorecore.util.VectorI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import static com.crowsofwar.gorecore.util.Vector.getEntityPos;
import static com.crowsofwar.gorecore.util.Vector.getRotationTo;
import static java.lang.Math.toDegrees;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AiWave extends BendingAi {
	
	protected AiWave(BendingAbility ability, EntityLiving entity, Bender bender) {
		super(ability, entity, bender);
	}
	
	@Override
	protected boolean shouldExec() {
		
		EntityLivingBase target = entity.getAttackTarget();
		if (target != null && target.isInWater()) {
			
			if (isAtEdgeOfWater()) {
				return true;
			}
			
		}
		
		return false;
		
	}
	
	@Override
	protected void startExec() {
		shouldContinueExecuting();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		
		EntityLivingBase target = entity.getAttackTarget();
		if (target != null && target.isInWater()) {
			entity.getLookHelper().setLookPosition(target.posX, target.posY, target.posZ, 10, 10);
			
			if (timeExecuting >= 40) {
				
				Vector rotations = getRotationTo(getEntityPos(entity), getEntityPos(target));
				entity.rotationYaw = (float) toDegrees(rotations.y());
				entity.rotationPitch = (float) toDegrees(rotations.x());
				
				execAbility();
				return false;
				
			}
			
			return true;
			
		}
		
		return false;
		
	}
	
	private boolean isAtEdgeOfWater() {
		
		World world = entity.world;
		Vector look = getRotationTo(getEntityPos(entity), getEntityPos(entity.getAttackTarget())).setY(0);
		
		Raytrace.Result result = Raytrace.predicateRaytrace(world, Vector.getEntityPos(entity).add(0, -1, 0),
				look, 4, (pos, blockState) -> blockState.getBlock() == Blocks.WATER);
		if (result.hitSomething()) {
			
			VectorI pos = result.getPos();
			IBlockState hitBlockState = world.getBlockState(pos.toBlockPos());
			IBlockState up = world.getBlockState(pos.toBlockPos().up());
			
			for (int i = 0; i < 3; i++) {
				if (world.getBlockState(pos.toBlockPos().up()).getBlock() == Blocks.AIR) {
					return true;
				}
			}
			
		}
		return false;
		
	}
	
}
