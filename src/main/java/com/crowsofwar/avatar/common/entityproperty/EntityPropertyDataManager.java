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

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.EntityDataManager;

/**
 * An IEntityProperty which uses the entity's DataManager to synchronize between
 * server and client. TODO add more docs
 *
 * @param <T>
 *            The type of object stored in the DataManager.
 */
public final class EntityPropertyDataManager<T> implements IEntityProperty<T> {
	
	private final Entity entity;
	private final EntityDataManager dataManager;
	private final DataParameter<T> parameter;
	
	/**
	 * Creates a new EntityProperty using the EntityDataManager for a specific
	 * entity.
	 * 
	 * @param entity
	 *            The instance of the entity
	 * @param clazz
	 *            The class of the entity
	 * @param serializer
	 *            The DataSerializer which will be used to send the value over
	 *            the network
	 * @param startingValue
	 *            The initial value of your property
	 */
	public <E extends Entity> EntityPropertyDataManager(E entity, Class<E> clazz,
			DataSerializer<T> serializer, T startingValue) {
		this.entity = entity;
		this.dataManager = entity.getDataManager();
		this.parameter = EntityDataManager.createKey(clazz, serializer);
		this.dataManager.register(parameter, startingValue);
	}
	
	/**
	 * Create a new EntityProperty.
	 * 
	 * @param entity
	 *            The instance of the entity
	 * @param parameter
	 *            The DataParameter which is used to store the entity property.
	 *            Note: Is also registered to the EntityDataManager.
	 * @param startingValue
	 *            The initial value of the property.
	 */
	public EntityPropertyDataManager(Entity entity, DataParameter<T> parameter, T startingValue) {
		this.entity = entity;
		this.dataManager = entity.getDataManager();
		this.parameter = parameter;
		this.dataManager.register(parameter, startingValue);
	}
	
	@Override
	public T getValue() {
		return dataManager.get(parameter);
	}
	
	@Override
	public void setValue(T value) {
		dataManager.set(parameter, value);
	}
	
}
