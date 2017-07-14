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
package com.crowsofwar.gorecore.util;

import java.util.UUID;

import io.netty.buffer.ByteBuf;

/**
 * Contains utility methods for reading and writing to ByteBufs.
 * 
 * @author CrowsOfWar
 */
public final class GoreCoreByteBufUtil {
	
	public static String readString(ByteBuf buf) {
		String res = "";
		int length = buf.readInt();
		for (int i = 0; i < length; i++) {
			res += buf.readChar();
		}
		return res;
	}
	
	public static void writeString(ByteBuf buf, String str) {
		char[] chs = str.toCharArray();
		buf.writeInt(chs.length);
		for (int i = 0; i < chs.length; i++) {
			buf.writeChar(chs[i]);
		}
	}
	
	public static UUID readUUID(ByteBuf buf) {
		return new UUID(buf.readLong(), buf.readLong());
	}
	
	public static void writeUUID(ByteBuf buf, UUID uuid) {
		buf.writeLong(uuid.getMostSignificantBits());
		buf.writeLong(uuid.getLeastSignificantBits());
	}
	
}
