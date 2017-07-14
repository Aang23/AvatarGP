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
package com.crowsofwar.avatar.common.data.ctx;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.util.Raytrace.Result;

import net.minecraft.entity.EntityLivingBase;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityContext extends BendingContext {
	
	private final BendingAbility ability;
	
	public AbilityContext(AvatarPlayerData data, Result raytrace, BendingAbility ability) {
		super(data, raytrace);
		this.ability = ability;
	}
	
	public AbilityContext(BendingData data, EntityLivingBase entity, Bender bender, Result raytrace,
			BendingAbility ability) {
		super(data, entity, bender, raytrace);
		this.ability = ability;
	}
	
	public AbilityData getAbilityData() {
		return getData().getAbilityData(ability);
	}
	
	public int getLevel() {
		return getAbilityData().getLevel();
	}
	
	public AbilityTreePath getPath() {
		return getAbilityData().getPath();
	}
	
	/**
	 * Returns true if ability is on level 4 and has selected that path.
	 */
	public boolean isMasterLevel(AbilityTreePath path) {
		return getLevel() == 3 && getPath() == path;
	}
	
}
