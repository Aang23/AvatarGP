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
package com.crowsofwar.gorecore.tree;

import net.minecraft.command.ICommandSender;

import java.util.Arrays;

public class CommandCall {

	private ICommandSender from;
	private boolean isOp;
	private String[] passedArgs;
	private int argumentIndex;
	
	public CommandCall(ICommandSender from, String[] passedArgs) {
		this.from = from;
		this.passedArgs = passedArgs;
		this.argumentIndex = 0;
		
		// TODO find a better way to find out if Command sender is operator
		isOp = from.canUseCommand(2, "gamemode");
		
	}
	
	public ArgumentList popArguments(ICommandNode node) {
		IArgument<?>[] arguments = node.getArgumentList();
		String[] poppedArray = Arrays.copyOfRange(passedArgs, argumentIndex, passedArgs.length);
		argumentIndex += arguments.length;
		return new ArgumentList(poppedArray, arguments);
	}
	
	public int getArgumentsLeft() {
		return passedArgs.length - argumentIndex;
	}
	
	public boolean isOpped() {
		return isOp;
	}
	
	public ICommandSender getFrom() {
		return from;
	}
	
}
