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

import java.util.Iterator;

import com.crowsofwar.avatar.common.data.AvatarWorldData;
import com.crowsofwar.avatar.common.data.ScheduledDestroyBlock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EarthbendingEvents {
	
	private EarthbendingEvents() {}
	
	@SubscribeEvent
	public void digSpeed(PlayerEvent.BreakSpeed e) {
		EntityPlayer player = e.getEntityPlayer();
		World world = player.world;
		
		IBlockState state = e.getState();
		if (STATS_CONFIG.bendableBlocks.contains(state.getBlock())) {
			e.setNewSpeed(e.getOriginalSpeed() * 2);
		}
		
	}
	
	@SubscribeEvent
	public void worldUpdate(WorldTickEvent e) {
		World world = e.world;
		if (!world.isRemote && e.phase == TickEvent.Phase.START && world.provider.getDimension() == 0) {
			
			AvatarWorldData wd = AvatarWorldData.getDataFromWorld(world);
			Iterator<ScheduledDestroyBlock> iterator = wd.getScheduledDestroyBlocks().iterator();
			while (iterator.hasNext()) {
				
				ScheduledDestroyBlock sdb = iterator.next();
				sdb.decrementTicks();
				if (sdb.getTicks() <= 0) {
					
					world.destroyBlock(sdb.getPos(), sdb.isDrop());
					iterator.remove();
					
				}
				
			}
			
		}
	}
	
	private void destroyBlock(World world, BlockPos pos, boolean dropBlock, int fortune) {
		
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();
		
		if (!block.isAir(iblockstate, world, pos)) {
			world.playEvent(2001, pos, Block.getStateId(iblockstate));
			
			if (dropBlock) {
				block.dropBlockAsItem(world, pos, iblockstate, fortune);
			}
			
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
		
	}
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new EarthbendingEvents());
	}
	
}
