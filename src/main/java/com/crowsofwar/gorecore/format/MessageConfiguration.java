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
package com.crowsofwar.gorecore.format;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.text.TextFormatting;

public class MessageConfiguration {
	
	public static final MessageConfiguration DEFAULT = new MessageConfiguration();
	
	private final Map<String, TextFormatting> colors;
	/**
	 * Constants are variables which are applied to all ChatMessages using this
	 * MessageConfiguration. They are applied once to the MessageConfiguration,
	 * then every ChatMessage that uses this configuration will receive those
	 * constants as variables. Constants are used in the same way as
	 * message-specific formatting arguments:
	 * <code>Constant: ${const_name}</code>.
	 */
	private final Map<String, String> constants;
	
	public MessageConfiguration() {
		this.colors = new HashMap<String, TextFormatting>();
		this.constants = new HashMap<>();
	}
	
	public MessageConfiguration addColor(String reference, TextFormatting color) {
		if (!color.isColor()) throw new IllegalArgumentException("The chat formatting must be a color");
		this.colors.put(reference, color);
		return this;
	}
	
	public TextFormatting getColor(String reference) {
		return colors.get(reference);
	}
	
	public String getColorName(String reference) {
		if (hasColor(reference))
			return getColor(reference).name().toLowerCase();
		else
			return null;
	}
	
	public boolean hasColor(String reference) {
		return colors.containsKey(reference);
	}
	
	public Map<String, TextFormatting> allColors() {
		return new HashMap<>(colors);
	}
	
	/**
	 * Add a constant to this message configuration.
	 * <p>
	 * Constants are variables which are applied to all ChatMessages using this
	 * MessageConfiguration. They are applied once to the MessageConfiguration,
	 * then every ChatMessage that uses this configuration will receive those
	 * constants as variables. Constants are used in the same way as
	 * message-specific formatting arguments:
	 * <code>Constant: ${const_name}</code>.
	 * 
	 * @param name
	 *            The name of the constant
	 * @param value
	 *            The value assigned to the constant
	 * @return this
	 */
	public MessageConfiguration addConstant(String name, String value) {
		this.constants.put(name, value);
		return this;
	}
	
	/**
	 * Returns a set of the constants.
	 * <p>
	 * Each element in the set is an entry which maps constant name -> constant
	 * value.
	 */
	public Set<Map.Entry<String, String>> getAllConstants() {
		return Collections.unmodifiableSet(constants.entrySet());
	}
	
}
