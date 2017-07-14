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

import static com.crowsofwar.avatar.client.uitools.Measurement.fromPixels;
import static com.crowsofwar.avatar.client.uitools.ScreenInfo.screenHeight;

import org.lwjgl.input.Mouse;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

/**
 * Allows similar functionality to GuiButton, but with a custom texture.
 * 
 * @author CrowsOfWar
 */
public class ComponentCustomButton extends UiComponent {
	
	private final ResourceLocation texture;
	private final int startU, startV, width, height;
	private final Runnable onClick;
	
	private boolean enabled, wasDown;
	
	/**
	 * Create a custom button. U and V specified should be initial U/V for
	 * regular (nohover) button. Next to that on the texture, there should be a
	 * hover version of the button, then a disabled version of the button.
	 */
	public ComponentCustomButton(ResourceLocation texture, int u, int v, int width, int height,
			Runnable onClick) {
		this.texture = texture;
		this.startU = u;
		this.startV = v;
		this.width = width;
		this.height = height;
		this.onClick = onClick;
		
		this.enabled = true;
	}
	
	@Override
	protected float componentWidth() {
		return width;
	}
	
	@Override
	protected float componentHeight() {
		return height;
	}
	
	@Override
	protected void componentDraw(float partialTicks, boolean mouseHover) {
		
		mc.renderEngine.bindTexture(texture);
		
		int mouseX = Mouse.getX(), mouseY = screenHeight() - Mouse.getY();
		
		Measurement min = coordinates();
		Measurement max = min.plus(fromPixels(width(), height()));
		boolean hover = mouseX > min.xInPixels() && mouseY > min.yInPixels() && mouseX < max.xInPixels()
				&& mouseY < max.yInPixels();
		
		int u = startU;
		if (!enabled) {
			u += width * 2;
		} else if (hover) {
			u += width;
		}
		
		drawTexturedModalRect(0, 0, u, startV, width, height);
		
		boolean down = Mouse.isButtonDown(0);
		if (enabled && down && !wasDown && hover) {
			onClick.run();
			mc.getSoundHandler()
					.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		}
		wasDown = down;
		
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
