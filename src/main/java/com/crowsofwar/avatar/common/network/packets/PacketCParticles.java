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

import com.crowsofwar.avatar.common.network.PacketRedirector;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class PacketCParticles extends AvatarPacket<PacketCParticles> {
	
	private EnumParticleTypes particle;
	private int minimum, maximum;
	private double x, y, z;
	private double maxVelocityX, maxVelocityY, maxVelocityZ;
	
	public PacketCParticles() {}
	
	/**
	 * @param particle
	 * @param minimum
	 * @param maximum
	 * @param x
	 * @param y
	 * @param z
	 * @param maxVelocityX
	 * @param maxVelocityY
	 * @param maxVelocityZ
	 */
	public PacketCParticles(EnumParticleTypes particle, int minimum, int maximum, double x, double y,
			double z, double maxVelocityX, double maxVelocityY, double maxVelocityZ) {
		this.particle = particle;
		this.minimum = minimum;
		this.maximum = maximum;
		this.x = x;
		this.y = y;
		this.z = z;
		this.maxVelocityX = maxVelocityX;
		this.maxVelocityY = maxVelocityY;
		this.maxVelocityZ = maxVelocityZ;
	}
	
	@Override
	public void avatarFromBytes(ByteBuf buf) {
		particle = EnumParticleTypes.values()[buf.readInt()];
		minimum = buf.readInt();
		maximum = buf.readInt();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		maxVelocityX = buf.readDouble();
		maxVelocityY = buf.readDouble();
		maxVelocityZ = buf.readDouble();
	}
	
	@Override
	public void avatarToBytes(ByteBuf buf) {
		buf.writeInt(particle.ordinal());
		buf.writeInt(minimum);
		buf.writeInt(maximum);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeDouble(maxVelocityX);
		buf.writeDouble(maxVelocityY);
		buf.writeDouble(maxVelocityZ);
	}
	
	@Override
	protected Side getRecievedSide() {
		return Side.CLIENT;
	}
	
	@Override
	protected com.crowsofwar.avatar.common.network.packets.AvatarPacket.Handler<PacketCParticles> getPacketHandler() {
		return PacketRedirector::redirectMessage;
	}
	
	public EnumParticleTypes getParticle() {
		return particle;
	}
	
	public int getMinimum() {
		return minimum;
	}
	
	public int getMaximum() {
		return maximum;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public double getMaxVelocityX() {
		return maxVelocityX;
	}
	
	public double getMaxVelocityY() {
		return maxVelocityY;
	}
	
	public double getMaxVelocityZ() {
		return maxVelocityZ;
	}
	
}
