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

import java.util.List;

import com.crowsofwar.gorecore.format.FormattedMessage;
import com.crowsofwar.gorecore.tree.CommandCall;
import com.crowsofwar.gorecore.tree.ICommandNode;
import com.crowsofwar.gorecore.tree.NodeFunctional;

import net.minecraft.util.text.TextComponentTranslation;

public class TestPlayVideogames extends NodeFunctional {
	
	public TestPlayVideogames() {
		super("play", false);
	}
	
	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		String videogame = options.isEmpty() ? "" : options.get(0);
		String send = options.isEmpty() ? "test.videogames.none" : "test.videogames";
		call.getFrom().sendMessage(new TextComponentTranslation(send, videogame));
		return null;
	}
	
	@Override
	public FormattedMessage getInfoMessage() {
		return TestMessages.MSG_PLAYVIDEOGAMES_HELP;
	}
	
}
