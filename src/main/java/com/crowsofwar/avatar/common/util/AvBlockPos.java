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
package com.crowsofwar.avatar.common.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;

/**
 * Recommended to use minecraft Vec3i instead.
 * 
 * @author CrowsOfWar
 */
@Deprecated
public class AvBlockPos {
	
	public int x, y, z;
	
	public AvBlockPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}
	
	public static AvBlockPos fromBytes(ByteBuf buf) {
		return new AvBlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	public double dist(AvBlockPos pos) {
		return dist(this, pos);
	}
	
	public double distSq(AvBlockPos pos) {
		return distSq(this, pos);
	}
	
	public static double dist(AvBlockPos pos1, AvBlockPos pos2) {
		return Math.sqrt(distSq(pos1, pos2));
	}
	
	public static double distSq(AvBlockPos pos1, AvBlockPos pos2) {
		double dx = pos2.x - pos1.x;
		double dy = pos2.y - pos1.y;
		double dz = pos2.z - pos1.z;
		return dx * dx + dy * dy + dz * dz;
	}
	
	/**
	 * Move this BlockPos in the specified direction by 1 meter.
	 */
	public void offset(EnumFacing direction) {
		switch (direction.getAxis()) {
			case X:
				x += direction.getAxisDirection().getOffset();
				break;
			case Y:
				y += direction.getAxisDirection().getOffset();
				break;
			case Z:
				z += direction.getAxisDirection().getOffset();
				break;
		}
	}
	
	/**
	 * Returns true if the object is a BlockPos and it has the same coordinates.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AvBlockPos)) return false;
		AvBlockPos pos = (AvBlockPos) obj;
		return x == pos.x && y == pos.y && z == pos.z;
	}
	
}
