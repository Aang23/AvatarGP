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
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.gorecore.tree.ArgumentList;
import com.crowsofwar.gorecore.tree.ArgumentPlayerName;
import com.crowsofwar.gorecore.tree.ArgumentRangeInteger;
import com.crowsofwar.gorecore.tree.CommandCall;
import com.crowsofwar.gorecore.tree.IArgument;
import com.crowsofwar.gorecore.tree.ICommandNode;
import com.crowsofwar.gorecore.tree.NodeFunctional;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class NodeAbilitySet extends NodeFunctional {
	
	private final IArgument<String> argPlayer;
	private final IArgument<BendingAbility> argAbility;
	private final IArgument<Integer> argSetTo;
	
	public NodeAbilitySet() {
		super("set", true);
		argPlayer = addArgument(new ArgumentPlayerName("player"));
		argAbility = addArgument(new ArgumentAbility("ability"));
		argSetTo = addArgument(new ArgumentRangeInteger("value", 0, 100));
	}
	
	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		
		ArgumentList args = call.popArguments(this);
		String player = args.get(argPlayer);
		BendingAbility ability = args.get(argAbility);
		int setXp = args.get(argSetTo);
		
		if (setXp >= 0 && setXp <= 100) {
			AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(call.getFrom().getEntityWorld(), player);
			if (data != null) {
				
				data.getAbilityData(ability).setXp(setXp);
				AvatarChatMessages.MSG_ABILITY_SET_SUCCESS.send(call.getFrom(), player, ability.getName(),
						setXp);
			}
		} else {
			AvatarChatMessages.MSG_ABILITY_SET_RANGE.send(call.getFrom());
		}
		
		return null;
	}
	
}
