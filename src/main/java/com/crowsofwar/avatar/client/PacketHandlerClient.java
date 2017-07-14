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
package com.crowsofwar.avatar.client;

import java.util.Random;

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.AvatarLog.WarningType;
import com.crowsofwar.avatar.client.gui.AvatarUiRenderer;
import com.crowsofwar.avatar.common.network.IPacketHandler;
import com.crowsofwar.avatar.common.network.packets.PacketCErrorMessage;
import com.crowsofwar.avatar.common.network.packets.PacketCParticles;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Handles packets addressed to the client. Packets like this have a C in their
 * name.
 *
 */
@SideOnly(Side.CLIENT)
public class PacketHandlerClient implements IPacketHandler {
	
	private final Minecraft mc;
	
	public PacketHandlerClient() {
		this.mc = Minecraft.getMinecraft();
	}
	
	@Override
	public IMessage onPacketReceived(IMessage packet, MessageContext ctx) {
		
		if (packet instanceof PacketCParticles) return handlePacketParticles((PacketCParticles) packet, ctx);
		
		if (packet instanceof PacketCErrorMessage)
			return handlePacketNotEnoughChi((PacketCErrorMessage) packet, ctx);
		
		AvatarLog.warn(WarningType.WEIRD_PACKET, "Client recieved unknown packet from server:" + packet);
		
		return null;
	}
	
	@Override
	public Side getSide() {
		return Side.CLIENT;
	}
	
	private IMessage handlePacketParticles(PacketCParticles packet, MessageContext ctx) {
		
		EnumParticleTypes particle = packet.getParticle();
		if (particle == null) {
			AvatarLog.warn(WarningType.WEIRD_PACKET, "Unknown particle recieved from server");
			return null;
		}
		
		Random random = new Random();
		
		int particles = random.nextInt(packet.getMaximum() - packet.getMinimum() + 1) + packet.getMinimum();
		
		for (int i = 0; i < particles; i++) {
			mc.world.spawnParticle(particle, packet.getX(), packet.getY(), packet.getZ(),
					packet.getMaxVelocityX() * random.nextGaussian(),
					packet.getMaxVelocityY() * random.nextGaussian(),
					packet.getMaxVelocityZ() * random.nextGaussian());
		}
		
		return null;
	}
	
	private IMessage handlePacketNotEnoughChi(PacketCErrorMessage packet, MessageContext ctx) {
		
		AvatarUiRenderer.displayErrorMessage(packet.getMessage());
		
		return null;
	}
	
}
