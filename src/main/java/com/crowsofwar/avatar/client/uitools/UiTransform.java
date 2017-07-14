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
 * A mutable transformation class which keeps track of a UiComponent's position.
 * <p>
 * It starts at a {@link StartingPosition}, which is the component's general
 * location on the screen (top-right, left, bottom, etc). That can be adjusted
 * by {@link #offset() offsetting it}. The actual position is found by calling
 * {@link #coordinates()}.
 * 
 * @author CrowsOfWar
 */
public interface UiTransform {
	
	/**
	 * Called by the UiComponent to update the transform.
	 */
	void update(float partialTicks);
	
	/**
	 * The calculated coordinates based on position and offset
	 */
	Measurement coordinates();
	
	/**
	 * The starting position, such as top-right
	 */
	StartingPosition position();
	
	/**
	 * Relocate the starting position to the given value
	 */
	void setPosition(StartingPosition position);
	
	/**
	 * Returns the offset from the {@link #position() starting position}.
	 */
	Measurement offset();
	
	/**
	 * Set the offsets to the given value
	 */
	void setOffset(Measurement offset);
	
	/**
	 * Adds the given offset to the current offset
	 */
	default void addOffset(Measurement offset) {
		setOffset(offset().plus(offset));
	}
	
	/**
	 * Gets the scale of the actual component. Coordinates may be adjusted to
	 * accommodate for this.
	 */
	float scale();
	
	/**
	 * Sets the scale of the component
	 */
	void setScale(float scale);
	
	/**
	 * Returns the value to multiply by the {@link #offset() offsets}.
	 */
	float offsetScale();
	
	/**
	 * Set the value to multiply {@link #offset() offset} by.
	 */
	void setOffsetScale(float scale);
	
	/**
	 * Get the frame that this transform is inside
	 */
	Frame getFrame();
	
	/**
	 * Put this transform into the given frame
	 */
	void setFrame(Frame frame);
	
	/**
	 * Gets the Z-position of the UiComponent. In the case of two elements
	 * overlapping, it is used to determine which element would display "on top"
	 * of the other.
	 */
	float zLevel();
	
	/**
	 * Sets the z-position of the UiComponent
	 */
	void setZLevel(float zLevel);
	
}
