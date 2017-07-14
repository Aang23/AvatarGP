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
import static net.minecraft.client.Minecraft.getMinecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Mouse;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;

/**
 * Handles calls to all UI components, so they don't need to be worried about
 * outside their instantiation.
 * 
 * @author CrowsOfWar
 */
public class UiComponentHandler {
	
	private final List<UiComponent> components;
	
	public UiComponentHandler() {
		components = new ArrayList<>();
	}
	
	public UiComponentHandler(UiComponent... components) {
		this();
		this.components.addAll(Arrays.asList(components));
	}
	
	public void add(UiComponent component) {
		components.add(component);
	}
	
	public void draw(float partialTicks, float mouseX, float mouseY) {
		
		List<String> tooltip = null;
		
		for (UiComponent component : components) {
			component.draw(partialTicks, mouseX, mouseY);
			
			if (component.isVisible()) {
				float mx2 = Mouse.getX();
				float my2 = screenHeight() - Mouse.getY();
				
				Measurement coords = component.coordinates();
				if (mx2 >= coords.xInPixels() && mx2 <= coords.xInPixels() + component.width()) {
					if (my2 >= coords.yInPixels() && my2 <= coords.yInPixels() + component.height()) {
						List<String> result = component.getTooltip(mx2, my2);
						if (result != null) {
							tooltip = result;
						}
					}
				}
			}
			
		}
		
		if (tooltip != null) {
			
			int width = screenWidth() / scaleFactor();
			int height = screenHeight() / scaleFactor();
			
			GuiUtils.drawHoveringText(tooltip, (int) mouseX, (int) mouseY, width, height, -1,
					getMinecraft().fontRenderer);
			GlStateManager.disableLighting();
			
		}
		
	}
	
	public void click(float x, float y, int button) {
		for (UiComponent component : components)
			component.mouseClicked(x, y, button);
	}
	
	public void type(int key) {
		for (UiComponent component : components)
			component.keyPressed(key);
	}
	
}
