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

import java.awt.Color;

public class MenuTheme {
	
	private final ThemeColor background, edge, icon;
	private final int text;
	
	public MenuTheme(ThemeColor background, ThemeColor edge, ThemeColor icon, int text) {
		this.background = background;
		this.edge = edge;
		this.icon = icon;
		this.text = text;
	}
	
	public ThemeColor getBackground() {
		return background;
	}
	
	public ThemeColor getEdge() {
		return edge;
	}
	
	public ThemeColor getIcon() {
		return icon;
	}
	
	public int getText() {
		return text;
	}
	
	public static class ThemeColor {
		private final int r, g, b, hoverR, hoverG, hoverB;
		
		public ThemeColor(Color def, Color hover) {
			this(def.getRed(), def.getGreen(), def.getBlue(), hover.getRed(), hover.getGreen(),
					hover.getBlue());
		}
		
		public ThemeColor(int r, int g, int b, int hoverR, int hoverG, int hoverB) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.hoverR = hoverR;
			this.hoverG = hoverG;
			this.hoverB = hoverB;
		}
		
		public int getRed(boolean hover) {
			return hover ? hoverR : r;
		}
		
		public int getGreen(boolean hover) {
			return hover ? hoverG : g;
		}
		
		public int getBlue(boolean hover) {
			return hover ? hoverB : b;
		}
		
	}
	
}
