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

import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.gorecore.tree.IArgument;

import net.minecraft.command.ICommandSender;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ArgumentBendingController implements IArgument<BendingController> {
	
	private final String name;
	
	public ArgumentBendingController(String name) {
		this.name = name;
	}
	
	@Override
	public boolean isOptional() {
		return false;
	}
	
	@Override
	public BendingController getDefaultValue() {
		return null;
	}
	
	@Override
	public BendingController convert(String input) {
		return BendingManager.getBending(input.toLowerCase());
	}
	
	@Override
	public String getArgumentName() {
		return name;
	}
	
	@Override
	public String getHelpString() {
		String out = "<";
		for (BendingController bc : BendingManager.allBending()) {
			out += bc.getControllerName() + "|";
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
		for (BendingController bc : BendingManager.allBending())
			out.add(bc.getControllerName());
		return out;
	}
	
}
