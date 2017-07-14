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

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PreviewWarningGui extends GuiScreen {
	
	@Override
	public void initGui() {
		this.buttonList.clear();
		
		this.buttonList
				.add(new GuiButton(0, (width - 200) / 2, height - height / 5, 200, 20, "To Main Menu"));
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiMainMenu());
		} else if (button.id == 1) {
			this.mc.shutdown();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		// @formatter:off
		String[] lines = {
			"Warning: Avatar Mod Preview Version",
			"",
			"You are running a preview version of the Avatar Mod.",
			"This is made so fans can get a glimpse of upcoming releases,",
			"and so people can critique/suggest tweaks to the mod.",
			"",
			"While you are not required to, I would really appreciate it",
			"if you gave some feedback on the new changes in the form of",
			"an e-mail or a forum post. I'll use your suggestions in the",
			"OFFICIAL release to make the mod more fun.",
			"",
			"Thanks, CrowsOfWar",
			"ofwarcrows@gmail.com"
		};
		// @formatter:on
		
		int y = height / 6;
		for (String ln : lines) {
			drawString(fontRenderer, ln, (width - fontRenderer.getStringWidth(ln)) / 2, y, 0xffffff);
			y += fontRenderer.FONT_HEIGHT + 2;
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}