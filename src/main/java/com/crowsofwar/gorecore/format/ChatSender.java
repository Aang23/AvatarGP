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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChatSender {
	
	public static final ChatSender instance;
	
	private static final Map<String, FormattedMessage> referenceToChatMessage;
	static final Map<String, FormattedMessage> translateKeyToChatMessage;
	
	/**
	 * Cause static block to be called
	 */
	public static void load() {}
	
	static {
		instance = new ChatSender();
		MinecraftForge.EVENT_BUS.register(instance);
		referenceToChatMessage = new HashMap<>();
		translateKeyToChatMessage = new HashMap<>();
	}
	
	private ChatSender() {}
	
	private Object[] getFormatArgs(TextComponentTranslation message) {
		return ObfuscationReflectionHelper.getPrivateValue(TextComponentTranslation.class, message, 1);
	}
	
	private String getKey(TextComponentTranslation message) {
		return ObfuscationReflectionHelper.getPrivateValue(TextComponentTranslation.class, message, 0);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void processClientChat(ClientChatReceivedEvent e) {
		if (e.getMessage() instanceof TextComponentTranslation) {
			
			TextComponentTranslation message = (TextComponentTranslation) e.getMessage();
			
			// Create a list containing the message and its siblings
			// siblings are messages that appear next to it, but can have
			// different formatting
			List<ITextComponent> comps = new ArrayList();
			Object[] formatArgs = getFormatArgs(message);
			comps.add(new TextComponentTranslation(getKey(message), formatArgs));
			comps.addAll(e.getMessage().getSiblings());
			
			// Format each chat component
			
			String result = "";
			
			for (ITextComponent chat : comps) {
				String processed = formatChatComponent(chat, false);
				if (processed != null) {
					result += processed;
				}
			}
			if (!result.isEmpty()) e.setMessage(new TextComponentTranslation(result));
			
		}
		
	}
	
	/**
	 * Formats the chat component by interpreting the tags. This is only done if
	 * there is a registered FormattedMessage associated with the chat key.
	 * Returns null if there isn't a FormattedMessage to use.
	 */
	private String formatChatComponent(ITextComponent chat, boolean plaintext) {
		
		if (chat instanceof TextComponentTranslation) {
			TextComponentTranslation translate = (TextComponentTranslation) chat;
			String key = getKey(translate);
			FormattedMessage cm = translateKeyToChatMessage.get(key);
			
			if (cm != null) {
				
				try {
					
					String text = translate.getUnformattedText().replaceAll("%", "%%");
					if (plaintext) {
						return FormattedMessageProcessor.formatPlaintext(cm, text, translate.getFormatArgs());
					} else {
						return FormattedMessageProcessor.formatText(cm, text, translate.getFormatArgs());
					}
					
				} catch (ProcessingException e) {
					e.printStackTrace();
					return "Error processing text; see log for details";
				}
				
			}
			
		}
		
		return null;
		
	}
	
	static class ProcessingException extends RuntimeException {
		
		public ProcessingException(String message) {
			super(message);
		}
		
	}
	
}
