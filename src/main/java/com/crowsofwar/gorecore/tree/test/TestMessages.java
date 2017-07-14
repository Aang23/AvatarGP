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
package com.crowsofwar.gorecore.tree.test;

import static com.crowsofwar.gorecore.format.FormattedMessage.newChatMessage;

import com.crowsofwar.gorecore.format.FormattedMessage;
import com.crowsofwar.gorecore.format.MessageConfiguration;

import net.minecraft.util.text.TextFormatting;

public class TestMessages {
	
	// @formatter:off
	public static final MessageConfiguration CFG = new MessageConfiguration()
			.addColor("special", TextFormatting.LIGHT_PURPLE)
			.addConstant("const", "This_is_a_constant");
	public static final FormattedMessage MSG_VIDEOGAME_HELP = newChatMessage(CFG, "test.buyVideogames.help");
	public static final FormattedMessage MSG_CAKE_FROST_HELP = newChatMessage(CFG, "test.frostCake.help");
	public static final FormattedMessage MSG_CAKE_LICK_HELP = newChatMessage(CFG, "test.lickCake.help");
	public static final FormattedMessage MSG_PLAYVIDEOGAMES_HELP = newChatMessage(CFG, "test.videogames.help");
	public static final FormattedMessage MSG_CHATSENDER_HELP = newChatMessage(CFG, "test.chatSender.help");
	public static final FormattedMessage MSG_VIDEOGAME_BRANCH_HELP = newChatMessage(CFG, "test.videogamesBranch.help");
	public static final FormattedMessage MSG_FRUIT = newChatMessage(CFG, "test.chatSender", "fruit");
	public static final FormattedMessage MSG_CONST = newChatMessage(CFG, "test.const");
	
}
