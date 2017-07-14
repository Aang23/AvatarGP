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
package com.crowsofwar.avatar.common.entity.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.crowsofwar.avatar.AvatarLog;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

/**
 * Describes a synced behavior. They follow the state design pattern, in that
 * each behavior should be switchable over an entity, and is responsible for an
 * update tick. Typically, behaviors are static inner classes, where the outer
 * class extends Behavior and is the superclass of the inner classes.
 * <p>
 * All custom behaviors must be registered via {@link #registerBehavior(Class)}.
 * As Behaviors are most commonly synced with DataManager, a
 * {@link BehaviorSerializer data serializer} is needed to synchronize server
 * and client. It should be registered with
 * {@link DataSerializers#registerSerializer(DataSerializer)}.
 * <p>
 * Make sure that subclasses receive the instance of entity. For server-side
 * changing of behavior, call {@link #Behavior(Entity) the constructor with
 * Entity argument}. Client-side
 * 
 * @param E
 *            Type of entity this behavior is for
 * 
 * @author CrowsOfWar
 */
public abstract class Behavior<E extends Entity> {
	
	private static int nextId = 1;
	private static Map<Integer, Class<? extends Behavior>> behaviorIdToClass;
	private static Map<Class<? extends Behavior>, Integer> classToBehaviorId;
	
	protected static int registerBehavior(Class<? extends Behavior> behaviorClass) {
		if (behaviorIdToClass == null) {
			behaviorIdToClass = new HashMap<>();
			classToBehaviorId = new HashMap<>();
			nextId = 1;
		}
		int id = nextId++;
		behaviorIdToClass.put(id, behaviorClass);
		classToBehaviorId.put(behaviorClass, id);
		return id;
	}
	
	/**
	 * Looks up the behavior class by the given Id, then instantiates an
	 * instance with reflection.
	 */
	public static Behavior lookup(int id, Entity entity) {
		try {
			
			Behavior behavior = behaviorIdToClass.get(id).newInstance();
			return behavior;
			
		} catch (Exception e) {
			
			AvatarLog.error("Error constructing behavior...");
			e.printStackTrace();
			return null;
			
		}
	}
	
	public Behavior() {}
	
	public int getId() {
		return classToBehaviorId.get(getClass());
	}
	
	/**
	 * Called every update tick.
	 * 
	 * @return Next Behavior. Return <code>this</code> to continue the Behavior.
	 *         Never return null.
	 */
	public abstract Behavior onUpdate(E entity);
	
	public abstract void fromBytes(PacketBuffer buf);
	
	public abstract void toBytes(PacketBuffer buf);
	
	public abstract void load(NBTTagCompound nbt);
	
	public abstract void save(NBTTagCompound nbt);
	
	public static class BehaviorSerializer<B extends Behavior<? extends Entity>>
			implements DataSerializer<B> {
		
		// FIXME research- why doesn't read/write get called every time that
		// behavior changes???
		
		@Override
		public void write(PacketBuffer buf, B value) {
			buf.writeInt(value.getId());
			value.toBytes(buf);
		}
		
		@Override
		public B read(PacketBuffer buf) throws IOException {
			try {
				
				Behavior behavior = behaviorIdToClass.get(buf.readInt()).newInstance();
				behavior.fromBytes(buf);
				return (B) behavior;
				
			} catch (Exception e) {
				
				AvatarLog.error("Error reading Behavior from bytes");
				e.printStackTrace();
				return null;
				
			}
		}
		
		@Override
		public DataParameter<B> createKey(int id) {
			return new DataParameter<>(id, this);
		}

		@Override
		public B copyValue(B behavior) {
			return behavior;
		}

	}
	
}
