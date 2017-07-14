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
package com.crowsofwar.avatar.client.gui;

import static com.crowsofwar.avatar.client.gui.RadialMenu.*;
import static net.minecraft.client.renderer.GlStateManager.*;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.gui.MenuTheme;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Holds information for the RadialMenu about a segment. Contains information on
 * its rotation (position), and whether it's clicked.
 *
 */
public class RadialSegment extends Gui {
	
	private final RadialMenu gui;
	private final MenuTheme theme;
	private final Minecraft mc;
	private final float angle;
	private final int index;
	private final BendingAbility ability;
	
	public RadialSegment(RadialMenu gui, MenuTheme theme, int index, BendingAbility ability) {
		this.gui = gui;
		this.angle = 22.5f + index * 45;
		this.index = index;
		this.ability = ability;
		this.theme = theme;
		this.mc = Minecraft.getMinecraft();
	}
	
	/**
	 * Returns whether the mouse is currently hovering
	 * 
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public boolean isMouseHover(int mouseX, int mouseY, ScaledResolution resolution) {
		
		int mouseCenteredX = mouseX - resolution.getScaledWidth() / 2;
		int mouseCenteredY = mouseY - resolution.getScaledHeight() / 2;
		double r = Math.sqrt(mouseCenteredX * mouseCenteredX + mouseCenteredY * mouseCenteredY)
				/ RadialMenu.menuScale;
		double currentAngle = Math.toDegrees(Math.atan2(mouseCenteredY, mouseCenteredX)) + 90;
		double minAngle = angle - 45;
		if (minAngle < 0) minAngle += 360;
		double maxAngle = angle;
		boolean addCurrentAngle = currentAngle < 0;
		if (minAngle > maxAngle) {
			maxAngle += 360;
			addCurrentAngle = true;
		}
		if (addCurrentAngle) currentAngle += 360;
		
		return r >= 100 && r <= 300 && currentAngle >= minAngle && currentAngle <= maxAngle;
	}
	
	public float getAngle() {
		return angle;
	}
	
	/**
	 * Draw this radial segment.
	 * 
	 * @param hover
	 *            Whether mouse is over it
	 * @param resolution
	 *            Resolution MC is at
	 * @param alpha
	 *            Alpha of the image; 0 for completely transparent and 1 for
	 *            completely opaque
	 * @param scale
	 *            Scale of the image, 1 for no change
	 */
	//@formatter:off
	public void draw(boolean hover, ScaledResolution resolution, float alpha, float scale) {
		
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();
		
		GlStateManager.enableBlend();
		
		// Draw background & edge
		GlStateManager.pushMatrix();
			GlStateManager.translate(width / 2f, height / 2f, 0); 	// Re-center origin
			GlStateManager.scale(menuScale, menuScale, menuScale); 	// Scale all following arguments
			GlStateManager.scale(.9f, .9f, 1);
			GlStateManager.rotate(this.getAngle(), 0, 0, 1);		// All transform operations and the image are rotated
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.translate(-segmentX, -segmentY, 0);		// Offset the image to the correct
																	// center point
			// Draw background
			GlStateManager.color(theme.getBackground().getRed(hover) / 255f,
					theme.getBackground().getGreen(hover) / 255f, theme.getBackground().getBlue(hover) / 255f, alpha);
			mc.getTextureManager().bindTexture(AvatarUiTextures.radialMenu);
			drawTexturedModalRect(0, 0, 0, 0, 256, 256);
			
		GlStateManager.popMatrix();
		
		// Draw icon
		GlStateManager.pushMatrix();
			float iconScale = .4f;
			float angle = this.getAngle() - 20f;
			angle %= 360;
			
			// Recenter over origin
			translate((width - 256 * iconScale) / 2f, (height - 256 * iconScale) / 2f, 0);
			// Translate to the correct position
			rotate(angle, 0, 0, 1);
			translate(0, -200 * .9f * menuScale * scale, 0);
			rotate(-angle, 0, 0, 1);
			// Last transform before draw
			scale(iconScale, iconScale, 1);
			
			// Ensure icon is not overlapped
			GlStateManager.translate(0, 0, 2);
			
			if (ability != null) {
				mc.getTextureManager().bindTexture(AvatarUiTextures.getAbilityTexture(ability));
				drawTexturedModalRect(0, 0, 0, 0, 256, 256);
			}
			
//			float darkenBy = 0.05f;
//			float r = theme.getIcon().getRed(hover) / 255f - darkenBy;
//			float g = theme.getIcon().getGreen(hover) / 255f - darkenBy;
//			float b = theme.getIcon().getBlue(hover) / 255f - darkenBy;
//			float avg = (r + g + b) / 3;
//			GlStateManager.color(avg, avg, avg, alpha);
			
			// TODO Blurred versions
//			mc.getTextureManager().bindTexture(AvatarUiTextures.blurredIcons);
//			drawTexturedModalRect(0, 0, getTextureU(), getTextureV(), 32, 32);
			
		GlStateManager.popMatrix();
		
	}
	//@formatter:on
	
}
