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
package com.crowsofwar.avatar.common;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

/**
 * Contains static methods used to acquire custom DamageSources for various
 * uses.
 * 
 * @author CrowsOfWar
 */
public class AvatarDamageSource {
	
	/**
	 * Create a DamageSource for damage caused by a floating block.
	 * 
	 * @param hit
	 *            Who was hit by floating block
	 * @param owner
	 *            Who threw the floating block
	 * @return DamageSource for the floating block
	 */
	public static DamageSource causeFloatingBlockDamage(Entity hit, @Nullable Entity owner) {
		return new EntityDamageSourceIndirect("avatar_earthbendBlock", hit, owner);
	}
	
	/**
	 * Create a DamageSource for damage caused by a water arc.
	 * 
	 * @param hit
	 *            Who was hit by the water arc
	 * @param owner
	 *            Who created the water arc
	 * @return DamageSource for the water arc
	 */
	public static DamageSource causeWaterDamage(Entity hit, @Nullable Entity owner) {
		return new EntityDamageSourceIndirect("avatar_waterArc", hit, owner);
	}
	
	/**
	 * Create a DamageSource for damage caused by a fire arc.
	 * 
	 * @param hit
	 *            Who was hit by the fire arc
	 * @param owner
	 *            Who created the fire arc
	 * @return DamageSource for the fire arc
	 */
	public static DamageSource causeFireDamage(Entity hit, @Nullable Entity owner) {
		return new EntityDamageSourceIndirect("avatar_fireArc", hit, owner);
	}
	
	/**
	 * Create a DamageSource for damage caused by a ravine.
	 * 
	 * @param hit
	 *            Who was hit by the ravine
	 * @param owner
	 *            Who created the ravine
	 */
	public static DamageSource causeRavineDamage(Entity hit, @Nullable Entity owner) {
		return new EntityDamageSourceIndirect("avatar_ravine", hit, owner);
	}
	
	/**
	 * Create a DamageSource for damage caused by a wave.
	 * 
	 * @param hit
	 *            Who was hit by the wave
	 * @param owner
	 *            Who created the wave
	 */
	public static DamageSource causeWaveDamage(Entity hit, @Nullable Entity owner) {
		return new EntityDamageSourceIndirect("avatar_wave", hit, owner);
	}
	
	/**
	 * Create a DamageSource for damage caused by a fireball.
	 * 
	 * @param hit
	 *            Who was hit by the fireball
	 * @param owner
	 *            Who created the fireball
	 */
	public static DamageSource causeFireballDamage(Entity hit, @Nullable Entity owner) {
		return new EntityDamageSourceIndirect("avatar_fireball", hit, owner);
	}
	
	/**
	 * Create a DamageSource for damage caused by an airblade.
	 * 
	 * @param hit
	 *            Who was hit by the airblade
	 * @param owner
	 *            Who created the airblade
	 */
	public static DamageSource causeAirbladeDamage(Entity hit, @Nullable Entity owner) {
		return new EntityDamageSourceIndirect("avatar_airblade", hit, owner);
	}
	
	/**
	 * Create a DamageSource for damage caused by flamethrower.
	 * 
	 * @param hit
	 *            Who was hit by the flames
	 * @param owner
	 *            Who created the flames
	 */
	public static DamageSource causeFlamethrowerDamage(Entity hit, @Nullable Entity owner) {
		return new EntityDamageSourceIndirect("avatar_flamethrower", hit, owner);
	}
	
	/**
	 * Create a DamageSource for damage caused by smashing the ground.
	 * 
	 * @param hit
	 *            Who was hit by the smash
	 * @param owner
	 *            Who smashed the ground
	 */
	public static DamageSource causeSmashDamage(Entity hit, @Nullable Entity owner) {
		return new EntityDamageSourceIndirect("avatar_groundSmash", hit, owner);
	}
	
}
