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

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class GoreCoreChatMessages {
	
	public static void register() {}
	
	public static final MessageConfiguration CFG = new MessageConfiguration().addColor("value",
			TextFormatting.GOLD);
	public static final FormattedMessage MSG_FIXID_SUCCESS = newChatMessage(CFG, "gc.cmd.fixid.success", "player");
	public static final FormattedMessage MSG_FIXID_FAILURE = newChatMessage(CFG, "gc.cmd.fixid.failure", "player");
	public static final FormattedMessage MSG_FIXID_ONLINE = newChatMessage(CFG, "gc.cmd.fixid.online", "player");
	public static final FormattedMessage MSG_FIXID_CONFIRM = newChatMessage(CFG, "gc.cmd.fixid.confirm", "player");
	public static final FormattedMessage MSG_FIXID_CONFIRM2 = newChatMessage(CFG, "gc.cmd.fixid.confirm2",
			"player");
	
}
