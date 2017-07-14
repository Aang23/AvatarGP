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
package com.crowsofwar.avatar.common.network.packets;

import java.util.UUID;

import com.crowsofwar.avatar.common.network.PacketRedirector;
import com.crowsofwar.gorecore.util.AccountUUIDs;
import com.crowsofwar.gorecore.util.AccountUUIDs.AccountId;
import com.crowsofwar.gorecore.util.GoreCoreByteBufUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Sent from client to server to request data about a player.
 *
 */
public class PacketSRequestData extends AvatarPacket<PacketSRequestData> {
	
	private UUID asking;
	
	public PacketSRequestData() {}
	
	public PacketSRequestData(UUID asking) {
		this.asking = asking;
	}
	
	public PacketSRequestData(EntityPlayer player) {
		AccountId result = AccountUUIDs.getId(player.getName());
		this.asking = result.getUUID();
	}
	
	@Override
	public void avatarFromBytes(ByteBuf buf) {
		asking = GoreCoreByteBufUtil.readUUID(buf);
	}
	
	@Override
	public void avatarToBytes(ByteBuf buf) {
		GoreCoreByteBufUtil.writeUUID(buf, asking);
	}
	
	@Override
	public Side getRecievedSide() {
		return Side.SERVER;
	}
	
	public UUID getAskedPlayer() {
		return asking;
	}
	
	@Override
	protected AvatarPacket.Handler<PacketSRequestData> getPacketHandler() {
		return PacketRedirector::redirectMessage;
	}
	
}
