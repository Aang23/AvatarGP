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
package com.crowsofwar.avatar.client.gui.skills;

import com.crowsofwar.avatar.client.gui.AvatarUiTextures;
import com.crowsofwar.avatar.client.uitools.ComponentImage;
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;

import net.minecraft.client.Minecraft;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ComponentAbilityIcon extends ComponentImage {
	
	public ComponentAbilityIcon(BendingAbility ability) {
		super(AvatarUiTextures.skillsGui, getCurrentLevel(ability) * 16, 240, 16, 16);
	}
	
	private static int getCurrentLevel(BendingAbility ability) {
		AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(Minecraft.getMinecraft().player);
		return data.getAbilityData(ability).getLevel();
	}
	
}
