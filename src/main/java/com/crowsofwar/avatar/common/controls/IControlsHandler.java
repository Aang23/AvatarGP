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
package com.crowsofwar.avatar.common.controls;

import java.util.List;

/**
 * Manages what controls are pressed. This is designed for the client.
 *
 */
public interface IControlsHandler {
	
	/**
	 * <strong>For internal use only. To check if a control is pressed, you
	 * should use {@link AvatarControl#isPressed()}.</strong>
	 * <p>
	 * Get whether that control is pressed, only used for non-keybinding
	 * controls
	 */
	boolean isControlPressed(AvatarControl control);
	
	/**
	 * Get the key code for that control. It must be a keybinding.
	 */
	int getKeyCode(AvatarControl control);
	
	/**
	 * Get the display of that control based on its current binding (adjusts if
	 * the keybinding changes). Null if there is no description available.
	 */
	String getDisplayName(AvatarControl control);
	
	/**
	 * Get all controls pressed.
	 */
	List<AvatarControl> getAllPressed();
	
}
