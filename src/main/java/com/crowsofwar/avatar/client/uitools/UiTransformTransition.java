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
public class UiTransformTransition implements UiTransform {
	
	private final UiTransform initial, ending;
	private final float maxTicks;
	private float ticks, lastPartialTicks;
	
	public UiTransformTransition(UiTransform initial, UiTransform ending, float seconds) {
		this.initial = initial;
		this.ending = ending;
		this.maxTicks = seconds * 20;
	}
	
	private float percentDone() {
		float value = ticks / maxTicks;
		return value > 1 ? 1 : value;
	}
	
	private float invPercentDone() {
		return 1 - percentDone();
	}
	
	@Override
	public Measurement coordinates() {
		return ending.coordinates().times(percentDone()).plus(initial.coordinates().times(invPercentDone()));
	}
	
	@Override
	public StartingPosition position() {
		Measurement dim1 = initial.getFrame().getDimensions(), dim2 = ending.getFrame().getDimensions();
		float x = ending.position().getX() * dim2.xInPixels() * percentDone()
				+ initial.position().getX() * dim1.xInPixels() * invPercentDone();
		float y = ending.position().getY() * dim2.yInPixels() * percentDone()
				+ initial.position().getY() * dim1.yInPixels() * invPercentDone();
		return StartingPosition.custom(x, y);
	}
	
	@Override
	public void setPosition(StartingPosition position) {}
	
	@Override
	public Measurement offset() {
		return ending.offset().times(percentDone()).plus(initial.offset().times(invPercentDone()));
	}
	
	@Override
	public void setOffset(Measurement offset) {}
	
	@Override
	public float scale() {
		return ending.scale() * percentDone() + initial.scale() * invPercentDone();
	}
	
	@Override
	public void setScale(float scale) {}
	
	@Override
	public float offsetScale() {
		return ending.offsetScale() * percentDone() + initial.offsetScale() * invPercentDone();
	}
	
	@Override
	public void setOffsetScale(float scale) {}
	
	@Override
	public void update(float partialTicks) {
		float diff = partialTicks - lastPartialTicks;
		if (diff < 0) diff += 1;
		ticks += diff;
		lastPartialTicks = partialTicks;
	}
	
	@Override
	public Frame getFrame() {
		return Frame.SCREEN;
	}
	
	@Override
	public void setFrame(Frame frame) {}
	
	@Override
	public float zLevel() {
		return ending.zLevel() * percentDone() + initial.zLevel() * invPercentDone();
	}
	
	@Override
	public void setZLevel(float zLevel) {}
	
}
