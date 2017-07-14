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

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A vector which is adds Consumers in a way that allows clients to create a
 * view of another vector.
 * <p>
 * In a backed vector, the vector's fields x, y, and z have become obsolete.
 * Instead, functions provide the x, y, and z when they are retrieved, and enact
 * necessary behavior when the components are set.
 * 
 * @author CrowsOfWar
 */
public class BackedVector extends Vector {
	
	private final Consumer<Double> setX, setY, setZ;
	private final Supplier<Double> getX, getY, getZ;
	
	/**
	 * Create a backed vector.
	 * 
	 * @param setX
	 *            Called when X was set. Use this to modify the underlying
	 *            vector.
	 * @param setY
	 *            Called when Y was set. Use this to modify the underlying
	 *            vector.
	 * @param setZ
	 *            Called when Z was set. Use this to modify the underlying
	 *            vector.
	 * @param getX
	 *            Called to retrieve the x-value of the vector
	 * @param getY
	 *            Called to retrieve the y-value of the vector
	 * @param getZ
	 *            Called to retrieve the z-value of the vector
	 */
	public BackedVector(Consumer<Double> setX, Consumer<Double> setY, Consumer<Double> setZ,
			Supplier<Double> getX, Supplier<Double> getY, Supplier<Double> getZ) {
		this.setX = setX;
		this.setY = setY;
		this.setZ = setZ;
		this.getX = getX;
		this.getY = getY;
		this.getZ = getZ;
	}
	
	@Override
	public double x() {
		return getX.get();
	}
	
	@Override
	public double y() {
		return getY.get();
	}
	
	@Override
	public double z() {
		return getZ.get();
	}
	
	@Override
	public Vector setX(double x) {
		super.setX(x);
		setX.accept(x);
		return this;
	}
	
	@Override
	public Vector setY(double y) {
		super.setY(y);
		setY.accept(y);
		return this;
	}
	
	@Override
	public Vector setZ(double z) {
		super.setZ(z);
		setZ.accept(z);
		return this;
	}
	
	@Override
	public Vector set(double x, double y, double z) {
		setX(x);
		setY(y);
		setZ(z);
		return this;
	}
	
}
