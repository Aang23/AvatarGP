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
package com.crowsofwar.gorecore.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a property where the type is unknown. The type must be assumed based on the
 * configuration file. Use {@link #as(ObjectLoader)} or a similar method to find the actual value by
 * casting.
 * 
 * @author CrowsOfWar
 */
public class UnknownTypeProperty {
	
	private final Object object;
	private final String name;
	
	public UnknownTypeProperty(String name, Object obj) {
		this.name = name;
		this.object = obj;
	}
	
	public Object getObject() {
		return object;
	}
	
	public <T> T as(ObjectLoader<T> factory) {
		if (!(object instanceof Map)) throw new ConfigException(name + " isn't a Dictionary");
		return factory.load(new Configuration((Map) object));
	}
	
	public String asString() {
		return object.toString();
	}
	
	public int asInt() {
		try {
			return Integer.valueOf(object + "");
		} catch (NumberFormatException e) {
			throw new ConfigException(name + " isn't an integer");
		}
	}
	
	public boolean asBoolean() {
		try {
			return (boolean) object;
		} catch (ClassCastException e) {
			throw new ConfigException(name + " isn't a boolean");
		}
	}
	
	public double asDouble() {
		try {
			return Double.valueOf(object + "");
		} catch (NumberFormatException e) {
			throw new ConfigException(name + " isn't a double");
		}
	}
	
	public <T> List<T> asList(ListLoader<T> factory) {
		if (!(object instanceof List)) throw new ConfigException(name + " isn't a List");
		
		List<T> out = new ArrayList<>();
		
		List<?> list = (List<?>) object;
		for (Object obj : list) {
			out.add(factory.load(obj));
		}
		
		return out;
	}
	
	public List<String> asStringList() {
		return asList(obj -> (String) obj);
	}
	
	public Configuration asMapping() {
		return new Configuration((Map) object);
	}
	
}
