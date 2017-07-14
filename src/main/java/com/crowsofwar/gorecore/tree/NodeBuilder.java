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
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.command.ICommandSender;

/**
 * Allows quick creation of a NodeFunctional at runtime via the builder pattern
 * and lambdas.
 * 
 * @author CrowsOfWar
 */
public class NodeBuilder {
	
	private final String name;
	private boolean op;
	private final List<IArgument> args;
	
	public NodeBuilder(String name) {
		this.name = name;
		this.args = new ArrayList<>();
	}
	
	public NodeBuilder addArgument(IArgument argument) {
		this.args.add(argument);
		return this;
	}
	
	public NodeBuilder addArgumentDirect(String argumentName, ITypeConverter<?> converter) {
		return addArgument(new ArgumentDirect<>(argumentName, converter));
	}
	
	public <T> NodeBuilder addArgumentDirect(String argumentName, ITypeConverter<T> converter,
			T defaultValue) {
		return addArgument(new ArgumentDirect<>(argumentName, converter, defaultValue));
	}
	
	public NodeBuilder addArgumentPlayer(String argumentName) {
		return addArgument(new ArgumentPlayerName(argumentName));
	}
	
	public NodeFunctional build(Consumer<ArgPopper> action) {
		NodeFunctional node = new NodeFunctional(name, op) {
			@Override
			protected ICommandNode doFunction(CommandCall call, List<String> options) {
				ArgumentList argList = call.popArguments(this);
				action.accept(new ArgPopper(argList, args, call));
				return null;
			}
		};
		for (IArgument arg : args)
			node.addArgument(arg);
		
		return node;
	}
	
	public static class ArgPopper {
		
		private ArgumentList values;
		private List<IArgument> args;
		private int nextArg;
		private final CommandCall call;
		
		public ArgPopper(ArgumentList values, List<IArgument> args, CommandCall call) {
			this.values = values;
			this.args = args;
			this.nextArg = 0;
			this.call = call;
		}
		
		//@formatter:off
		/**
		 * Get the next argument from this popper. Arguments must be popped in
		 * the same order they were declared.
		 * <p>
		 * Example:
		 * 
		 * <pre>
		 * new NodeBuilder("sendMsg")
		 * 		.addArgumentPlayer("player")
		 * 		.addArgumentDirect("msg", ITypeConverter.CONVERTER_STRING)
		 * 		.build(popper -> {
		 * 			// Must use exact same order as used in addArgument methods
		 * 			String playerName = popper.get();
		 * 			String message = popper.get();
		 * 		});
		 * </pre>
		 */
		//@formatter:on
		public <T> T get() {
			if (nextArg >= args.size()) throw new IndexOutOfBoundsException("Ran out of arguments to pop");
			IArgument<T> key = args.get(nextArg++);
			return values.get(key);
		}
		
		public ICommandSender from() {
			return call.getFrom();
		}
		
	}
	
}
