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

import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.client.gui.AvatarUiTextures;
import com.crowsofwar.avatar.client.uitools.UiComponent;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.network.packets.PacketSSkillsMenu;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ComponentBendingTab extends UiComponent {
	
	private final BendingType type;
	private final boolean fullTab;
	
	private final ResourceLocation bendingIconLocation;
	
	public ComponentBendingTab(BendingType type, boolean fullTab) {
		
		bendingIconLocation = new ResourceLocation(
				"avatarmod:textures/gui/tab/" + type.name().toLowerCase() + ".png");
		
		this.type = type;
		this.fullTab = fullTab;
		
	}
	
	@Override
	protected void click(int button) {
		AvatarMod.network.sendToServer(new PacketSSkillsMenu(type));
	}
	
	@Override
	protected float componentWidth() {
		return 20;
	}
	
	@Override
	protected float componentHeight() {
		return fullTab ? 23 : 20;
	}
	
	@Override
	protected void componentDraw(float partialTicks, boolean mouseHover) {
		
		// Draw tab image
		mc.renderEngine.bindTexture(AvatarUiTextures.skillsGui);
		int tabU = fullTab ? 236 : 216;
		int tabV = mouseHover ? 23 : 0;
		drawTexturedModalRect(0, 0, tabU, tabV, 20, fullTab ? 23 : 20);
		
		// Draw component image
		mc.renderEngine.bindTexture(bendingIconLocation);
		GlStateManager.pushMatrix();
		
		double iconScale = 0.75;
		GlStateManager.translate((20 - 20 * iconScale) / 2, (20 - 20 * iconScale) / 2, 0);
		GlStateManager.scale(20.0 / 256, 20.0 / 256, 1);
		GlStateManager.scale(iconScale, iconScale, 1);
		drawTexturedModalRect(0, fullTab ? -3 : 0, 0, 0, 256, 256);
		GlStateManager.popMatrix();
		
	}
	
}
