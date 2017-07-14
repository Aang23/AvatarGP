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
package com.crowsofwar.avatar.client.uitools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ScreenInfo {
	
	private static int width, height, scale;
	
	public static int screenWidth() {
		if (width == 0) refreshDimensions();
		return width;
	}
	
	public static int screenHeight() {
		if (height == 0) refreshDimensions();
		return height;
	}
	
	public static int scaleFactor() {
		if (scale == 0) refreshDimensions();
		return scale == 0 ? 1 : scale;
	}
	
	public static void refreshDimensions() {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		width = res.getScaledWidth() * res.getScaleFactor();
		height = res.getScaledHeight() * res.getScaleFactor();
		scale = res.getScaleFactor();
	}
	
}
