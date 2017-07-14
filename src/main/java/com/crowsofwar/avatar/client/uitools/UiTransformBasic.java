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

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class UiTransformBasic implements UiTransform {
	
	private final UiComponent component;
	private StartingPosition pos;
	private Measurement offset;
	private float offsetScale, componentScale;
	private Frame frame;
	private float zLevel;
	
	public UiTransformBasic(UiComponent component) {
		if (component == null) throw new IllegalArgumentException("Cannot have null component");
		
		this.component = component;
		pos = StartingPosition.TOP_LEFT;
		offset = Measurement.fromPixels(0, 0);
		offsetScale = 1;
		componentScale = 1;
		frame = Frame.SCREEN;
		zLevel = 0;
		
	}
	
	@Override
	public Measurement coordinates() {
		float w = frame.getDimensions().xInPixels();
		float h = frame.getDimensions().yInPixels();
		
		float x = frame.getCoordsMin().xInPixels() + pos.getX() * w;
		x += offset().xInPixels() * offsetScale - pos.getMinusX() * component.width();
		
		float y = frame.getCoordsMin().yInPixels() + pos.getY() * h;
		y += offset().yInPixels() * offsetScale - pos.getMinusY() * component.height();
		
		return Measurement.fromPixels(x, y);
	}
	
	@Override
	public StartingPosition position() {
		return pos;
	}
	
	@Override
	public void setPosition(StartingPosition position) {
		this.pos = position;
	}
	
	@Override
	public Measurement offset() {
		return offset;
	}
	
	@Override
	public void setOffset(Measurement offset) {
		this.offset = offset;
	}
	
	@Override
	public float offsetScale() {
		return offsetScale;
	}
	
	@Override
	public void setOffsetScale(float scale) {
		this.offsetScale = scale;
	}
	
	@Override
	public float scale() {
		return componentScale;
	}
	
	@Override
	public void setScale(float scale) {
		this.componentScale = scale;
	}
	
	@Override
	public void update(float partialTicks) {}
	
	@Override
	public Frame getFrame() {
		return frame;
	}
	
	@Override
	public void setFrame(Frame frame) {
		this.frame = frame;
	}
	
	@Override
	public float zLevel() {
		return zLevel;
	}
	
	@Override
	public void setZLevel(float zLevel) {
		this.zLevel = zLevel;
	}
	
}
