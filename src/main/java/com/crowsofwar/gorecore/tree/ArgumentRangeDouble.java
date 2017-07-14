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
 * 
 * 
 * @author CrowsOfWar
 */
public class ArgumentRangeDouble implements IArgument<Double> {
	
	private final double defaultValue;
	private final boolean optional;
	private final double min;
	private final double max;
	private final String name;
	
	public ArgumentRangeDouble(String name, double min, double max) {
		this.name = name;
		this.defaultValue = 0;
		this.optional = false;
		this.min = min;
		this.max = max;
	}
	
	public ArgumentRangeDouble(String name, double min, double max, double defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.optional = true;
		this.min = min;
		this.max = max;
	}
	
	@Override
	public boolean isOptional() {
		return optional;
	}
	
	@Override
	public Double getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public Double convert(String input) {
		double value = ITypeConverter.CONVERTER_DOUBLE.convert(input);
		if (value < min || value > max) {
			throw new TreeCommandException("gc.tree.error.rangeDouble", name, min, max);
		}
		return value;
	}
	
	@Override
	public String getArgumentName() {
		return name;
	}
	
	@Override
	public String getHelpString() {
		char open = isOptional() ? '[' : '<';
		char close = isOptional() ? ']' : '>';
		return open + "any number " + min + "-" + max + close;
	}
	
	@Override
	public String getSpecificationString() {
		char open = isOptional() ? '[' : '<';
		char close = isOptional() ? ']' : '>';
		return open + name + close;
	}
	
	@Override
	public List<String> getCompletionSuggestions(ICommandSender sender, String currentInput) {
		return new ArrayList<>();
	}
	
}
