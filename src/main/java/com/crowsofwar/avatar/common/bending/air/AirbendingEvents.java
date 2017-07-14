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

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.controls.AvatarControl;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityAirBubble;
import com.crowsofwar.avatar.common.network.packets.PacketSWallJump;
import com.crowsofwar.gorecore.GoreCore;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AirbendingEvents {
	
	private AirbendingEvents() {}
	
	private void tick(EntityPlayer player, World world, AvatarPlayerData data) {
		if (player == GoreCore.proxy.getClientSidePlayer() && player.isCollidedHorizontally
				&& !player.isCollidedVertically && data.getTimeInAir() >= STATS_CONFIG.wallJumpDelay) {
			if (AvatarControl.CONTROL_JUMP.isPressed()) {
				AvatarMod.network.sendToServer(new PacketSWallJump());
			}
		}
		if (player.onGround) {
			data.setWallJumping(false);
			data.setTimeInAir(0);
		} else {
			data.setTimeInAir(data.getTimeInAir() + 1);
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		EntityPlayer player = e.player;
		World world = player.world;
		AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(player);
		if (data.hasBending(BendingType.AIRBENDING)) {
			tick(player, world, data);
		}
	}
	
	@SubscribeEvent
	public void airBubbleShield(LivingAttackEvent e) {
		World world = e.getEntity().world;
		
		EntityLivingBase attacked = (EntityLivingBase) e.getEntity();
		
		if (Bender.isBenderSupported(attacked)) {
			BendingData data = Bender.create(attacked).getData();
			if (data.hasStatusControl(StatusControl.BUBBLE_CONTRACT)) {
				EntityAirBubble bubble = AvatarEntity.lookupControlledEntity(world, EntityAirBubble.class,
						attacked);
				if (bubble != null) {
					if (bubble.attackEntityFrom(e.getSource(), e.getAmount())) {
						e.setCanceled(true);
						world.playSound(null, attacked.getPosition(), SoundEvents.BLOCK_CLOTH_HIT,
								SoundCategory.PLAYERS, 1, 1);
					}
				}
			}
		}
		
	}
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new AirbendingEvents());
	}
	
}
