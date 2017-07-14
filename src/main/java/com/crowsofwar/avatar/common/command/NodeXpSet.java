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

import static com.crowsofwar.avatar.common.AvatarChatMessages.MSG_XPSET_SUCCESS;

import java.util.List;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.gorecore.tree.ArgumentDirect;
import com.crowsofwar.gorecore.tree.ArgumentList;
import com.crowsofwar.gorecore.tree.ArgumentOptions;
import com.crowsofwar.gorecore.tree.ArgumentPlayerName;
import com.crowsofwar.gorecore.tree.CommandCall;
import com.crowsofwar.gorecore.tree.IArgument;
import com.crowsofwar.gorecore.tree.ICommandNode;
import com.crowsofwar.gorecore.tree.ITypeConverter;
import com.crowsofwar.gorecore.tree.NodeFunctional;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class NodeXpSet extends NodeFunctional {
	
	private final IArgument<String> argPlayerName;
	private final IArgument<BendingAbility> argAbility;
	private final IArgument<String> argSpecification;
	private final IArgument<Float> argNewXp;
	
	public NodeXpSet() {
		super("xp", true);
		
		argPlayerName = new ArgumentPlayerName("player");
		argAbility = new ArgumentAbility("ability");
		argSpecification = new ArgumentOptions<>(ITypeConverter.CONVERTER_STRING, "specification", //
				"locked", "lvl1", "lvl2", "lvl3", "lvl4_1", "lvl4_2");
		argNewXp = new ArgumentDirect<>("xp", ITypeConverter.CONVERTER_FLOAT, 0f);
		
		addArguments(argPlayerName, argAbility, argSpecification, argNewXp);
		
	}
	
	@Override
	protected ICommandNode doFunction(CommandCall call, List<String> options) {
		
		ArgumentList args = call.popArguments(this);
		String playerName = args.get(argPlayerName);
		BendingAbility ability = args.get(argAbility);
		String specification = args.get(argSpecification);
		float xp = args.get(argNewXp);
		
		BendingData data = AvatarPlayerData.fetcher().fetch(call.getFrom().getEntityWorld(), playerName);
		AbilityData abilityData = data.getAbilityData(ability);
		
		int level;
		AbilityTreePath path = AbilityTreePath.MAIN;
		
		if (specification.equals("locked")) {
			level = -1;
			xp = 0;
		} else {
			String secondPart = specification.substring("lvl".length());
			level = Integer.parseInt(secondPart.charAt(0) + "") - 1;
			
			if (level == 3) {
				String pathStr = secondPart.substring("n_".length());
				int index = Integer.parseInt(pathStr);
				path = AbilityTreePath.values()[index];
			}
			
		}
		
		abilityData.setLevel(level);
		abilityData.setXp(xp);
		abilityData.setPath(path);
		
		MSG_XPSET_SUCCESS.send(call.getFrom(), playerName, ability.getName(), specification);
		
		return null;
		
	}
	
}
