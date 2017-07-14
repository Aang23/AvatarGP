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
package com.crowsofwar.avatar.common.data;

import java.util.List;
import java.util.Map;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.bending.StatusControl;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public interface BendingData {
	
	// ================================================================================
	// BENDING CONTROLLERS
	// ================================================================================
	
	/**
	 * Check if the player has that bending controller
	 */
	boolean hasBending(BendingController bending);
	
	/**
	 * Check if the player has that type of bending
	 */
	boolean hasBending(BendingType type);
	
	/**
	 * If the bending controller is not already present, adds the bending
	 * controller.
	 * <p>
	 * Also adds the state if it isn't present.
	 */
	void addBending(BendingController bending);
	
	/**
	 * If the bending controller is not already present, adds the bending
	 * controller.
	 */
	void addBending(BendingType type);
	
	/**
	 * Remove the specified bending controller and its associated state. Please
	 * note, this will be saved, so is permanent (unless another bending
	 * controller is added).
	 */
	void removeBending(BendingController bending);
	
	/**
	 * Remove the bending controller and its state with that type.
	 * 
	 * @see #removeBending(BendingController)
	 */
	void removeBending(BendingType type);
	
	List<BendingController> getAllBending();
	
	void setAllBending(List<BendingController> controller);
	
	void clearBending();
	
	// ================================================================================
	// ACTIVE BENDING
	// ================================================================================
	
	/**
	 * Gets the currently in-use bending controller. Null if player has no
	 * bending
	 */
	BendingController getActiveBending();
	
	/**
	 * Gets the type of the in-use bending controller. Null if the player has no
	 * bending
	 */
	BendingType getActiveBendingType();
	
	/**
	 * Set the currently in-use bending. If null, will be rejected
	 */
	void setActiveBending(BendingController controller);
	
	/**
	 * Set the currently in-use type. If null, will be rejected
	 */
	void setActiveBendingType(BendingType type);
	
	// ================================================================================
	// STATUS CONTROLS
	// ================================================================================
	
	boolean hasStatusControl(StatusControl control);
	
	void addStatusControl(StatusControl control);
	
	void removeStatusControl(StatusControl control);
	
	List<StatusControl> getAllStatusControls();
	
	void setAllStatusControls(List<StatusControl> controls);
	
	void clearStatusControls();
	
	// ================================================================================
	// ABILITY DATA
	// ================================================================================
	
	boolean hasAbilityData(BendingAbility ability);
	
	/**
	 * Retrieves data about the given ability. Will create data if necessary.
	 */
	AbilityData getAbilityData(BendingAbility ability);
	
	void setAbilityData(BendingAbility ability, AbilityData data);
	
	/**
	 * Gets a list of all ability data contained in this player data.
	 */
	List<AbilityData> getAllAbilityData();
	
	Map<BendingAbility, AbilityData> getAbilityDataMap();
	
	void setAbilityDataMap(Map<BendingAbility, AbilityData> map);
	
	/**
	 * Removes all ability data associations
	 */
	void clearAbilityData();
	
	// ================================================================================
	// CHI
	// ================================================================================
	
	/**
	 * Gets the chi information about the bender
	 */
	Chi chi();
	
	void setChi(Chi chi);
	
	// ================================================================================
	// TICK HANDLERS
	// ================================================================================
	
	boolean hasTickHandler(TickHandler handler);
	
	void addTickHandler(TickHandler handler);
	
	void removeTickHandler(TickHandler handler);
	
	List<TickHandler> getAllTickHandlers();
	
	void setAllTickHandlers(List<TickHandler> handlers);
	
	void clearTickHandlers();
	
	// ================================================================================
	// MISC
	// ================================================================================
	
	MiscData getMiscData();
	
	void setMiscData(MiscData miscData);
	
	float getFallAbsorption();
	
	void setFallAbsorption(float fallAbsorption);
	
	int getTimeInAir();
	
	void setTimeInAir(int time);
	
	int getAbilityCooldown();
	
	void setAbilityCooldown(int cooldown);
	
	void decrementCooldown();
	
	boolean isWallJumping();
	
	void setWallJumping(boolean wallJumping);
	
	int getPetSummonCooldown();
	
	void setPetSummonCooldown(int cooldown);
	
	/**
	 * Save this BendingData
	 */
	void save(DataCategory category);
	
}