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
package com.crowsofwar.gorecore.util;

import java.util.Random;

/**
 * Provides a variety of numerical functions related to math and random numbers.
 * 
 * @author CrowsOfWar
 */
public final class GoreCoreMathHelper {
	
	/**
	 * Clamp the double to the given range.
	 * 
	 * @param n
	 *            The number to clamp
	 * @param min
	 *            The minimum number in that range
	 * @param max
	 *            The maximum number in that range
	 * @return The clamped number
	 */
	public static double clampDouble(double n, double min, double max) {
		if (n < min) n = min;
		if (n > max) n = max;
		return n;
	}
	
	/**
	 * Round the given number to the nearest whole.
	 * 
	 * @param n
	 *            The number
	 * @return The number, rounded to the nearest integer
	 */
	public static int round(double n) {
		return (int) (n + 0.5);
	}
	
	/**
	 * Round the given number to the nearest whole that is smaller than the
	 * number.
	 * 
	 * @param n
	 *            The number
	 * @return The number, rounded to the nearest smaller integer
	 */
	public static int roundDown(double n) {
		return (int) n;
	}
	
	/**
	 * Round the given number to the nearest whole that is greater than the
	 * number.
	 * 
	 * @param n
	 *            The number
	 * @return The number, rounded to the nearest greater integer
	 */
	public static int roundUp(double n) {
		return roundDown(n) + 1;
	}
	
	public static final Random random = new Random();
	
	/**
	 * Generate a random boolean.
	 */
	public static boolean randomBoolean() {
		return random.nextBoolean();
	}
	
	/**
	 * Generate a double in the given range.
	 * 
	 * @param min
	 *            The minimum number to generate
	 * @param max
	 *            The maximum number to generate
	 * @return A random double in the given range
	 */
	public static double randomDouble(double min, double max) {
		return min + random.nextDouble() * (max - min);
	}
	
	/**
	 * Generate a float in the given range.
	 * 
	 * @param min
	 *            The minimum number to generate
	 * @param max
	 *            The maximum number to generate
	 * @return A random float in the given range
	 */
	public static float randomFloat(float min, float max) {
		return min + random.nextFloat() * (max - min);
	}
	
	/**
	 * Generate an integer in the given range.
	 * 
	 * @param min
	 *            The minimum number to generate
	 * @param max
	 *            The maximum number to generate
	 * @return A random integer in the given range
	 */
	public static int randomInt(int min, int max) {
		return min + random.nextInt(max - min);
	}
	
	/**
	 * Generate a long in the given range.
	 * 
	 * @param min
	 *            The minimum number to generate
	 * @param max
	 *            The maximum number to generate
	 * @return A random long in the given range
	 */
	public static long randomLong(long min, long max) {
		return min + random.nextLong() * (max - min);
	}
	
}
