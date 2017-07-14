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

import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.network.PacketRedirector;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class PacketSSkillsMenu extends AvatarPacket<PacketSSkillsMenu> {
	
	private int element;
	
	public PacketSSkillsMenu() {}
	
	public PacketSSkillsMenu(BendingType element) {
		this.element = element.id();
	}
	
	@Override
	public void avatarFromBytes(ByteBuf buf) {
		element = buf.readInt();
	}
	
	@Override
	public void avatarToBytes(ByteBuf buf) {
		buf.writeInt(element);
	}
	
	@Override
	protected Side getRecievedSide() {
		return Side.SERVER;
	}
	
	@Override
	protected com.crowsofwar.avatar.common.network.packets.AvatarPacket.Handler<PacketSSkillsMenu> getPacketHandler() {
		return PacketRedirector::redirectMessage;
	}
	
	public int getElement() {
		return element;
	}
	
}
