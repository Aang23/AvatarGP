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
package com.crowsofwar.gorecore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.crowsofwar.gorecore.config.convert.ConverterRegistry;
import com.crowsofwar.gorecore.format.ChatSender;
import com.crowsofwar.gorecore.proxy.GoreCoreCommonProxy;
import com.crowsofwar.gorecore.settings.GoreCoreModConfig;
import com.crowsofwar.gorecore.tree.test.GoreCoreChatMessages;
import com.crowsofwar.gorecore.tree.test.GoreCoreCommand;
import com.crowsofwar.gorecore.tree.test.TreeTest;
import com.crowsofwar.gorecore.util.AccountUUIDs;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = GoreCore.MOD_ID, name = GoreCore.MOD_NAME, version = GoreCore.MOD_VERSION)
public class GoreCore {
	
	public static final String MOD_ID = "gorecore";
	public static final String MOD_NAME = "GoreCore";
	public static final String MOD_VERSION = "1.11.2-0.4.2";
	
	@SidedProxy(clientSide = "com.crowsofwar.gorecore.proxy.GoreCoreClientProxy", serverSide = "com.crowsofwar.gorecore.proxy.GoreCoreCommonProxy")
	public static GoreCoreCommonProxy proxy;
	
	public static GoreCoreModConfig config;
	
	public static Logger LOGGER = LogManager.getLogger("GoreCore");
	
	public static final boolean IS_DEOBFUSCATED = Entity.class.getSimpleName().equals("Entity");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new GoreCoreModConfig(event);
		ConverterRegistry.addDefaultConverters();
		
		AccountUUIDs.readCache();
		GoreCoreChatMessages.register();
		
		proxy.sideSpecifics();
		
		ChatSender.load();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> AccountUUIDs.saveCache()));
		
	}
	
	@EventHandler
	public void onServerLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new TreeTest()); // TODO remove when testing
														// is over
		event.registerServerCommand(new GoreCoreCommand());
	}
	
}
