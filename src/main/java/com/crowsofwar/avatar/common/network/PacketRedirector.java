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
package com.crowsofwar.avatar.common.network;

import com.crowsofwar.avatar.AvatarMod;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Redirect a packet to the correct sided packet handler. Contains a single
 * method, {@link #redirectMessage(IMessage, MessageContext)}. Not to be
 * instantiated.
 *
 */
public class PacketRedirector {
	
	/**
	 * Only use static methods. Not to be instantiated.
	 */
	private PacketRedirector() {}
	
	public static IMessage redirectMessage(IMessage message, MessageContext ctx) {
		IPacketHandler packetHandler;
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side == Side.CLIENT) {
			packetHandler = AvatarMod.proxy.getClientPacketHandler();
		} else {
			packetHandler = PacketHandlerServer.instance;
		}
		return packetHandler.onPacketReceived(message, ctx);
	}
	
}
