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

import static com.crowsofwar.avatar.client.uitools.ScreenInfo.scaleFactor;
import static net.minecraft.client.renderer.GlStateManager.color;

import com.crowsofwar.avatar.client.uitools.Measurement;
import com.crowsofwar.avatar.client.uitools.UiComponent;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

/**
 * A component which allows control over a gui-container's Slots. Makes it
 * easier to set position, background, visibility, etc.
 * 
 * @author CrowsOfWar
 */
public class ComponentInventorySlots extends UiComponent {
	
	private final Container container;
	
	private final int cols, rows, minIndex, maxIndex;
	private int width, height;
	
	private ResourceLocation texture;
	private int u, v;
	
	private Measurement padding;
	
	/**
	 * Creates only one inventory slot at the given index.
	 */
	public ComponentInventorySlots(Container container, int index) {
		this(container, 1, 1, index, index);
	}
	
	/**
	 * Creates a grid of inventory slots with the given dimensions. This assumes
	 * the indices are in row-major order. Min/max index are inclusive.
	 */
	public ComponentInventorySlots(Container container, int cols, int rows, int minIndex, int maxIndex) {
		
		this.container = container;
		
		this.cols = cols;
		this.rows = rows;
		this.width = cols * 18;
		this.height = rows * 18;
		this.minIndex = minIndex;
		this.maxIndex = maxIndex;
		
		this.texture = null;
		this.u = -1;
		this.v = -1;
		
		this.padding = Measurement.fromPixels(0, 0);
		
	}
	
	/**
	 * Draws using the given texture. Dimensions will also be updated.
	 */
	public void useTexture(ResourceLocation texture, int u, int v, int width, int height) {
		this.width = width;
		this.height = height;
		this.texture = texture;
		this.u = u;
		this.v = v;
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
		// Check visibility
		for (int i = minIndex; i <= maxIndex; i++) {
			Slot slot = container.getSlot(i);
			int x = (int) coordinates().xInPixels() + (int) padding.xInPixels() * scaleFactor();
			int y = (int) coordinates().yInPixels() + (int) padding.yInPixels() * scaleFactor();
			
			int j = i - minIndex;
			
			slot.xPos = 18 * scaleFactor() * (j % cols) + x;
			slot.yPos = 18 * scaleFactor() * (j / cols) + y;
			slot.xPos /= scaleFactor();
			slot.yPos /= scaleFactor();
			slot.xPos++;
			slot.yPos++;
		}
		
		// Draw texture
		if (texture != null) {
			mc.renderEngine.bindTexture(texture);
			color(1, 1, 1, 1);
			drawTexturedModalRect(0, 0, u, v, width, height);
		}
		
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!isVisible()) {
			for (int i = minIndex; i <= maxIndex; i++) {
				Slot slot = container.getSlot(i);
				slot.xPos = -18;
				slot.yPos = -18;
			}
		}
	}
	
	public Measurement getPadding() {
		return padding;
	}
	
	/**
	 * Set offset from texture to start of slots
	 */
	public void setPadding(Measurement padding) {
		this.padding = padding;
	}
	
}
