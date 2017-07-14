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

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.AvatarLog.WarningType;
import com.crowsofwar.avatar.AvatarMod;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public abstract class AvatarPacket<MSG extends IMessage> implements IMessage, IMessageHandler<MSG, IMessage> {
	
	public AvatarPacket() {}
	
	@Override
	public final IMessage onMessage(MSG message, MessageContext ctx) {
		
		Handler<MSG> handler = getPacketHandler();
		
		IThreadListener mainThread = getRecievedSide().isServer()
				? ctx.getServerHandler().player.getServerWorld()
				: AvatarMod.proxy.getClientThreadListener();
		
		mainThread.addScheduledTask(() -> {
			IMessage followup = handler.onMessageRecieved(message, ctx);
			if (followup != null) {
				if (ctx.side.isClient()) {
					AvatarMod.network.sendToServer(followup);
				} else {
					AvatarMod.network.sendTo(followup, ctx.getServerHandler().player);
				}
			}
		});
		
		return null;
		
	}
	
	@Override
	public final void fromBytes(ByteBuf buf) {
		try {
			avatarFromBytes(buf);
		} catch (RuntimeException ex) {
			AvatarLog.warn(WarningType.BAD_CLIENT_PACKET,
					"Error processing packet " + getClass().getSimpleName(), ex);
		}
	}
	
	@Override
	public final void toBytes(ByteBuf buf) {
		try {
			avatarToBytes(buf);
		} catch (RuntimeException ex) {
			AvatarLog.warn(WarningType.BAD_CLIENT_PACKET,
					"Error processing packet " + getClass().getSimpleName(), ex);
		}
	}
	
	protected abstract void avatarFromBytes(ByteBuf buf);
	
	protected abstract void avatarToBytes(ByteBuf buf);
	
	/**
	 * Returns the side that this packet is meant to be received on.
	 */
	protected abstract Side getRecievedSide();
	
	/**
	 * Get a packet handler which contains the logic for when this packet is
	 * received.
	 */
	protected abstract Handler<MSG> getPacketHandler();
	
	/**
	 * An interface to handle the packet being received.
	 * <p>
	 * Method must be implemented:
	 * {@link #onMessageRecieved(MSG, MessageContext)}.
	 * 
	 * @param <MSG>
	 *            The type of the message to receive
	 * 
	 * @author CrowsOfWar
	 */
	@FunctionalInterface
	public interface Handler<MSG extends IMessage> {
		
		/**
		 * Called to handle the packet being received.
		 * 
		 * @param message
		 *            The actual instance of the received packet. You use this
		 *            instance to retrieve the necessary data.
		 * @param ctx
		 *            The context of the message. Can be used to obtain
		 *            necessary objects such as a player entity.
		 * 
		 * @return The follow-up packet, null for none. Note: doesn't actually
		 *         use default SimpleImpl follow ups.
		 */
		IMessage onMessageRecieved(MSG message, MessageContext ctx);
		
	}
	
}
