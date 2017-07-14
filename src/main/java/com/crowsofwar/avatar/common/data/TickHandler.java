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
package com.crowsofwar.avatar.common.data;

import java.util.HashMap;
import java.util.Map;

import com.crowsofwar.avatar.common.bending.SmashGroundHandler;
import com.crowsofwar.avatar.common.bending.air.AirParticleSpawner;
import com.crowsofwar.avatar.common.bending.fire.FlamethrowerUpdateTick;
import com.crowsofwar.avatar.common.bending.water.WaterSkateHandler;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.mob.BisonSummonHandler;

import io.netty.buffer.ByteBuf;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public abstract class TickHandler {
	
	public static TickHandler AIR_PARTICLE_SPAWNER = new AirParticleSpawner();
	public static TickHandler FLAMETHROWER = new FlamethrowerUpdateTick();
	public static TickHandler WATER_SKATE = new WaterSkateHandler();
	public static TickHandler BISON_SUMMONER = new BisonSummonHandler();
	public static TickHandler SMASH_GROUND = new SmashGroundHandler();
	
	private static int nextId = 1;
	private static Map<Integer, TickHandler> allHandlers;
	private final int id;
	
	public TickHandler() {
		if (allHandlers == null) allHandlers = new HashMap<>();
		
		id = nextId++;
		allHandlers.put(id, this);
		
	}
	
	/**
	 * Ticks and returns whether to remove (false to stay)
	 */
	public abstract boolean tick(BendingContext ctx);
	
	public int id() {
		return id;
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
	}
	
	public static TickHandler fromId(int id) {
		return allHandlers.get(id);
	}
	
	public static TickHandler fromBytes(ByteBuf buf) {
		return fromId(buf.readInt());
	}
	
}
