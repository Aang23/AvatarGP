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
package com.crowsofwar.avatar.common.bending;

import com.crowsofwar.avatar.common.bending.air.AbilityAirBubble;
import com.crowsofwar.avatar.common.bending.air.AbilityAirGust;
import com.crowsofwar.avatar.common.bending.air.AbilityAirJump;
import com.crowsofwar.avatar.common.bending.air.AbilityAirblade;
import com.crowsofwar.avatar.common.bending.earth.AbilityMining;
import com.crowsofwar.avatar.common.bending.earth.AbilityPickUpBlock;
import com.crowsofwar.avatar.common.bending.earth.AbilityRavine;
import com.crowsofwar.avatar.common.bending.earth.AbilityWall;
import com.crowsofwar.avatar.common.bending.fire.AbilityFireArc;
import com.crowsofwar.avatar.common.bending.fire.AbilityFireball;
import com.crowsofwar.avatar.common.bending.fire.AbilityFlamethrower;
import com.crowsofwar.avatar.common.bending.fire.AbilityLightFire;
import com.crowsofwar.avatar.common.bending.water.AbilityCreateWave;
import com.crowsofwar.avatar.common.bending.water.AbilityWaterArc;
import com.crowsofwar.avatar.common.bending.water.AbilityWaterBubble;
import com.crowsofwar.avatar.common.bending.water.AbilityWaterSkate;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.util.Raytrace;

import net.minecraft.entity.EntityLiving;

/**
 * Encapsulates all logic required for a bending ability. There is 1 instance of
 * a bending ability for each ability present - similar to BendingController.
 * 
 * @author CrowsOfWar
 */
public abstract class BendingAbility {
	
	public static BendingAbility ABILITY_AIR_GUST, ABILITY_AIR_JUMP, ABILITY_PICK_UP_BLOCK, ABILITY_RAVINE,
			ABILITY_LIGHT_FIRE, ABILITY_FIRE_ARC, ABILITY_FLAMETHROWER, ABILITY_WATER_ARC, ABILITY_WAVE,
			ABILITY_WATER_BUBBLE, ABILITY_WALL, ABILITY_WATER_SKATE, ABILITY_FIREBALL, ABILITY_AIRBLADE,
			ABILITY_MINING, ABILITY_AIR_BUBBLE;
	
	/**
	 * Creates all abilities. Done before bending controllers are created.
	 */
	public static void registerAbilities() {
		ABILITY_AIR_GUST = new AbilityAirGust();
		ABILITY_AIR_JUMP = new AbilityAirJump();
		ABILITY_PICK_UP_BLOCK = new AbilityPickUpBlock();
		ABILITY_RAVINE = new AbilityRavine();
		ABILITY_LIGHT_FIRE = new AbilityLightFire();
		ABILITY_FIRE_ARC = new AbilityFireArc();
		ABILITY_FLAMETHROWER = new AbilityFlamethrower();
		ABILITY_WATER_ARC = new AbilityWaterArc();
		ABILITY_WAVE = new AbilityCreateWave();
		ABILITY_WATER_BUBBLE = new AbilityWaterBubble();
		ABILITY_WALL = new AbilityWall();
		ABILITY_WATER_SKATE = new AbilityWaterSkate();
		ABILITY_FIREBALL = new AbilityFireball();
		ABILITY_AIRBLADE = new AbilityAirblade();
		ABILITY_MINING = new AbilityMining();
		ABILITY_AIR_BUBBLE = new AbilityAirBubble();
	}
	
	private static int nextId = 1;
	
	private final BendingType type;
	protected final int id;
	private final String name;
	private Raytrace.Info raytrace;
	
	public BendingAbility(BendingType bendingType, String name) {
		this.type = bendingType;
		this.id = nextId++;
		this.name = name;
		this.raytrace = new Raytrace.Info();
		BendingManager.registerAbility(this);
	}
	
	protected BendingController controller() {
		return BendingManager.getBending(type);
	}
	
	/**
	 * Get the bending type that this ability belongs to
	 */
	public final BendingType getBendingType() {
		return type;
	}
	
	/**
	 * Execute this ability. Only called on server.
	 * 
	 * @param ctx
	 *            Information for the ability
	 */
	public abstract void execute(AbilityContext ctx);
	
	/**
	 * Get cooldown after the ability is activated.
	 */
	public int getCooldown(AbilityContext ctx) {
		return 15;
	}
	
	/**
	 * Get the Id of this ability.
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * Require that a raycast be sent prior to {@link #execute(AbilityContext)}.
	 * Information for the raytrace will then be available through the
	 * {@link AbilityContext}.
	 * 
	 * @param range
	 *            Range to raycast. -1 for player's reach.
	 * @param raycastLiquids
	 *            Whether to keep going on hit liquids
	 */
	protected void requireRaytrace(double range, boolean raycastLiquids) {
		this.raytrace = new Raytrace.Info(range, raycastLiquids);
	}
	
	/**
	 * Get the request raytrace requirements for when the ability is activated.
	 */
	public final Raytrace.Info getRaytrace() {
		return raytrace;
	}
	
	/**
	 * Gets the name of this ability. Will be all lowercase with no spaces.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Creates a new instance of AI for the given entity/bender.
	 */
	public BendingAi getAi(EntityLiving entity, Bender bender) {
		return new DefaultAbilityAi(this, entity, bender);
	}
	
}
