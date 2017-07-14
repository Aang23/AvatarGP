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
package com.crowsofwar.avatar.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.gorecore.config.ConfigLoader;
import com.crowsofwar.gorecore.config.Load;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ConfigClient {
	
	public static ConfigClient CLIENT_CONFIG = new ConfigClient();
	
	@Load
	public float radialMenuAlpha = 0.75f;
	
	@Load
	public float chiBarAlpha = 0.5f;
	
	@Load
	public float bendingCycleAlpha = 0.5f;
	
	@Load
	public boolean useCustomParticles = true;
	
	@Load
	private Map<String, Integer> nameKeymappings = new HashMap<>();
	public Map<BendingAbility, Integer> keymappings = new HashMap<>();
	
	@Load
	private Map<String, Boolean> nameConflicts = new HashMap<>();
	public Map<BendingAbility, Boolean> conflicts = new HashMap<>();
	
	public static void load() {
		ConfigLoader.load(CLIENT_CONFIG, "avatar/cosmetic.yml");
		
		CLIENT_CONFIG.keymappings.clear();
		Set<Map.Entry<String, Integer>> entries = CLIENT_CONFIG.nameKeymappings.entrySet();
		for (Map.Entry<String, Integer> entry : entries) {
			BendingAbility ability = null;
			for (BendingAbility a : BendingManager.allAbilities()) {
				if (a.getName().equals(entry.getKey())) {
					ability = a;
					break;
				}
			}
			if (ability != null) {
				CLIENT_CONFIG.keymappings.put(ability, entry.getValue());
			}
		}
		CLIENT_CONFIG.conflicts.clear();
		Set<Map.Entry<String, Boolean>> entries2 = CLIENT_CONFIG.nameConflicts.entrySet();
		for (Map.Entry<String, Boolean> entry : entries2) {
			BendingAbility ability = null;
			for (BendingAbility a : BendingManager.allAbilities()) {
				if (a.getName().equals(entry.getKey())) {
					ability = a;
					break;
				}
			}
			if (ability != null) {
				CLIENT_CONFIG.conflicts.put(ability, entry.getValue());
			}
		}
		
	}
	
	public static void save() {
		
		CLIENT_CONFIG.nameKeymappings.clear();
		Set<Map.Entry<BendingAbility, Integer>> entries = CLIENT_CONFIG.keymappings.entrySet();
		for (Map.Entry<BendingAbility, Integer> entry : entries) {
			CLIENT_CONFIG.nameKeymappings.put(entry.getKey().getName(), entry.getValue());
		}
		CLIENT_CONFIG.nameConflicts.clear();
		Set<Map.Entry<BendingAbility, Boolean>> entries2 = CLIENT_CONFIG.conflicts.entrySet();
		for (Map.Entry<BendingAbility, Boolean> entry : entries2) {
			CLIENT_CONFIG.nameConflicts.put(entry.getKey().getName(), entry.getValue());
		}
		
		ConfigLoader.save(CLIENT_CONFIG, "avatar/cosmetic.yml");
	}
	
}
