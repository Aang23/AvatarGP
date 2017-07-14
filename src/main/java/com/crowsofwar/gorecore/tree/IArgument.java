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

import net.minecraft.command.ICommandSender;

/**
 * Represents an argument for a command node.
 * <p>
 * Is responsible for converting the values of the argument to/from Strings.
 * 
 * @param <T>
 *            Type of the argument
 * 
 * @author CrowsOfWar
 */
public interface IArgument<T> {
	
	/**
	 * Returns whether this argument can be omitted for the command node to
	 * still work.
	 */
	boolean isOptional();
	
	/**
	 * Gets the default value of this argument. Null if not optional.
	 */
	T getDefaultValue();
	
	/**
	 * Return a value based off of the given input. The input's case should be
	 * ignored.
	 * 
	 * @param input
	 *            Input string
	 */
	T convert(String input);
	
	/**
	 * Get the name of this argument to show up in the help pages
	 */
	String getArgumentName();
	
	/**
	 * Format the argument to show the accepted values of the argument. e.g.
	 * &lt;ON|OFF>
	 */
	String getHelpString();
	
	/**
	 * Show a description of what this argument represents. e.g. [duration]
	 */
	String getSpecificationString();
	
	/**
	 * Gets a list of tab completion suggestions while the player is typing an
	 * argument. The first item on the list is the item which will be
	 * auto-completed.
	 * <p>
	 * If there are no suggestions, returns an empty list (doesn't returns
	 * null).
	 * 
	 * @param sender
	 *            Player who is typing the message
	 * @param currentInput
	 *            What is typed so far for the argument
	 */
	List<String> getCompletionSuggestions(ICommandSender sender, String currentInput);
	
}
