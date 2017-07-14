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
package com.crowsofwar.avatar.client.gui;

import java.util.HashMap;
import java.util.Map;

import com.crowsofwar.avatar.common.bending.BendingAbility;

import net.minecraft.util.ResourceLocation;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AvatarUiTextures {
	
	static final ResourceLocation radialMenu = new ResourceLocation("avatarmod",
			"textures/radial/circle_segment.png");
	static final ResourceLocation icons = new ResourceLocation("avatarmod", "textures/gui/ability_icons.png");
	static final ResourceLocation blurredIcons = new ResourceLocation("avatarmod",
			"textures/gui/blurred_icons.png");
	public static final ResourceLocation skillsGui = new ResourceLocation("avatarmod",
			"textures/gui/skillmenu.png");
	public static final ResourceLocation getBending = new ResourceLocation("avatarmod",
			"textures/gui/getbending.png");
	public static final ResourceLocation airBubbleHealth = new ResourceLocation("avatarmod",
			"textures/gui/airbubble_health.png");
	
	static final ResourceLocation bgWater = new ResourceLocation("avatarmod",
			"textures/gui/bg_water_screen.png");
	static final ResourceLocation bgFire = new ResourceLocation("avatarmod",
			"textures/gui/bg_fire_screen.png");
	static final ResourceLocation bgAir = new ResourceLocation("avatarmod", "textures/gui/bg_air_screen.png");
	static final ResourceLocation bgEarth = new ResourceLocation("avatarmod",
			"textures/gui/bg_earth_screen.png");
	public static final ResourceLocation STATUS_CONTROL_ICONS = new ResourceLocation("avatarmod",
			"textures/gui/status_controls.png");
	public static final ResourceLocation CHI_BAR = new ResourceLocation("avatarmod", "textures/gui/chi.png");
	
	public static final ResourceLocation WHITE = new ResourceLocation("avatarmod", "textures/gui/white.png");
	
	private static final Map<BendingAbility, ResourceLocation> abilityTextures = new HashMap<>();
	private static final Map<BendingAbility, ResourceLocation> abilityCards = new HashMap<>();
	private static final Map<BendingAbility, ResourceLocation> abilityCardsPlain = new HashMap<>();
	
	private static <T> ResourceLocation getCachedImage(Map<T, ResourceLocation> map, T obj, String loc) {
		if (!map.containsKey(obj)) {
			ResourceLocation location = new ResourceLocation("avatarmod", loc);
			map.put(obj, location);
			return location;
		}
		return map.get(obj);
	}
	
	public static ResourceLocation getAbilityTexture(BendingAbility ability) {
		return getCachedImage(abilityTextures, ability, "textures/radial/icon_" + ability.getName() + ".png");
	}
	
	public static ResourceLocation getCardTexture(BendingAbility ability) {
		return getCachedImage(abilityCards, ability, "textures/gui/skillmenu/" + ability.getName() + ".png");
	}
	
	public static ResourceLocation getPlainCardTexture(BendingAbility ability) {
		return getCachedImage(abilityCardsPlain, ability,
				"textures/gui/skillmenu/" + ability.getName() + "_plain.png");
	}
	
}
