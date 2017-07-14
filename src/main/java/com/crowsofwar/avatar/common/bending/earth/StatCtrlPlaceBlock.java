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
package com.crowsofwar.avatar.common.bending.earth;

import static com.crowsofwar.avatar.common.bending.BendingAbility.ABILITY_PICK_UP_BLOCK;
import static com.crowsofwar.avatar.common.bending.StatusControl.CrosshairPosition.RIGHT_OF_CROSSHAIR;
import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.controls.AvatarControl.CONTROL_RIGHT_CLICK_DOWN;

import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.BendingContext;
import com.crowsofwar.avatar.common.entity.AvatarEntity;
import com.crowsofwar.avatar.common.entity.EntityFloatingBlock;
import com.crowsofwar.avatar.common.entity.data.FloatingBlockBehavior;
import com.crowsofwar.gorecore.util.Vector;
import com.crowsofwar.gorecore.util.VectorI;

import net.minecraft.block.SoundType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class StatCtrlPlaceBlock extends StatusControl {
	
	public StatCtrlPlaceBlock() {
		super(1, CONTROL_RIGHT_CLICK_DOWN, RIGHT_OF_CROSSHAIR);
		
		requireRaytrace(-1, true);
		
	}
	
	@Override
	public boolean execute(BendingContext ctx) {
		
		BendingController controller = BendingManager.getBending(BendingType.EARTHBENDING);
		
		BendingData data = ctx.getData();
		
		EntityFloatingBlock floating = AvatarEntity.lookupEntity(ctx.getWorld(), EntityFloatingBlock.class,
				fb -> fb.getBehavior() instanceof FloatingBlockBehavior.PlayerControlled
						&& fb.getOwner() == ctx.getBenderEntity());
		
		if (floating != null) {
			// TODO Verify look at block
			VectorI looking = ctx.getClientLookBlock();
			EnumFacing lookingSide = ctx.getLookSide();
			if (looking != null && lookingSide != null) {
				looking.offset(lookingSide);
				
				floating.setBehavior(new FloatingBlockBehavior.Place(looking.toBlockPos()));
				Vector force = looking.precision().minus(new Vector(floating));
				force.normalize();
				floating.velocity().add(force);
				
				SoundType sound = floating.getBlock().getSoundType();
				if (sound != null) {
					floating.world.playSound(null, floating.getPosition(), sound.getPlaceSound(),
							SoundCategory.PLAYERS, sound.getVolume(), sound.getPitch());
				}
				
				data.removeStatusControl(THROW_BLOCK);
				
				data.getAbilityData(ABILITY_PICK_UP_BLOCK).addXp(SKILLS_CONFIG.blockPlaced);
				
				return true;
			}
			return false;
		}
		
		return true;
		
	}
	
}
