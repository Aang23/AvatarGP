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
import static com.crowsofwar.avatar.client.uitools.ScreenInfo.scaleFactor;
import static net.minecraft.client.renderer.GlStateManager.*;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Components are a part of a ui. They can be text, images, etc. They have a
 * manipulable {@link UiTransform transformation} that allows repositioning,
 * scaling, etc.
 * 
 * @author CrowsOfWar
 */
public abstract class UiComponent extends Gui {
	
	protected final Minecraft mc;
	private UiTransform transform;
	private boolean visible;
	
	public UiComponent() {
		this.mc = Minecraft.getMinecraft();
		this.transform = new UiTransformBasic(this);
		this.visible = true;
	}
	
	public UiTransform transform() {
		return transform;
	}
	
	public void setTransform(UiTransform transform) {
		this.transform = transform;
	}
	
	protected abstract float componentWidth();
	
	protected abstract float componentHeight();
	
	/**
	 * Get the actual width that is seen on-screen, in pixels
	 */
	public float width() {
		return componentWidth() * scale() * scaleFactor();
	}
	
	/**
	 * Get the actual height that is seen on-screen, in pixels
	 */
	public float height() {
		return componentHeight() * scale() * scaleFactor();
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void draw(float partialTicks, float mouseX, float mouseY) {
		
		transform.update(partialTicks);
		color(1, 1, 1, 1);
		
		if (!visible) return;
		
		//@formatter:off
		pushMatrix();
		
			float x = coordinates().xInPixels() / scaleFactor();
			float y = coordinates().yInPixels() / scaleFactor();
			GlStateManager.translate((int) x, (int) y, 0);
			GlStateManager.scale(scale(), scale(), 1f); // unfortunately needed due to shadowing
			GlStateManager.translate(0, 0, zLevel());
			componentDraw(partialTicks, isMouseHover(mouseX, mouseY));
			
		popMatrix();
		//@formatter:on
		
	}
	
	/**
	 * Actually draw the component. It is already translated and scaled to the
	 * correct position.
	 * 
	 * @param mouseHover
	 *            TODO
	 */
	protected abstract void componentDraw(float partialTicks, boolean mouseHover);
	
	public final void mouseClicked(float mouseX, float mouseY, int button) {
		if (isMouseHover(mouseX, mouseY)) {
			click(button);
		}
	}
	
	protected void click(int button) {
		
	}
	
	public void keyPressed(int keyCode) {}
	
	/**
	 * Called when the mouse hovers over the component. Returns the tooltip
	 * lines to draw. If there is no tooltip, returns null.
	 */
	public List<String> getTooltip(float mouseX, float mouseY) {
		return null;
	}
	
	public boolean isMouseHover(float mouseX, float mouseY) {
		
		Measurement min = coordinates().times(1f / scaleFactor());
		Measurement max = min.plus(fromPixels(width() / scaleFactor(), height() / scaleFactor()));
		
		return mouseX >= min.xInPixels() && mouseX <= max.xInPixels() && mouseY >= min.yInPixels()
				&& mouseY <= max.yInPixels() && visible;
		
	}
	
	// Delegates to transform
	
	public Measurement coordinates() {
		return transform.coordinates();
	}
	
	public StartingPosition position() {
		return transform.position();
	}
	
	public void setPosition(StartingPosition position) {
		transform.setPosition(position);
	}
	
	public Measurement offset() {
		return transform.offset();
	}
	
	public void setOffset(Measurement offset) {
		transform.setOffset(offset);
	}
	
	public void addOffset(Measurement offset) {
		transform.addOffset(offset);
	}
	
	public float offsetScale() {
		return transform.offsetScale();
	}
	
	public void setOffsetScale(float scale) {
		transform.setOffsetScale(scale);
	}
	
	public float scale() {
		return transform.scale();
	}
	
	public void setScale(float scale) {
		transform.setScale(scale);
	}
	
	public float zLevel() {
		return transform.zLevel();
	}
	
	public void setZLevel(float zLevel) {
		transform.setZLevel(zLevel);
	}
	
	public Frame getFrame() {
		return transform.getFrame();
	}
	
	public void setFrame(Frame frame) {
		transform.setFrame(frame);
	}
	
}
