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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class FormattedMessage {
	
	private final String translateKey;
	private final String[] translateArgs;
	private final MessageConfiguration config;
	
	private FormattedMessage(MessageConfiguration config, String translateKey, String... translateArgs) {
		this.translateKey = translateKey;
		this.translateArgs = translateArgs;
		this.config = config;
	}
	
	public ITextComponent getChatMessage(boolean plaintext, Object... formatValues) {
		
		if (plaintext) {
			@SuppressWarnings("deprecation")
			String unformatted = I18n.translateToLocal(translateKey);
			String formatted = FormattedMessageProcessor.formatPlaintext(this, unformatted, formatValues);
			return new TextComponentString(formatted);
		} else {
			return new TextComponentTranslation(translateKey, formatValues);
		}
		
	}
	
	public void send(ICommandSender sender, Object... formatValues) {
		sender.sendMessage(getChatMessage(!(sender instanceof Entity), formatValues));
	}
	
	public String getTranslateKey() {
		return translateKey;
	}
	
	public String[] getTranslationArgs() {
		return translateArgs;
	}
	
	public MultiMessage chain() {
		return new MultiMessage().add(this);
	}
	
	public MessageConfiguration getConfig() {
		return config;
	}
	
	/**
	 * Creates a new chat message with the given translation key and formatting
	 * names.
	 * 
	 * @param translateKey
	 * @param translateArgs
	 * @return
	 */
	public static FormattedMessage newChatMessage(String translateKey, String... translateArgs) {
		return newChatMessage(MessageConfiguration.DEFAULT, translateKey, translateArgs);
	}
	
	/**
	 * Creates a new chat message with the given translation key, formatting
	 * names, and configurations.
	 * 
	 * @param config
	 * @param translateKey
	 * @param translateArgs
	 * @return
	 */
	public static FormattedMessage newChatMessage(MessageConfiguration config, String translateKey,
			String... translateArgs) {
		FormattedMessage cm = new FormattedMessage(config, translateKey, translateArgs);
		ChatSender.translateKeyToChatMessage.put(translateKey, cm);
		return cm;
	}
	
}
