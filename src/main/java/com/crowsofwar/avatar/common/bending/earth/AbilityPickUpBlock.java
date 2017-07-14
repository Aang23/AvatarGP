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
package com.crowsofwar.avatar.common.bending.earth;

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import java.util.Random;

import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityFloatingBlock;
import com.crowsofwar.avatar.common.entity.data.FloatingBlockBehavior;
import com.crowsofwar.gorecore.util.Vector;
import com.crowsofwar.gorecore.util.VectorI;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityPickUpBlock extends EarthAbility {
	
	private final Random random;
	
	public AbilityPickUpBlock() {
		super("pickup_block");
		this.random = new Random();
		requireRaytrace(-1, true);
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		
		BendingData data = ctx.getData();
		EntityLivingBase entity = ctx.getBenderEntity();
		Bender bender = ctx.getBender();
		World world = ctx.getWorld();
		
		EntityFloatingBlock currentBlock = AvatarEntity.lookupEntity(ctx.getWorld(),
				EntityFloatingBlock.class,
				fb -> fb.getBehavior() instanceof FloatingBlockBehavior.PlayerControlled
						&& fb.getOwner() == ctx.getBenderEntity());
		
		if (currentBlock != null) {
			currentBlock.drop();
			data.removeStatusControl(StatusControl.THROW_BLOCK);
			data.removeStatusControl(StatusControl.PLACE_BLOCK);
		} else {
			VectorI target = ctx.verifyClientLookBlock(-1, 5);
			if (target != null) {
				
				pickupBlock(ctx, target.toBlockPos());
				
				// EnumFacing direction = entity.getHorizontalFacing();
				// pickupBlock(ctx, target.toBlockPos().offset(direction));
				// pickupBlock(ctx,
				// target.toBlockPos().offset(direction.getOpposite()));
				
			}
		}
	}
	
	private void pickupBlock(AbilityContext ctx, BlockPos pos) {
		
		World world = ctx.getWorld();
		Bender bender = ctx.getBender();
		EntityLivingBase entity = ctx.getBenderEntity();
		BendingData data = ctx.getData();
		
		IBlockState ibs = world.getBlockState(pos);
		Block block = ibs.getBlock();
		
		if (!world.isAirBlock(pos) && STATS_CONFIG.bendableBlocks.contains(block)) {
			
			if (ctx.consumeChi(STATS_CONFIG.chiPickUpBlock)) {
				
				AbilityData abilityData = data.getAbilityData(this);
				float xp = abilityData.getTotalXp();
				
				EntityFloatingBlock floating = new EntityFloatingBlock(world, ibs);
				floating.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				floating.setItemDropsEnabled(!bender.isCreativeMode());
				
				double dist = 2.5;
				Vector force = new Vector(0, Math.sqrt(19.62 * dist), 0);
				floating.velocity().add(force);
				floating.setBehavior(new FloatingBlockBehavior.PickUp());
				floating.setOwner(entity);
				floating.setDamageMult(abilityData.getLevel() >= 2 ? 2 : 1);
				
				if (STATS_CONFIG.preventPickupBlockGriefing) {
					floating.setItemDropsEnabled(false);
				} else {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
				
				world.spawnEntity(floating);
				
				SoundType sound = block.getSoundType();
				if (sound != null) {
					world.playSound(null, floating.getPosition(), sound.getBreakSound(),
							SoundCategory.PLAYERS, sound.getVolume(), sound.getPitch());
				}
				
				data.addStatusControl(StatusControl.PLACE_BLOCK);
				data.addStatusControl(StatusControl.THROW_BLOCK);
				
			}
			
		} else {
			world.playSound(null, entity.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS,
					1, (float) (random.nextGaussian() / 0.25 + 0.375));
		}
		
	}
	
}
