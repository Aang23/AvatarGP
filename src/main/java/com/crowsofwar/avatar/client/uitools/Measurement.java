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

import static com.crowsofwar.avatar.client.uitools.ScreenInfo.screenHeight;
import static com.crowsofwar.avatar.client.uitools.ScreenInfo.screenWidth;

/**
 * A measurement that can either be in screen pixels or percentage of total
 * screen. Measurements keep track of x and y values, and are immutable.
 * 
 * @author CrowsOfWar
 */
public class Measurement {
	
	private final Frame frame;
	private final float x, y;
	
	private Measurement(Frame frame, float x, float y) {
		this.frame = frame;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x-value of the measurement in pixels.
	 */
	public float xInPixels() {
		return x;
	}
	
	/**
	 * Returns the y-value of the measurement in pixels.
	 */
	public float yInPixels() {
		return y;
	}
	
	/**
	 * Returns the x-value of the measurement in percentage of screen width from
	 * 0-100.
	 */
	public float xInPercent() {
		return x / screenWidth() * 100;
	}
	
	/**
	 * Returns the y-value of the measurement in percentage of screen height
	 * from 0-100.
	 */
	public float yInPercent() {
		return y / screenHeight() * 100;
	}
	
	/**
	 * Returns a new measurement scaled by the given factor.
	 */
	public Measurement times(float scl) {
		return new Measurement(frame, x * scl, y * scl);
	}
	
	/**
	 * Returns a new measurement based off of this coordinates plus the other
	 * coordinates.
	 */
	public Measurement plus(Measurement m) {
		return new Measurement(frame, this.x + m.x, this.y + m.y);
	}
	
	public static Measurement fromPixels(float x, float y) {
		return fromPixels(Frame.SCREEN, x, y);
	}
	
	public static Measurement fromPixels(Frame frame, float x, float y) {
		return new Measurement(frame, x, y);
	}
	
	public static Measurement fromPercent(float pctX, float pctY) {
		return fromPercent(Frame.SCREEN, pctX, pctY);
	}
	
	/**
	 * Percent is from 0-100.
	 */
	public static Measurement fromPercent(Frame frame, float pctX, float pctY) {
		Measurement dim = frame.getDimensions();
		return new Measurement(frame, dim.x * pctX / 100, dim.y * pctY / 100);
	}
	
}
