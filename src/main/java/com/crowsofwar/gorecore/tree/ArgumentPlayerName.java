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
package com.crowsofwar.gorecore.tree;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;

/**
 * An argument for a player's username. Supports tab completion if the player is
 * currently in the world. However, players not in the world can be specified.
 * 
 * @author CrowsOfWar
 */
public class ArgumentPlayerName implements IArgument<String> {
	
	private final String name;
	
	public ArgumentPlayerName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean isOptional() {
		return false;
	}
	
	@Override
	public String getDefaultValue() {
		return null;
	}
	
	@Override
	public String convert(String input) {
		return input;
	}
	
	@Override
	public String getArgumentName() {
		return name;
	}
	
	@Override
	public String getHelpString() {
		return "<playername>";
	}
	
	@Override
	public String getSpecificationString() {
		return "<" + getArgumentName() + ">";
	}
	
	@Override
	public List<String> getCompletionSuggestions(ICommandSender sender, String currentInput) {
		World world = sender.getEntityWorld();
		List<String> suggestions = new ArrayList<>();
		
		world.playerEntities.forEach(player -> {
			suggestions.add(player.getName());
		});
		
		if (!suggestions.get(0).toLowerCase().startsWith(currentInput.toLowerCase()))
			return new ArrayList<>();
		return suggestions;
	}
	
}
