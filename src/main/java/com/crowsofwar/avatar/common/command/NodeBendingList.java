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

import java.util.List;

import com.crowsofwar.avatar.common.AvatarChatMessages;
import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.gorecore.tree.ArgumentList;
import com.crowsofwar.gorecore.tree.ArgumentPlayerName;
import com.crowsofwar.gorecore.tree.CommandCall;
import com.crowsofwar.gorecore.tree.IArgument;
import com.crowsofwar.gorecore.tree.ICommandNode;
import com.crowsofwar.gorecore.tree.NodeFunctional;

import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;

public class NodeBendingList extends NodeFunctional {
	
	private final IArgument<String> argPlayerName;
	
	public NodeBendingList() {
		super("list", true);
		this.argPlayerName = new ArgumentPlayerName("player");
		addArgument(argPlayerName);
	}
	
	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		
		ICommandSender sender = call.getFrom();
		World world = sender.getEntityWorld();
		
		ArgumentList args = call.popArguments(this);
		String playerName = args.get(argPlayerName);
		
		AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(world, playerName);
		if (data == null) {
			
			AvatarChatMessages.MSG_PLAYER_DATA_NO_DATA.send(sender, playerName);
			
		} else {
			
			if (!data.getAllBending().isEmpty()) {
				
				List<BendingController> allControllers = data.getAllBending();
				AvatarChatMessages.MSG_BENDING_LIST_TOP.send(sender, playerName, allControllers.size());
				
				for (BendingController controller : allControllers) {
					AvatarChatMessages.MSG_BENDING_LIST_ITEM.send(sender, controller.getControllerName());
				}
				
			} else {
				
				AvatarChatMessages.MSG_BENDING_LIST_NONBENDER.send(sender, playerName);
				
			}
			
		}
		
		return null;
	}
	
}
