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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class GuiButtonScrolls extends GuiButton {
	
	private final Slot scrollSlot;
	
	public GuiButtonScrolls(Container container, int buttonId, int x, int y) {
		super(buttonId, x, y, 18, 18, "");
		this.scrollSlot = container.inventorySlots.get(0);
	}

	// drawButton
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		
		this.enabled = scrollSlot.getHasStack();
		
		if (this.visible) {
			
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(AvatarUiTextures.skillsGui);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= x && mouseY >= y
					&& mouseX < x + this.width && mouseY < y + this.height;
			
			int offset = this.getHoverState(this.hovered);
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			
			//@formatter:off
			
			drawTexturedModalRect(x, y,
					58 + offset * 18, 0,
					width, height);
			
			//@formatter:on
			
			mouseDragged(mc, mouseX, mouseY);
			
			int j = 14737632;
			
			if (packedFGColour != 0) {
				j = packedFGColour;
			} else if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = 16777120;
			}
			
		}
		
	}
	
}
