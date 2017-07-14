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

import com.crowsofwar.gorecore.config.ConfigLoader;
import com.crowsofwar.gorecore.config.Load;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ConfigSkills {
	
	public static final ConfigSkills SKILLS_CONFIG = new ConfigSkills();
	
	private ConfigSkills() {}
	
	// @formatter:off
	@Load
	public float blockPlaced = 5f,
		blockThrowHit = 6.5f,
		blockKill = 4f,
		airJump = 2f,
		airGustHit = 4f,
		ravineHit = 3f,
		waveHit = 4f,
		waterHit = 3f,
		fireHit = 3f,
		flamethrowerHit = 0.75f,
		fireballHit = 4.5f,
		airbladeHit = 3f,
		miningUse = 10f,
		miningBreakOre = 5f,
		waterSkateOneSecond = 1.5f,
		wallRaised = 1f,
		wallBlockedAttack = 3f,
		airbubbleProtect = 6f,
		litFire = 20,
		createBubble = 15;
	// @formatter:on
	
	public static void load() {
		ConfigLoader.load(SKILLS_CONFIG, "avatar/skills.yml");
	}
	
}
