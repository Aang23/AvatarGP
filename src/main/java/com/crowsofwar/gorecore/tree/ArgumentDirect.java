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

import net.minecraft.command.ICommandSender;

/**
 * An argument that takes direct user input and converts it into a value. This
 * allows all possible values. Does not support tab completion.
 * 
 * @param <T>
 *            The type of value
 * 
 * @author CrowsOfWar
 */
public class ArgumentDirect<T> implements IArgument<T> {
	
	private final T defaultValue;
	private final ITypeConverter<T> converter;
	private final String name;
	
	public ArgumentDirect(String argumentName, ITypeConverter<T> converter) {
		this(argumentName, converter, null);
	}
	
	public ArgumentDirect(String argumentName, ITypeConverter<T> converter, T defaultValue) {
		this.defaultValue = defaultValue;
		this.converter = converter;
		this.name = argumentName;
	}
	
	@Override
	public boolean isOptional() {
		return defaultValue != null;
	}
	
	@Override
	public T getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public T convert(String input) {
		return converter.convert(input);
	}
	
	@Override
	public String getArgumentName() {
		return name;
	}
	
	@Override
	public String getHelpString() {
		String before = isOptional() ? "\\[" : "<";
		String after = isOptional() ? "]" : ">";
		return before + "any " + converter.getTypeName() + after;
	}
	
	@Override
	public String getSpecificationString() {
		String before = isOptional() ? "\\[" : "<";
		String after = isOptional() ? "]" : ">";
		return before + getArgumentName() + after;
	}
	
	@Override
	public List<String> getCompletionSuggestions(ICommandSender sender, String currentInput) {
		return new ArrayList<>();
	}
	
}
