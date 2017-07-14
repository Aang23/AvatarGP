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
package com.crowsofwar.avatar.client.uitools;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class StartingPosition {
	
	public static StartingPosition TOP_LEFT = new StartingPosition(0, 0, 0, 0);
	public static StartingPosition TOP_RIGHT = new StartingPosition(1, 0, 1, 0),
			TOP_CENTER = new StartingPosition(.5f, 0, .5f, 0),
			MIDDLE_TOP = new StartingPosition(.5f, 0, .5f, 0),
			MIDDLE_CENTER = new StartingPosition(.5f, .5f, .5f, .5f),
			MIDDLE_BOTTOM = new StartingPosition(.5f, 1, .5f, 1),
			BOTTOM_RIGHT = new StartingPosition(1, 1, 1, 1);
	
	private float x, y, minusX, minusY;
	
	private StartingPosition(float x, float y, float minusX, float minusY) {
		this.x = x;
		this.y = y;
		this.minusX = minusX;
		this.minusY = minusY;
	}
	
	/**
	 * Gets amount of x divided by the frame width (0-1)
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Gets amount of y divided by the frame height (0-1)
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * To achieve the desired x coordinate, the width times this number should
	 * be subtracted from the x position.
	 */
	public float getMinusX() {
		return minusX;
	}
	
	/**
	 * To achieve the desired y coordinate, the height times this number should
	 * be subtracted from the y position.
	 */
	public float getMinusY() {
		return minusY;
	}
	
	public static StartingPosition custom(float x, float y) {
		return new StartingPosition(x, y, .5f, .5f);
	}
	
	public static StartingPosition custom(float x, float y, float mx, float my) {
		return new StartingPosition(x, y, mx, my);
	}
	
}
