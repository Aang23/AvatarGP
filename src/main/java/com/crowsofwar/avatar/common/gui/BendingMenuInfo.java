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

import java.util.List;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingController;

/**
 * Encapsulates information about an BendingController's radial menu- the
 * control and AvatarAbilities which will be included in the Gui.
 *
 */
public class BendingMenuInfo {
	
	private MenuTheme theme;
	private final String key;
	private final BendingAbility[] buttons;
	
	/**
	 * Create information for an BendingController's radial menu.
	 * 
	 * @param theme
	 *            The theme of this menu, defines colors, etc.
	 * @param key
	 *            The key which must be held to use this radial menu
	 * @param buttons
	 *            An array of abilities which will be used as the buttons. Can't
	 *            be more than 8. If it is less than 8, the unspecified elements
	 *            are filled with {@link AvatarAbility#NONE}.
	 */
	public BendingMenuInfo(MenuTheme theme, String key, BendingController bending) {
		List<BendingAbility> buttons = bending.getAllAbilities();
		if (buttons.size() > 8) throw new IllegalArgumentException(
				"Cannot create BendingMenuInfo with buttons being larger than 8");
		this.theme = theme;
		this.key = key;
		this.buttons = new BendingAbility[8];
		for (int i = 0; i < 8; i++)
			this.buttons[i] = i < buttons.size() ? buttons.get(i) : null;
	}
	
	public MenuTheme getTheme() {
		return theme;
	}
	
	public String getKey() {
		return key;
	}
	
	/**
	 * Get all the buttons. Size is guaranteed to be 8; if there is no button in
	 * that slot, it is null.
	 */
	public BendingAbility[] getButtons() {
		return buttons;
	}
	
}
