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
package com.crowsofwar.avatar.common.bending.air;

import static com.crowsofwar.avatar.common.bending.StatusControl.AIR_JUMP;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.util.Raytrace;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityAirJump extends AirAbility {
	
	/**
	 * @param controller
	 */
	public AbilityAirJump() {
		super("air_jump");
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		
		BendingData data = ctx.getData();
		
		if (!data.hasStatusControl(AIR_JUMP) && ctx.consumeChi(STATS_CONFIG.chiAirJump)) {
			
			data.addStatusControl(AIR_JUMP);
			if (data.hasTickHandler(TickHandler.AIR_PARTICLE_SPAWNER)) {
				StatusControl sc = AIR_JUMP;
				Raytrace.Result raytrace = Raytrace.getTargetBlock(ctx.getBenderEntity(), -1);
				if (AIR_JUMP.execute(
						new BendingContext(data, ctx.getBenderEntity(), ctx.getBender(), raytrace))) {
					data.removeStatusControl(AIR_JUMP);
				}
			}
			
		}
	}
	
}
