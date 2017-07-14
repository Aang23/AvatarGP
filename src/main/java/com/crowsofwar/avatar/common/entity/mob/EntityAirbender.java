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

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.item.ItemScroll.ScrollType;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityAirbender extends EntityHumanBender {
	
	public static final ResourceLocation LOOT_TABLE = LootTableList
			.register(new ResourceLocation("avatarmod", "airbender"));
	
	/**
	 * @param world
	 */
	public EntityAirbender(World world) {
		super(world);
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35);
	}
	
	@Override
	protected void addBendingTasks() {
		this.tasks.addTask(1, BendingAbility.ABILITY_AIR_BUBBLE.getAi(this, this));
		this.tasks.addTask(2, BendingAbility.ABILITY_AIR_GUST.getAi(this, this));
		this.tasks.addTask(3, BendingAbility.ABILITY_AIRBLADE.getAi(this, this));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1, true));
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
	
	@Override
	protected ScrollType getScrollType() {
		return ScrollType.AIR;
	}
	
	@Override
	protected int getNumSkins() {
		return 7;
	}
	
}
