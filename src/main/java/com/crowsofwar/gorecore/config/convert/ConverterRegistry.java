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
package com.crowsofwar.gorecore.config.convert;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages all {@link Converter converters} by keeping them in memory.
 * Internally uses a Map using {@link Pair} as the key.
 * 
 * @author CrowsOfWar
 */
public class ConverterRegistry {
	
	private static final Map<Pair, Converter> converters = new HashMap<>();
	
	public static <F, T> void addConverter(Class<F> from, Class<T> to, Converter<F, T> convert) {
		Pair pair = Pair.of(from, to);
		converters.put(pair, convert);
	}
	
	public static <F, T> Converter<F, T> getConverter(Class<F> from, Class<T> to) {
		Pair pair = Pair.of(from, to);
		return converters.get(pair);
	}
	
	/**
	 * Returns whether there is a converter to convert.
	 */
	public static boolean isConverter(Class<?> from, Class<?> to) {
		if (!Pair.exists(from, to)) return false;
		Pair pair = Pair.of(from, to);
		return converters.containsKey(pair);
	}
	
	public static void addDefaultConverters() {
		addConverter(Integer.class, Double.class, integer -> integer.doubleValue());
		addConverter(Double.class, Integer.class, dubbl -> dubbl.intValue());
		addConverter(Integer.class, String.class, integer -> integer + "");
		addConverter(Double.class, String.class, dubbl -> dubbl + "");
		addConverter(Integer.class, Float.class, integer -> integer.floatValue());
		addConverter(Float.class, Integer.class, floatt -> floatt.intValue());
		addConverter(Double.class, Float.class, dubbl -> dubbl.floatValue());
		addConverter(Float.class, Double.class, floatt -> floatt.doubleValue());
	}
	
}
