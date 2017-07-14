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

import static com.crowsofwar.avatar.common.bending.BendingAbility.*;
import static com.crowsofwar.avatar.common.bending.BendingType.FIREBENDING;

import java.awt.Color;

import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.gui.BendingMenuInfo;
import com.crowsofwar.avatar.common.gui.MenuTheme;
import com.crowsofwar.avatar.common.gui.MenuTheme.ThemeColor;

import net.minecraft.nbt.NBTTagCompound;

public class Firebending extends BendingController {
	
	private final BendingMenuInfo menu;
	
	public Firebending() {
		
		addAbility(ABILITY_LIGHT_FIRE);
		addAbility(ABILITY_FIRE_ARC);
		addAbility(ABILITY_FLAMETHROWER);
		addAbility(ABILITY_FIREBALL);
		
		Color light = new Color(244, 240, 187);
		Color red = new Color(173, 64, 31);
		Color gray = new Color(40, 40, 40);
		ThemeColor background = new ThemeColor(light, red);
		ThemeColor edge = new ThemeColor(red, red);
		ThemeColor icon = new ThemeColor(gray, light);
		menu = new BendingMenuInfo(new MenuTheme(background, edge, icon, 0xFAAA5A), "Firebend", this);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		
	}
	
	@Override
	public BendingType getType() {
		return FIREBENDING;
	}
	
	@Override
	public BendingMenuInfo getRadialMenu() {
		return menu;
	}
	
	@Override
	public String getControllerName() {
		return "firebending";
	}
	
}
