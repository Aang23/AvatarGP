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

import static com.crowsofwar.gorecore.util.GoreCoreByteBufUtil.readUUID;
import static com.crowsofwar.gorecore.util.GoreCoreByteBufUtil.writeUUID;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.AvatarLog.WarningType;
import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.data.DataCategory;
import com.crowsofwar.gorecore.GoreCore;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class PacketCPlayerData extends AvatarPacket<PacketCPlayerData> {
	
	/**
	 * Used server-side to find what to write<br />
	 * Used client-side to write to
	 */
	private AvatarPlayerData data;
	private UUID playerId;
	
	// We need a SortedSet since the order of writing/reading the data is
	// determined by the order of the DataCategories
	// If there was a regular set(no ordering guarantees), the order could have
	// changed, which would make the data get read/written in the wrong order
	// which would probably cause bugs/crashing
	private SortedSet<DataCategory> changed;
	
	public PacketCPlayerData() {}
	
	public PacketCPlayerData(AvatarPlayerData data, UUID player, SortedSet<DataCategory> changed) {
		this.data = data;
		this.playerId = player;
		this.changed = changed;
	}
	
	@Override
	public void avatarFromBytes(ByteBuf buf) {
		playerId = readUUID(buf);
		AvatarPlayerData data = AvatarPlayerData.fetcher()
				.fetch(GoreCore.proxy.getClientSidePlayer().world, playerId);
		
		if (data != null) {
			
			// Find what changed
			changed = new TreeSet<>();
			int size = buf.readInt();
			for (int i = 0; i < size; i++) {
				changed.add(DataCategory.values()[buf.readInt()]);
			}
			
			// Read what changed
			AvatarMod.proxy.getClientThreadListener().addScheduledTask(() -> {
				for (DataCategory category : changed) {
					category.read(buf, data);
				}
			});
			
		} else {
			AvatarLog.warn(WarningType.WEIRD_PACKET, "Server sent a packet about data for player " + playerId
					+ " but data couldn't be found for them");
		}
		
	}
	
	@Override
	public void avatarToBytes(ByteBuf buf) {
		writeUUID(buf, playerId);
		
		// Tell client what has changed
		buf.writeInt(changed.size());
		for (DataCategory category : changed) {
			buf.writeInt(category.ordinal());
		}
		
		// The "real" payload - player data
		for (DataCategory category : changed) {
			category.write(buf, data);
		}
		
	}
	
	@Override
	protected Side getRecievedSide() {
		return Side.CLIENT;
	}
	
	@Override
	protected com.crowsofwar.avatar.common.network.packets.AvatarPacket.Handler<PacketCPlayerData> getPacketHandler() {
		
		// Do nothing!
		// In the avatarFromBytes method, already saved the data into the
		// player-data (when DataCategory.read is called)
		
		return (msg, ctx) -> null;
	}
	
	public UUID getPlayerId() {
		return playerId;
	}
	
}
