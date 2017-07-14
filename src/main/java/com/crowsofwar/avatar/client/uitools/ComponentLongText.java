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

import static com.crowsofwar.avatar.client.uitools.ScreenInfo.scaleFactor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;

/**
 * Text that spans multiple lines
 * 
 * @author CrowsOfWar
 */
public class ComponentLongText extends UiComponent {
	
	private String text;
	private Measurement width;
	
	private List<String> lines;
	
	/**
	 * Creates a multiline text component. Note that only the x value of the
	 * width will be used; y is ignored.
	 */
	public ComponentLongText(String text, Measurement width) {
		this.text = text;
		this.width = width;
		calculateLines();
	}
	
	@Override
	protected float componentWidth() {
		return width.xInPixels() / scaleFactor();
	}
	
	@Override
	protected float componentHeight() {
		return mc.fontRenderer.FONT_HEIGHT * lines.size();
	}
	
	@Override
	protected void componentDraw(float partialTicks, boolean mouseHover) {
		for (int i = 0; i < lines.size(); i++) {
			drawString(mc.fontRenderer, lines.get(i), 0, i * mc.fontRenderer.FONT_HEIGHT, 0xffffff);
		}
	}
	
	private void calculateLines() {
		
		FontRenderer fr = mc.fontRenderer;
		lines = new ArrayList<>();
		
		String currentLine = "";
		
		String[] words = text.split(" ");
		
		for (int i = 0; i < words.length; i++) {
			
			String word = words[i];
			String wouldBe = currentLine + word + " ";
			if (fr.getStringWidth(wouldBe) > width.xInPixels() / scaleFactor()) {
				// The line is too long, push it onto lines and reset
				lines.add(currentLine);
				currentLine = "";
				
				// If a word is too long by itself, ignore it
				// (don't keep trying to put it on a new line, when it wouldn't
				// fit by itself anyways)
				if (fr.getStringWidth(word) <= width.xInPixels() / scaleFactor()) {
					i--;
				} else {
					lines.set(lines.size() - 1, wouldBe);
				}
				
			} else {
				// The line isn't long yet, so keep adding more words
				currentLine = wouldBe;
			}
			
		}
		
		lines.add(currentLine);
		
	}
	
}
