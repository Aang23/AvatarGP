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
package com.crowsofwar.avatar.common.entityproperty;

import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.Entity;

/**
 * An entity property which allows simple access to the entity's motion vector
 * by manipulating the motionX, motionY, and motionZ fields. All methods for
 * velocity are in m/s.
 *
 */
public class EntityPropertyMotion implements IEntityProperty<Vector> {
	
	private final Vector internalVelocity;
	private final Entity entity;
	
	public EntityPropertyMotion(Entity entity) {
		this.internalVelocity = new Vector(0, 0, 0);
		this.entity = entity;
	}
	
	@Override
	public Vector getValue() {
		internalVelocity.setX(entity.motionX * 20);
		internalVelocity.setY(entity.motionY * 20);
		internalVelocity.setZ(entity.motionZ * 20);
		return internalVelocity;
	}
	
	@Override
	public void setValue(Vector value) {
		entity.motionX = value.x() / 20;
		entity.motionY = value.y() / 20;
		entity.motionZ = value.z() / 20;
	}
	
}
