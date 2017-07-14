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
package com.crowsofwar.avatar.common.bending.fire;

import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;
import static java.lang.Math.floor;

import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.network.packets.PacketCErrorMessage;
import com.crowsofwar.avatar.common.particle.NetworkParticleSpawner;
import com.crowsofwar.avatar.common.particle.ParticleSpawner;
import com.crowsofwar.gorecore.util.Vector;
import com.crowsofwar.gorecore.util.VectorI;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityLightFire extends FireAbility {
	
	private final ParticleSpawner particles;
	
	/**
	 * @param controller
	 */
	public AbilityLightFire() {
		super("light_fire");
		requireRaytrace(-1, false);
		particles = new NetworkParticleSpawner();
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		
		World world = ctx.getWorld();
		
		VectorI looking = ctx.verifyClientLookBlock(-1, 5);
		EnumFacing side = ctx.getLookSide();
		if (ctx.isLookingAtBlock(-1, 5)) {
			VectorI setAt = new VectorI(looking.x(), looking.y(), looking.z());
			setAt.offset(side);
			BlockPos blockPos = setAt.toBlockPos();
			
			double chance = 20 * ctx.getLevel() + 40;
			
			if (ctx.isMasterLevel(AbilityTreePath.FIRST)) {
				
				int yaw = (int) floor((ctx.getBenderEntity().rotationYaw * 8 / 360) + 0.5) & 7;
				int x = 0, z = 0;
				if (yaw == 1 || yaw == 2 || yaw == 3) x = -1;
				if (yaw == 5 || yaw == 6 || yaw == 7) x = 1;
				if (yaw == 3 || yaw == 4 || yaw == 5) z = -1;
				if (yaw == 0 || yaw == 1 || yaw == 7) z = 1;
				
				if (spawnFire(world, blockPos, ctx, true, chance)) {
					for (int i = 1; i < 5; i++) {
						spawnFire(world, blockPos.add(x * i, 0, z * i), ctx, false, 100);
					}
					ctx.getAbilityData().addXp(SKILLS_CONFIG.litFire);
				}
				
			} else if (ctx.isMasterLevel(AbilityTreePath.SECOND)) {
				
				if (spawnFire(world, blockPos, ctx, true, chance)) {
					spawnFire(world, blockPos.add(1, 0, 0), ctx, false, 100);
					spawnFire(world, blockPos.add(-1, 0, 0), ctx, false, 100);
					spawnFire(world, blockPos.add(0, 0, 1), ctx, false, 100);
					spawnFire(world, blockPos.add(0, 0, -1), ctx, false, 100);
					ctx.getAbilityData().addXp(SKILLS_CONFIG.litFire);
				}
				
			} else {
				if (spawnFire(world, blockPos, ctx, true, chance)) {
					ctx.getAbilityData().addXp(SKILLS_CONFIG.litFire);
				}
			}
			
		}
	}
	
	private boolean spawnFire(World world, BlockPos blockPos, AbilityContext ctx, boolean useChi,
			double chance) {
		
		if (world.isRainingAt(blockPos)) {
			
			particles.spawnParticles(world, EnumParticleTypes.CLOUD, 3, 7, ctx.getLookPos(),
					new Vector(0.5f, 0.75f, 0.5f));
			world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
					SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS,
					0.4f + (float) Math.random() * 0.2f, 0.9f + (float) Math.random() * 0.2f);
			
		} else {
			if (world.getBlockState(blockPos).getBlock() == Blocks.AIR
					&& Blocks.FIRE.canPlaceBlockAt(world, blockPos)) {
				
				if (!useChi || ctx.consumeChi(STATS_CONFIG.chiLightFire)) {
					
					double random = Math.random() * 100;
					
					if (random < chance) {
						
						world.setBlockState(blockPos, Blocks.FIRE.getDefaultState());
						world.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
								SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS,
								0.7f + (float) Math.random() * 0.3f, 0.9f + (float) Math.random() * 0.2f);
						
						return true;
						
					} else if (ctx.getBender().isPlayer()) {
						
						AvatarMod.network.sendTo(new PacketCErrorMessage("avatar.ability.light_fire.fail"),
								(EntityPlayerMP) ctx.getBenderEntity());
						
					}
					
				}
				
			}
		}
		
		return false;
		
	}
	
}
