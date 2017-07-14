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
package com.crowsofwar.avatar.common.bending.fire;

import static com.crowsofwar.avatar.common.bending.StatusControl.CrosshairPosition.RIGHT_OF_CROSSHAIR;
import static com.crowsofwar.avatar.common.controls.AvatarControl.CONTROL_RIGHT_CLICK_DOWN;
import static com.crowsofwar.avatar.common.controls.AvatarControl.CONTROL_RIGHT_CLICK_UP;

import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class StatCtrlSetFlamethrowing extends StatusControl {
	
	private final boolean setting;
	
	public StatCtrlSetFlamethrowing(boolean setting) {
		super(setting ? 4 : 5, setting ? CONTROL_RIGHT_CLICK_DOWN : CONTROL_RIGHT_CLICK_UP,
				RIGHT_OF_CROSSHAIR);
		this.setting = setting;
	}
	
	@Override
	public boolean execute(BendingContext ctx) {
		
		BendingData data = ctx.getData();
		EntityLivingBase bender = ctx.getBenderEntity();
		World world = ctx.getWorld();
		
		if (data.hasBending(BendingType.FIREBENDING)) {
			if (setting) {
				data.addStatusControl(STOP_FLAMETHROW);
				data.addTickHandler(TickHandler.FLAMETHROWER);
			} else {
				data.removeTickHandler(TickHandler.FLAMETHROWER);
			}
		}
		
		return true;
	}
	
}
