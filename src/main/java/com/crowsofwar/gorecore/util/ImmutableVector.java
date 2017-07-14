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

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ImmutableVector extends Vector {
	
	private final double x, y, z;
	
	/**
	 * Create the zero vector.
	 */
	public ImmutableVector() {
		this(0, 0, 0);
	}
	
	/**
	 * Creates using the coordinates (x, y, z).
	 * 
	 * @param x
	 *            X-position of the new vector
	 * @param y
	 *            Y-position of the new vector
	 * @param z
	 *            Z-position of the new vector
	 */
	public ImmutableVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates an immutable copy of the given vector.
	 * 
	 * @param vec
	 *            Vector to copy
	 */
	public ImmutableVector(Vector vec) {
		this.x = vec.x();
		this.y = vec.y();
		this.z = vec.z();
	}
	
	/**
	 * Creates an immutable copy of the given Minecraft vector.
	 * 
	 * @param vec
	 *            Vector to copy
	 */
	public ImmutableVector(Vec3d vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	/**
	 * Creates a vector from the feet position of the given entity.
	 * 
	 * @param entity
	 *            The entity to use
	 */
	public ImmutableVector(Entity entity) {
		this.x = entity.posX;
		this.y = entity.posY;
		this.z = entity.posZ;
	}
	
	/**
	 * Creates a vector from the coordinates defined by blockPos.
	 * 
	 * @param blockPos
	 *            The vanilla blockPos
	 */
	public ImmutableVector(BlockPos blockPos) {
		this.x = blockPos.getX();
		this.y = blockPos.getY();
		this.z = blockPos.getZ();
	}
	
	public ImmutableVector(Vec3i vec) {
		this.x = vec.getX();
		this.y = vec.getY();
		this.z = vec.getZ();
	}
	
	public ImmutableVector(EnumFacing facing) {
		this(facing.getDirectionVec());
	}
	
	@Override
	public double x() {
		return x;
	}
	
	@Override
	public double y() {
		return y;
	}
	
	@Override
	public double z() {
		return z;
	}
	
	@Override
	public Vector setX(double x) {
		throw new UnsupportedOperationException("Cannot modify immutable vectors");
	}
	
	@Override
	public Vector setY(double y) {
		throw new UnsupportedOperationException("Cannot modify immutable vectors");
	}
	
	@Override
	public Vector setZ(double z) {
		throw new UnsupportedOperationException("Cannot modify immutable vectors");
	}
	
}
