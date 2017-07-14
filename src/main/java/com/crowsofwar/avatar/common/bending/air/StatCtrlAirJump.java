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
package com.crowsofwar.avatar.common.bending.air;

import static com.crowsofwar.avatar.common.bending.BendingAbility.ABILITY_AIR_JUMP;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import java.util.List;

import com.crowsofwar.avatar.common.AvatarParticles;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.controls.AvatarControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.particle.NetworkParticleSpawner;
import com.crowsofwar.avatar.common.particle.ParticleSpawner;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class StatCtrlAirJump extends StatusControl {
	
	public StatCtrlAirJump() {
		super(0, AvatarControl.CONTROL_JUMP, CrosshairPosition.BELOW_CROSSHAIR);
	}
	
	@Override
	public boolean execute(BendingContext ctx) {
		
		Bender bender = ctx.getBender();
		EntityLivingBase entity = ctx.getBenderEntity();
		BendingData data = ctx.getData();
		World world = ctx.getWorld();
		
		AbilityData abilityData = data.getAbilityData(ABILITY_AIR_JUMP);
		boolean allowDoubleJump = abilityData.getLevel() == 3
				&& abilityData.getPath() == AbilityTreePath.FIRST;
		
		// Figure out whether entity is on ground by finding collisions with
		// ground - if found a collision box, then is not on ground
		List<AxisAlignedBB> collideWithGround = world.getCollisionBoxes(entity,
				entity.getEntityBoundingBox().expand(0, 0.5, 0));
		boolean onGround = !collideWithGround.isEmpty();
		
		if (onGround || (allowDoubleJump && ctx.consumeChi(STATS_CONFIG.chiAirJump))) {
			
			int lvl = abilityData.getLevel();
			double multiplier = 0.65;
			if (lvl >= 1) {
				multiplier = 1;
			}
			if (lvl >= 2) {
				multiplier = 1.2;
			}
			if (lvl >= 3) {
				multiplier = 1.4;
			}
			
			Vector rotations = new Vector(Math.toRadians((entity.rotationPitch) / 1),
					Math.toRadians(entity.rotationYaw), 0);
			
			Vector velocity = rotations.toRectangular();
			velocity.setY(Math.pow(velocity.y(), .1));
			velocity.mul(multiplier);
			if (!onGround) {
				velocity.mul(0.6);
				entity.motionX = 0;
				entity.motionY = 0;
				entity.motionZ = 0;
			}
			entity.addVelocity(velocity.x(), velocity.y(), velocity.z());
			if (entity instanceof EntityPlayerMP) {
				((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityVelocity(entity));
			}
			
			ParticleSpawner spawner = new NetworkParticleSpawner();
			spawner.spawnParticles(entity.world, AvatarParticles.getParticleAir(), 2, 6,
					new Vector(entity), new Vector(1, 0, 1));
			
			float fallAbsorption = 0;
			if (lvl == 0) {
				fallAbsorption = 8;
			} else if (lvl == 1) {
				fallAbsorption = 13;
			} else if (lvl == 2) {
				fallAbsorption = 16;
			} else if (lvl == 3) {
				fallAbsorption = 19;
			}
			
			data.setFallAbsorption(fallAbsorption);
			
			data.addTickHandler(TickHandler.AIR_PARTICLE_SPAWNER);
			if (abilityData.getLevel() == 3 && abilityData.getPath() == AbilityTreePath.SECOND) {
				data.addTickHandler(TickHandler.SMASH_GROUND);
			}
			abilityData.addXp(STATS_CONFIG.chiAirJump);
			
			entity.world.playSound(null, new BlockPos(entity), SoundEvents.ENTITY_BAT_TAKEOFF,
					SoundCategory.PLAYERS, 1, .7f);
			
			return true;
			
		}
		
		return false;
		
	}
	
}
