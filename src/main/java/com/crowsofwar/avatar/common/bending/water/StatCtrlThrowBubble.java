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
package com.crowsofwar.avatar.common.bending.water;

import static com.crowsofwar.avatar.common.bending.StatusControl.CrosshairPosition.RIGHT_OF_CROSSHAIR;
import static com.crowsofwar.avatar.common.controls.AvatarControl.CONTROL_RIGHT_CLICK_DOWN;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityWaterBubble;
import com.crowsofwar.avatar.common.entity.data.WaterBubbleBehavior;
import com.crowsofwar.gorecore.util.Vector;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class StatCtrlThrowBubble extends StatusControl {
	
	/**
	 */
	public StatCtrlThrowBubble() {
		super(7, CONTROL_RIGHT_CLICK_DOWN, RIGHT_OF_CROSSHAIR);
	}
	
	@Override
	public boolean execute(BendingContext ctx) {
		BendingData data = ctx.getData();
		
		EntityWaterBubble bubble = AvatarEntity.lookupEntity(ctx.getWorld(), EntityWaterBubble.class, //
				bub -> bub.getBehavior() instanceof WaterBubbleBehavior.PlayerControlled
						&& bub.getOwner() == ctx.getBenderEntity());
		
		if (bubble != null) {
			
			AbilityData adata = data.getAbilityData(BendingAbility.ABILITY_WATER_BUBBLE);
			double mult = adata.getLevel() >= 1 ? 14 : 8;
			if (adata.isMasterPath(AbilityTreePath.FIRST)) {
				mult = 20;
			}
			
			bubble.setBehavior(new WaterBubbleBehavior.Thrown());
			bubble.velocity().set(Vector.getLookRectangular(ctx.getBenderEntity()).mul(mult));
		}
		
		return true;
	}
	
}
