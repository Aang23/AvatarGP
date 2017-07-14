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
package com.crowsofwar.gorecore.format;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class MultiMessage {
	
	private final List<FormattedMessage> chatMessages;
	private final List<Object[]> formattingArgs;
	
	MultiMessage() {
		this.chatMessages = new ArrayList<FormattedMessage>();
		this.formattingArgs = new ArrayList<Object[]>();
	}
	
	public MultiMessage add(FormattedMessage message, Object... formattingArgs) {
		this.chatMessages.add(message);
		this.formattingArgs.add(formattingArgs);
		return this;
	}
	
	public List<FormattedMessage> getChatMessages() {
		return chatMessages;
	}
	
	public void send(ICommandSender sender) {
		if (chatMessages.isEmpty()) throw new IllegalArgumentException("Cannot send empty MultiMessage");
		boolean plaintext = !(sender instanceof Entity);
		ITextComponent send = null;
		for (int i = 0; i < chatMessages.size(); i++) {
			FormattedMessage message = chatMessages.get(i);
			if (send == null) {
				send = message.getChatMessage(plaintext, formattingArgs.get(i));
			} else {
				send.appendSibling(message.getChatMessage(plaintext, formattingArgs.get(i)));
			}
		}
		
		sender.sendMessage(send);
	}
	
	public List<Object[]> getFormattingArgs() {
		return formattingArgs;
	}
	
}
