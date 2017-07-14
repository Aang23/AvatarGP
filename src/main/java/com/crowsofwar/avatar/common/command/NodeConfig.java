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
package com.crowsofwar.avatar.common.command;

import static com.crowsofwar.avatar.common.AvatarChatMessages.*;

import java.util.List;

import com.crowsofwar.avatar.common.config.ConfigChi;
import com.crowsofwar.avatar.common.config.ConfigClient;
import com.crowsofwar.avatar.common.config.ConfigMobs;
import com.crowsofwar.avatar.common.config.ConfigSkills;
import com.crowsofwar.avatar.common.config.ConfigStats;
import com.crowsofwar.gorecore.config.ConfigurationException;
import com.crowsofwar.gorecore.tree.ArgumentDirect;
import com.crowsofwar.gorecore.tree.ArgumentList;
import com.crowsofwar.gorecore.tree.CommandCall;
import com.crowsofwar.gorecore.tree.IArgument;
import com.crowsofwar.gorecore.tree.ICommandNode;
import com.crowsofwar.gorecore.tree.ITypeConverter;
import com.crowsofwar.gorecore.tree.NodeFunctional;

import net.minecraft.command.ICommandSender;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class NodeConfig extends NodeFunctional {
	
	private final IArgument<String> argKey;
	private final IArgument<String> argVal;
	
	public NodeConfig() {
		super("config", true);
		this.argKey = addArgument(new ArgumentDirect<>("key", ITypeConverter.CONVERTER_STRING, ""));
		this.argVal = addArgument(new ArgumentDirect<>("value", ITypeConverter.CONVERTER_STRING, ""));
	}
	
	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		
		ArgumentList list = call.popArguments(this);
		String key = list.get(argKey);
		String val = list.get(argVal);
		if (key.equals("") || val.equals("")) {
			
			ICommandSender from = call.getFrom();
			boolean exception = false;
			
			try {
				
				ConfigStats.load();
				ConfigSkills.load();
				ConfigClient.load();
				ConfigChi.load();
				ConfigMobs.load();
				
			} catch (ConfigurationException e) {
				
				exception = true;
				MSG_CONFIG_EXCEPTION_1.send(from);
				MSG_CONFIG_EXCEPTION_2.send(from, e.getCause().toString());
				e.printStackTrace();
				
			}
			
			if (!exception) {
				
				MSG_CONFIG_SUCCESS.send(from);
				
			}
			
		} else {
			// AvatarConfig.set(key, new Yaml().load(val));
			// AvatarConfig.save();
		}
		
		return null;
	}
	
}
