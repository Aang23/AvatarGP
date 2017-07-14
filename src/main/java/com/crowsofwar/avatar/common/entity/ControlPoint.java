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
package com.crowsofwar.avatar.common.entity;

import com.crowsofwar.gorecore.util.ImmutableVector;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * A control point in an arc.
 * <p>
 * An arc is made up of multiple control points. This allows the arc to twist
 * and turn. Segments are drawn in-between control points, which creates a
 * blocky arc.
 * 
 * @author CrowsOfWar
 */
public class ControlPoint {
	
	protected EntityArc arc;
	protected EntityLivingBase owner;
	
	private final Vector internalVelocity;
	private final Vector internalPosition;
	
	private ImmutableVector lastPos;
	
	private final World world;
	private AxisAlignedBB hitbox;
	
	protected float size;
	
	public ControlPoint(EntityArc arc, float size, double x, double y, double z) {
		internalPosition = new Vector();
		internalVelocity = new Vector();
		this.arc = arc;
		this.world = arc.world;
		this.size = size;
		
		double sizeHalfed = size / 2;
		hitbox = new AxisAlignedBB(position().x() - sizeHalfed, position().y() - sizeHalfed,
				position().z() - sizeHalfed, position().x() + sizeHalfed, position().y() + sizeHalfed,
				position().z() + sizeHalfed);
		
		lastPos = new ImmutableVector();
		
	}
	
	/**
	 * Get the velocity of this entity in m/s. Changes to this vector will be
	 * reflected in the entity's actual velocity.
	 */
	public Vector velocity() {
		return internalVelocity;
	}
	
	/**
	 * Get the position of this entity. Changes to this vector will be reflected
	 * in the entity's actual position.
	 */
	public Vector position() {
		return internalPosition;
	}
	
	public Vector lastPosition() {
		return lastPos;
	}
	
	public double x() {
		return position().x();
	}
	
	public double y() {
		return position().y();
	}
	
	public double z() {
		return position().z();
	}
	
	/**
	 * Remove the control point's arc.
	 */
	public void setDead() {
		arc.setDead();
	}
	
	public AxisAlignedBB getBoundingBox() {
		return hitbox;
	}
	
	public float size() {
		return size;
	}
	
	public void onUpdate() {
		
		double sizeHalfed = size / 2;
		hitbox = new AxisAlignedBB(position().x() - sizeHalfed, position().y() - sizeHalfed,
				position().z() - sizeHalfed, position().x() + sizeHalfed, position().y() + sizeHalfed,
				position().z() + sizeHalfed);
		
		lastPos = new ImmutableVector(position());
		
		position().add(velocity().times(0.05));
		velocity().mul(0.4);
		
	}
	
	/**
	 * @deprecated Use {@link #position()}.{@link Vector#set(Vector) set(pos)}
	 */
	@Deprecated
	public void setVecPosition(Vector pos) {
		position().set(pos);
	}
	
	/**
	 * Move this control point by the designated offset, not checking for
	 * collisions.
	 * <p>
	 * Not to be confused with {@link Entity#move(double, double, double)}
	 * .
	 */
	public void move(double x, double y, double z) {
		position().add(x, y, z);
	}
	
	/**
	 * Move this control point by the designated offset, not checking for
	 * collisions.
	 * <p>
	 * Not to be confused with {@link Entity#move(double, double, double)}
	 * .
	 */
	public void move(Vector offset) {
		move(offset.x(), offset.y(), offset.z());
	}
	
	public double getXPos() {
		return position().x();
	}
	
	public double getYPos() {
		return position().y();
	}
	
	public double getZPos() {
		return position().z();
	}
	
	public double getDistance(ControlPoint point) {
		return position().dist(point.position());
	}
	
	/**
	 * Get the arc that this control point belongs to.
	 * 
	 * @return
	 */
	public EntityArc getArc() {
		return arc;
	}
	
	public EntityLivingBase getOwner() {
		return owner;
	}
	
	public void setOwner(EntityLivingBase owner) {
		this.owner = owner;
	}
	
	/**
	 * "Attach" the arc to this control point, meaning that the control point
	 * now has a reference to the given arc.
	 */
	public void setArc(EntityArc arc) {
		this.arc = arc;
	}
	
}
