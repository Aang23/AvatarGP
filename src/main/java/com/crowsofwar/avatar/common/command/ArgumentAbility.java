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

import java.util.ArrayList;
import java.util.List;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.gorecore.tree.IArgument;
import com.crowsofwar.gorecore.tree.TreeCommandException;

import net.minecraft.command.ICommandSender;

/**
 * Argument for an ability specified by its {@link BendingAbility#getName()
 * internal name}. Will give null if incorrect input
 * 
 * @author CrowsOfWar
 */
public class ArgumentAbility implements IArgument<BendingAbility> {
	
	private final String name;
	
	public ArgumentAbility(String name) {
		this.name = name;
	}
	
	@Override
	public boolean isOptional() {
		return false;
	}
	
	@Override
	public BendingAbility getDefaultValue() {
		return null;
	}
	
	@Override
	public BendingAbility convert(String input) {
		
		for (BendingAbility ability : BendingManager.allAbilities()) {
			if (ability.getName().equals(input)) {
				return ability;
			}
		}
		
		throw new TreeCommandException("avatar.cmd.noAbility", input);
		
	}
	
	@Override
	public String getArgumentName() {
		return name;
	}
	
	@Override
	public String getHelpString() {
		String out = "<";
		for (BendingAbility ability : BendingManager.allAbilities()) {
			out += ability.getName() + "|";
		}
		return out.substring(0, out.length() - 1) + ">";
	}
	
	@Override
	public String getSpecificationString() {
		return "<" + name + ">";
	}
	
	@Override
	public List<String> getCompletionSuggestions(ICommandSender sender, String currentInput) {
		List<String> out = new ArrayList<>();
		for (BendingAbility ability : BendingManager.allAbilities()) {
			out.add(ability.getName());
		}
		return out;
	}
	
}
