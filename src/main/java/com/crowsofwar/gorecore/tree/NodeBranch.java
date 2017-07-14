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

import java.util.List;

import com.crowsofwar.gorecore.format.FormattedMessage;
import com.crowsofwar.gorecore.tree.TreeCommandException.Reason;

public class NodeBranch implements ICommandNode {
	
	private final ICommandNode[] nodes;
	private final IArgument<String> argName;
	private final IArgument<?>[] args;
	private final String name;
	private final FormattedMessage infoMessage;
	
	public NodeBranch(FormattedMessage infoMessage, String name, ICommandNode... nodes) {
		this.nodes = nodes;
		// this.argName = new ArgumentDirect<String>("node-name",
		// ITypeConverter.CONVERTER_STRING);
		String[] possibilities = new String[nodes.length];
		for (int i = 0; i < possibilities.length; i++)
			possibilities[i] = nodes[i].getNodeName();
		this.argName = new ArgumentOptions<String>(ITypeConverter.CONVERTER_STRING, "node-name",
				possibilities);
		this.args = new IArgument<?>[] { argName };
		this.name = name;
		this.infoMessage = infoMessage;
	}
	
	@Override
	public ICommandNode execute(CommandCall call, List<String> options) {
		ArgumentList args = call.popArguments(this);
		String name = args.get(argName);
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getNodeName().equals(name)) return nodes[i];
		}
		throw new TreeCommandException(Reason.NO_BRANCH_NODE, name, getHelp());
	}
	
	@Override
	public boolean needsOpPermission() {
		return false;
	}
	
	@Override
	public String getNodeName() {
		return name;
	}
	
	@Override
	public IArgument<?>[] getArgumentList() {
		return args;
	}
	
	@Override
	public String getHelp() {
		return getNodeName() + " " + argName.getHelpString();
	}
	
	public ICommandNode[] getSubNodes() {
		return nodes;
	}
	
	@Override
	public FormattedMessage getInfoMessage() {
		return infoMessage;
	}
	
}
