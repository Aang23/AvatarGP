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
package com.crowsofwar.avatar.common.entity.mob;

import static com.crowsofwar.avatar.common.bending.BendingAbility.ABILITY_WAVE;

import com.crowsofwar.avatar.common.item.ItemScroll.ScrollType;

import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityWaterbender extends EntityHumanBender {
	
	public static final ResourceLocation LOOT_TABLE = LootTableList
			.register(new ResourceLocation("avatarmod", "waterbender"));
	
	public EntityWaterbender(World world) {
		super(world);
	}
	
	@Override
	protected void addBendingTasks() {
		this.tasks.addTask(1, ABILITY_WAVE.getAi(this, this));
		// this.tasks.addTask(2, ABILITY_WATER_ARC.getAi(this, this));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1, true));
	}
	
	@Override
	protected ScrollType getScrollType() {
		return ScrollType.WATER;
	}
	
	@Override
	protected int getNumSkins() {
		return 1;
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
	
	@Override
	public boolean consumeWaterLevel(int amount) {
		return true;
	}
	
}
