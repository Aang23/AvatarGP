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

/**
 * A pair of 2 {@link Type types}. Intended for use as a key in a map -
 * generally useless otherwise.
 * 
 * @author CrowsOfWar
 */
public class Pair {
	
	private final Type typeA, typeB;
	
	private Pair(Type typeA, Type typeB) {
		this.typeA = typeA;
		this.typeB = typeB;
	}
	
	/**
	 * Returns a TypePair which has the same classes as the given ones.
	 */
	public static Pair of(Class<?> clsA, Class<?> clsB) {
		if (clsA == null || clsB == null) {
			throw new ConversionException("Cannot create a pair with a null class");
		}
		return new Pair(Type.of(clsA), Type.of(clsB));
	}
	
	/**
	 * Returns whether a pair exists for the combination of those two types
	 */
	public static boolean exists(Class<?> clsA, Class<?> clsB) {
		return Type.exists(clsA) && Type.exists(clsB);
	}
	
	@Override
	public int hashCode() {
		return typeA.id() + typeB.id(); // is unique because ids are exps. of 2
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Pair)) return false;
		Pair pair = (Pair) obj;
		return pair.typeA == this.typeA && pair.typeB == this.typeB;
	}
	
}
