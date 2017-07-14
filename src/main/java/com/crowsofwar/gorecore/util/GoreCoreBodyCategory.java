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
package com.crowsofwar.gorecore.util;

import java.security.InvalidParameterException;

/**
 * Keeps track of armor type indices, and can be used for other things.
 * 
 * @author CrowsOfWar
 */
public enum GoreCoreBodyCategory {
	
	FEET,
	LEGS,
	CHEST,
	HEAD;
	
	/**
	 * Gets the integer used in EntityLivingBase's equipment methods for armor.
	 */
	public final int armorIndex() {
		return ordinal() + 1;
	}
	
	/**
	 * Gets the armor type integer used by ItemArmor.
	 */
	public final int armorType() {
		return -ordinal() + 3;
	}
	
	/**
	 * Returns the body category for the given index used in EntityLivingBase's
	 * equipment methods.
	 * 
	 * @param armorIndex
	 *            The index used in equipment methods
	 * @return The armor type for that index
	 * @throws InvalidParameterException
	 *             Thrown when the armor index is not in the correct range (i.e.
	 *             1-4)
	 */
	public static final GoreCoreBodyCategory getCategoryForArmorIndex(int armorIndex) {
		for (GoreCoreBodyCategory cat : values()) {
			if (cat.armorIndex() == armorIndex) return cat;
		}
		
		throw new InvalidParameterException(
				"Armor index must be the index used in EntityLivingBase's equipment " + "methods - 1 to 4!");
	}
	
	/**
	 * Returns the body category for that type of armor, as specified in
	 * ItemArmor's fields.
	 * 
	 * @param armorType
	 *            The type of armor
	 * @return The body category for that armor type
	 * @throws InvalidParameterException
	 *             Thrown if the armor type is invalid (i.e. is not in the range
	 *             of 0-3)
	 */
	public static final GoreCoreBodyCategory getCategoryForArmorType(int armorType) {
		for (GoreCoreBodyCategory cat : values()) {
			if (cat.armorType() == armorType) return cat;
		}
		
		throw new InvalidParameterException(
				"Armor type must be armorType as specified in ItemArmor - 0 to 3!");
	}
	
}
