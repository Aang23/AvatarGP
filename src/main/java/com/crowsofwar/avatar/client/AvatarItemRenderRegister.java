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
package com.crowsofwar.avatar.client;

import com.crowsofwar.avatar.common.item.AvatarItem;
import com.crowsofwar.avatar.common.item.AvatarItems;
import com.crowsofwar.avatar.common.item.ItemScroll.ScrollType;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AvatarItemRenderRegister {
	
	private static ModelResourceLocation[] locationsRegular, locationsGlow;
	
	public static void register() {
		
		MinecraftForge.EVENT_BUS.register(new AvatarItemRenderRegister());
		
		// Setup scrolls
		locationsRegular = new ModelResourceLocation[ScrollType.amount()];
		locationsGlow = new ModelResourceLocation[ScrollType.amount()];
		
		for (int i = 0; i < ScrollType.amount(); i++) {

			ScrollType type = ScrollType.fromId(i);

			locationsRegular[i] = new ModelResourceLocation("avatarmod:scroll_" + type.displayName(),
					"inventory");
			locationsGlow[i] = new ModelResourceLocation("avatarmod:scroll_" + type.displayName() + "_glow",
					"inventory");

			setCustomModelResourceLocation(AvatarItems.itemScroll, i, locationsGlow[i]);
			setCustomModelResourceLocation(AvatarItems.itemScroll, i, locationsRegular[i]);

		}
		
		for (int i = 0; i <= 5; i++) {
			register(AvatarItems.itemWaterPouch, i);
		}
		register(AvatarItems.itemBisonWhistle);
		for (int i = 0; i <= 3; i++) {
			register(AvatarItems.itemBisonArmor, i);
			register(AvatarItems.itemBisonSaddle, i);
		}
		
	}

	/**
	 * Registers the specified item with the given metadata(s). Maps it to
	 * {unlocalizedName}.json. Note that if no metadata is specified, the item
	 * will not be registered.
	 */
	private static void register(AvatarItem item, int... metadata) {

		if (metadata.length == 0) {
			metadata = new int[1];
		}
		
		for (int meta : metadata) {
			ModelResourceLocation mrl = new ModelResourceLocation("avatarmod:" + item.getModelName(meta),
					"inventory");
			
			ModelLoader.setCustomModelResourceLocation(item.item(), meta, mrl);

		}
		
	}
	
	@SubscribeEvent
	public void modelBake(ModelBakeEvent e) {
		
		for (int i = 0; i < ScrollType.amount(); i++) {
			
			ModelResourceLocation mrlRegular = locationsRegular[i];
			ModelResourceLocation mrlGlow = locationsGlow[i];

			IBakedModel currentModel = e.getModelRegistry().getObject(mrlRegular);
			ScrollsPerspectiveModel customModel = new ScrollsPerspectiveModel(mrlRegular, mrlGlow,
					currentModel, e.getModelRegistry().getObject(mrlGlow));
			e.getModelRegistry().putObject(mrlRegular, customModel);

		}
		
	}
	
}
