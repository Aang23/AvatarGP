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
package com.crowsofwar.avatar.client.gui.skills;

import static com.crowsofwar.avatar.client.gui.AvatarUiTextures.getCardTexture;
import static com.crowsofwar.avatar.client.uitools.Measurement.fromPercent;
import static com.crowsofwar.avatar.client.uitools.Measurement.fromPixels;
import static com.crowsofwar.avatar.client.uitools.ScreenInfo.scaleFactor;
import static com.crowsofwar.avatar.client.uitools.ScreenInfo.screenHeight;

import com.crowsofwar.avatar.client.uitools.ComponentImage;
import com.crowsofwar.avatar.client.uitools.Frame;
import com.crowsofwar.avatar.client.uitools.Measurement;
import com.crowsofwar.avatar.client.uitools.StartingPosition;
import com.crowsofwar.avatar.client.uitools.UiComponent;
import com.crowsofwar.avatar.common.bending.BendingAbility;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityCard {
	
	private final BendingAbility ability;
	private final int index;
	private Frame frame;
	private UiComponent icon;
	
	public AbilityCard(BendingAbility ability, int index) {
		
		fromPixels(0, 0);
		fromPercent(0, 0);
		
		this.ability = ability;
		this.index = index;
		
		float width = 256 / 256 * 0.4f * 100, height = 256f / 256 * 0.4f * 100;
		
		frame = new Frame();
		frame.setDimensions(fromPixels(192, 256).times(scaleFactor() / 2));
		
		icon = new ComponentImage(getCardTexture(ability), 32, 0, 192, 256);
		icon.setFrame(frame);
		icon.setPosition(StartingPosition.MIDDLE_TOP);
		// icon.setOffset(fromPixels(frame, 0, -text.height() - icon.height() *
		// 50 / 256));
		icon.setScale(0.5f);
		
		updateFramePos(0);
		
	}
	
	public void draw(float partialTicks, float scroll, float mouseX, float mouseY) {
		
		updateFramePos(scroll);
		
		icon.draw(partialTicks, mouseX, mouseY);
		// frame.draw(partialTicks);
		
	}
	
	/**
	 * Width in px
	 */
	public float width() {
		return frame.getDimensions().xInPixels();
	}
	
	public BendingAbility getAbility() {
		return ability;
	}
	
	public boolean isMouseHover(float mouseX, float mouseY, float scroll) {
		
		updateFramePos(scroll);
		Measurement min = frame.getCoordsMin();
		Measurement max = frame.getCoordsMax();
		return mouseX > min.xInPixels() && mouseY > min.yInPixels() && mouseX < max.xInPixels()
				&& mouseY < max.yInPixels();
		
	}
	
	private void updateFramePos(float scroll) {
		
		Measurement base = fromPixels(50, (screenHeight() - icon.height()) / 2);
		Measurement offset = fromPixels(scroll + index * icon.width() * 1.2f, 0);
		frame.setPosition(base.plus(offset));
		
	}
	
}
