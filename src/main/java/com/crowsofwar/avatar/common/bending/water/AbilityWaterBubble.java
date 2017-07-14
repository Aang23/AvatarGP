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

import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityWaterBubble;
import com.crowsofwar.avatar.common.entity.data.WaterBubbleBehavior;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityWaterBubble extends WaterAbility {
	
	public AbilityWaterBubble() {
		super("water_bubble");
		requireRaytrace(-1, false);
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		EntityLivingBase entity = ctx.getBenderEntity();
		BendingData data = ctx.getData();
		World world = ctx.getWorld();
		
		if (ctx.isLookingAtBlock()) {
			BlockPos lookPos = ctx.getClientLookBlock().toBlockPos();
			IBlockState lookingAtBlock = world.getBlockState(lookPos);
			if (lookingAtBlock.getBlock() == Blocks.WATER) {
				
				if (ctx.consumeChi(STATS_CONFIG.chiWaterBubble)) {
					
					EntityWaterBubble existing = AvatarEntity.lookupEntity(world, EntityWaterBubble.class, //
							bub -> bub.getBehavior() instanceof WaterBubbleBehavior.PlayerControlled
									&& bub.getOwner() == entity);
					
					if (existing != null) {
						existing.setBehavior(new WaterBubbleBehavior.Drop());
						// prevent bubble from removing status control
						existing.setOwner(null);
					}
					
					Vector pos = ctx.getLookPos();
					
					EntityWaterBubble bubble = new EntityWaterBubble(world);
					bubble.setPosition(pos.x(), pos.y(), pos.z());
					bubble.setBehavior(new WaterBubbleBehavior.PlayerControlled());
					bubble.setOwner(entity);
					bubble.setSourceBlock(ctx.getLevel() >= 2);
					world.spawnEntity(bubble);
					
					data.addStatusControl(StatusControl.THROW_BUBBLE);
					data.getAbilityData(this).addXp(SKILLS_CONFIG.createBubble);
					
					if (!ctx.isMasterLevel(AbilityTreePath.SECOND)) {
						world.setBlockToAir(lookPos);
					}
					
				}
			}
		}
	}
	
}
