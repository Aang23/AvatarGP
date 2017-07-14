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
package com.crowsofwar.avatar.common.data.ctx;

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.avatar.common.util.Raytrace.Result;
import com.crowsofwar.gorecore.util.VectorI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Used by AvatarPlayerData. Holds information about the player right now, such as its position,
 * entity, and looking at block.
 * 
 */
// TODO Move into AbilityContext
@Deprecated
public class PlayerState {
	
	private EntityPlayer playerEntity;
	private VectorI clientLookAtBlock;
	private VectorI serverLookAtBlock;
	private EnumFacing lookAtSide;
	
	public PlayerState() {
		
	}
	
	public PlayerState(EntityPlayer playerEntity, VectorI clientLookAtBlock, EnumFacing lookAtSide) {
		update(playerEntity, clientLookAtBlock, lookAtSide);
	}
	
	public void update(EntityPlayer playerEntity, Result raytrace) {
		update(playerEntity, raytrace == null ? null : raytrace.getPos(),
				raytrace == null ? null : raytrace.getSide());
	}
	
	public void update(EntityPlayer playerEntity, VectorI clientLookAtBlock, EnumFacing lookAtSide) {
		this.playerEntity = playerEntity;
		this.clientLookAtBlock = clientLookAtBlock;
		this.lookAtSide = lookAtSide;
	}
	
	/**
	 * Use PlayerData.getPlayerEntity instead.
	 * 
	 * @return
	 */
	@Deprecated
	public EntityPlayer getPlayerEntity() {
		return playerEntity;
	}
	
	public VectorI getClientLookAtBlock() {
		return clientLookAtBlock;
	}
	
	/**
	 * Get the side of the block the player is looking at
	 * 
	 * @return
	 */
	public EnumFacing getLookAtSide() {
		return lookAtSide;
	}
	
	/**
	 * Returns whether the player is looking at a block right now without verifying if the client is
	 * correct.
	 */
	public boolean isLookingAtBlock() {
		return lookAtSide != null && clientLookAtBlock != null;
	}
	
	/**
	 * Returns whether the player is looking at a block right now. Checks for hacking on the server.
	 * If on client side, then no checks are made.
	 * 
	 * @see #verifyClientLookAtBlock(double, double)
	 */
	public boolean isLookingAtBlock(double raycastDist, double maxDeviation) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return isLookingAtBlock();
		return lookAtSide != null && verifyClientLookAtBlock(raycastDist, maxDeviation) != null;
	}
	
	/**
	 * Ensure that the client's targeted block is within range of the server's targeted block. (To
	 * avoid hacking) On client side, simply returns the client's targeted block.
	 * 
	 * @param raycastDist
	 *            How far away can the block be?
	 * @param maxDeviation
	 *            How far away can server and client's target positions be?
	 * 
	 * @see Raytrace#getTargetBlock(EntityPlayer, double)
	 */
	public VectorI verifyClientLookAtBlock(double raycastDist, double maxDeviation) {
		if (clientLookAtBlock == null) return null;
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return clientLookAtBlock;
		Result res = Raytrace.getTargetBlock(playerEntity, raycastDist);
		if (!res.hitSomething()) return null;
		this.serverLookAtBlock = res.getPos();
		double dist = serverLookAtBlock.dist(clientLookAtBlock);
		if (dist <= maxDeviation) {
			return clientLookAtBlock;
		} else {
			AvatarLog.warn("Warning: PlayerState- Client sent too far location " + "to look at block. ("
					+ dist + ") Hacking?");
			Thread.dumpStack();
			return serverLookAtBlock;
		}
	}
	
}
