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

import static com.crowsofwar.avatar.client.uitools.ScreenInfo.*;
import static net.minecraft.client.renderer.GlStateManager.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

/**
 * A positioned rectangular frame within the screen which allows positioning of
 * {@link UiComponent components} within a certain bounds.
 * 
 * @author CrowsOfWar
 */
public class Frame extends Gui {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation("avatarmod", "textures/gui/box.png");
	
	public static final Frame SCREEN = new Frame(null) {
		
		@Override
		public Measurement getOffset() {
			return Measurement.fromPixels(0, 0);
		}
		
		@Override
		public Measurement getDimensions() {
			return Measurement.fromPixels(screenWidth(), screenHeight());
		}
		
		@Override
		public Measurement getCoordsMin() {
			return Measurement.fromPixels(0, 0);
		}
		
	};
	
	private final Frame parent;
	private Measurement offset, dimensions;
	
	public Frame() {
		this(SCREEN);
	}
	
	public Frame(Frame parent) {
		this.parent = parent;
		this.offset = Measurement.fromPixels(0, 0);
		this.dimensions = Measurement.fromPixels(screenWidth(), screenHeight());
	}
	
	/**
	 * Get offset from the parent frame
	 */
	public Measurement getOffset() {
		return offset;
	}
	
	public void setPosition(Measurement offset) {
		this.offset = offset;
	}
	
	public Measurement getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(Measurement dimensions) {
		this.dimensions = dimensions;
	}
	
	/**
	 * Get the calculated coordinates of this frame, based off of parent pos +
	 * offset. Top-left.
	 */
	public Measurement getCoordsMin() {
		return getOffset().plus(parent.getCoordsMin());
	}
	
	/**
	 * Get the calculated coordinates of this frame, based off of parent pos +
	 * offset. Bottom-right.
	 */
	public Measurement getCoordsMax() {
		return getCoordsMin().plus(getDimensions());
	}
	
	public void draw(float partialTicks) {
		
		//@formatter:off
		color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		pushMatrix();
			
			enableBlend();
		
			scaleFactor();
			
			scale(1f / scaleFactor(), 1f / scaleFactor(), 1);
			translate(getCoordsMin().xInPixels(), getCoordsMin().yInPixels(), 0);
			scale(dimensions.xInPixels() / 256, dimensions.yInPixels() / 256, 1);
			drawTexturedModalRect(0, 0, 0, 0, 256, 256);
			
			disableBlend();
			
		popMatrix();
		//@formatter:on
	}
	
}
