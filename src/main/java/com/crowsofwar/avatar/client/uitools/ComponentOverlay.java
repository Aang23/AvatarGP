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

import static com.crowsofwar.avatar.client.uitools.ScreenInfo.screenHeight;
import static com.crowsofwar.avatar.client.uitools.ScreenInfo.screenWidth;

import com.crowsofwar.avatar.client.gui.AvatarUiTextures;

import net.minecraft.client.renderer.GlStateManager;

/**
 * UI component which covers the whole screen in a background shade
 * 
 * @author CrowsOfWar
 */
public class ComponentOverlay extends UiComponent {
	
	@Override
	protected float componentWidth() {
		return screenWidth();
	}
	
	@Override
	protected float componentHeight() {
		return screenHeight();
	}
	
	@Override
	protected void componentDraw(float partialTicks, boolean mouseHover) {
		
		mc.renderEngine.bindTexture(AvatarUiTextures.WHITE);
		GlStateManager.enableBlend();
		GlStateManager.color(0, 0, 0, .5f);
		drawTexturedModalRect(0, 0, 0, 0, screenWidth(), screenHeight());
		
		GlStateManager.disableBlend();
	}
	
}
