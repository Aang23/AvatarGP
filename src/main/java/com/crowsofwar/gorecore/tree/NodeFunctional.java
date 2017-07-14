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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.crowsofwar.gorecore.format.FormattedMessage;

import net.minecraft.util.text.TextComponentTranslation;

/**
 * A very customizable implementation of {@link ICommandNode}. This is designed
 * to simplify the development of new command nodes by implementing many of
 * ICommandNode's methods.
 * 
 * @author CrowsOfWar
 */
public abstract class NodeFunctional implements ICommandNode {
	
	private static final FormattedMessage DEFAULT_INFO;
	static {
		DEFAULT_INFO = FormattedMessage.newChatMessage("gc.tree.node.defaultInfo");
	}
	
	private final String name;
	private final boolean op;
	private List<IArgument> args;
	
	public NodeFunctional(String name, boolean op) {
		this.name = name;
		this.op = op;
		this.args = new ArrayList<>();
	}
	
	protected <T extends IArgument<?>> T addArgument(T argument) {
		this.args.add(argument);
		return argument;
	}
	
	protected void addArguments(IArgument<?>... arguments) {
		this.args.addAll(Arrays.asList(arguments));
	}
	
	@Override
	public final boolean needsOpPermission() {
		return op;
	}
	
	@Override
	public final String getNodeName() {
		return name;
	}
	
	@Override
	public final IArgument<?>[] getArgumentList() {
		IArgument<?>[] arr = new IArgument[args.size()];
		return args.toArray(arr);
	}
	
	@Override
	public String getHelp() {
		String out = getNodeName();
		IArgument<?>[] args = getArgumentList();
		for (int i = 0; i < args.length; i++) {
			out += " " + args[i].getSpecificationString();
		}
		return out;
	}
	
	@Override
	public final ICommandNode execute(CommandCall call, List<String> options) {
		if (options.contains("help")) {
			call.getFrom()
					.sendMessage(new TextComponentTranslation("gc.tree.help", getHelp(), getNodeName()));
			return null;
		} else {
			return doFunction(call, options);
		}
	}
	
	protected abstract ICommandNode doFunction(CommandCall call, List<String> options);
	
	@Override
	public FormattedMessage getInfoMessage() {
		return DEFAULT_INFO;
	}
	
}
