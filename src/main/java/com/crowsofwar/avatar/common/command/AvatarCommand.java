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

import com.crowsofwar.avatar.common.AvatarChatMessages;
import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.gorecore.tree.ICommandNode;
import com.crowsofwar.gorecore.tree.ITypeConverter;
import com.crowsofwar.gorecore.tree.NodeBranch;
import com.crowsofwar.gorecore.tree.TreeCommand;

import java.util.Arrays;
import java.util.List;

public class AvatarCommand extends TreeCommand {
	
	public static final List<BendingController>[] CONTROLLER_BENDING_OPTIONS;
	static {
		CONTROLLER_BENDING_OPTIONS = new List[BendingManager.allBending().size() + 1];
		CONTROLLER_BENDING_OPTIONS[0] = BendingManager.allBending();
		for (int i = 1; i < CONTROLLER_BENDING_OPTIONS.length; i++) {
			CONTROLLER_BENDING_OPTIONS[i] = Arrays.asList(BendingManager.allBending().get(i - 1));
		}
	}
	
	public static final ITypeConverter<List<BendingController>> CONVERTER_BENDING = new ITypeConverter<List<BendingController>>() {
		
		@Override
		public List<BendingController> convert(String str) {
			return str.equals("all") ? BendingManager.allBending()
					: Arrays.asList(BendingManager.getBending(str.toLowerCase()));
		}
		
		@Override
		public String toString(List<BendingController> obj) {
			return obj.equals(BendingManager.allBending()) ? "all" : obj.get(0).getControllerName();
		}
		
		@Override
		public String getTypeName() {
			return "Bending";
		}
		
	};
	
	public AvatarCommand() {
		super(AvatarChatMessages.CFG);
	}
	
	@Override
	public String getName() {
		return "avatar";
	}
	
	@Override
	protected ICommandNode[] addCommands() {
		
		NodeBendingList bendingList = new NodeBendingList();
		NodeBendingAdd bendingAdd = new NodeBendingAdd();
		NodeBendingRemove bendingRemove = new NodeBendingRemove();
		NodeBranch branchBending = new NodeBranch(AvatarChatMessages.MSG_BENDING_BRANCH_INFO, "bending",
				bendingList, bendingAdd, bendingRemove);
		
		NodeBranch branchAbility = new NodeBranch(branchHelpDefault, "ability", new NodeAbilityGet(),
				new NodeAbilitySet());
		
		return new ICommandNode[] { branchBending, new NodeConfig(), branchAbility, new NodeXpSet() };
		
	}
	
}
