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
package com.crowsofwar.avatar.common.gui;

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.AvatarLog.WarningType;
import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.entity.mob.EntitySkyBison;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AvatarGuiHandler implements IGuiHandler {
	
	public static final int GUI_ID_SKILLS_EARTH = 1;
	public static final int GUI_ID_SKILLS_FIRE = 2;
	public static final int GUI_ID_SKILLS_WATER = 3;
	public static final int GUI_ID_SKILLS_AIR = 4;
	public static final int GUI_ID_BISON_CHEST = 5;
	public static final int GUI_ID_GET_BENDING = 6;
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		
		if (id >= GUI_ID_SKILLS_EARTH && id <= GUI_ID_SKILLS_AIR) {
			int element = id - GUI_ID_SKILLS_EARTH + 1;
			return new ContainerSkillsGui(player, BendingType.values()[id]);
		}
		if (id == GUI_ID_BISON_CHEST) {
			// x-coordinate represents ID of sky bison
			int bisonId = x;
			EntitySkyBison bison = EntitySkyBison.findBison(world, bisonId);
			if (bison != null) {
				
				return new ContainerBisonChest(player.inventory, bison.getInventory(), bison, player);
				
			} else {
				AvatarLog.warn(WarningType.WEIRD_PACKET, player.getName()
						+ " tried to open skybison inventory, was not found. BisonId: " + bisonId);
			}
		}
		if (id == GUI_ID_GET_BENDING) {
			return new ContainerGetBending(player);
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return AvatarMod.proxy.createClientGui(id, player, world, x, y, z);
	}
	
}
