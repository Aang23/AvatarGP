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
package com.crowsofwar.avatar.common.entity.data;

import static com.crowsofwar.avatar.common.config.ConfigMobs.MOBS_CONFIG;

import net.minecraft.entity.EntityCreature;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AnimalCondition {
	
	private final DataParameter<Float> syncFood;
	private final DataParameter<Integer> syncDomestication;
	private final DataParameter<Integer> syncAge;
	private final EntityCreature animal;
	private final float maxFoodPoints, foodRegenPoints;
	
	private float lastDistance;
	private int breedTimer;
	private boolean sterile;
	
	public AnimalCondition(EntityCreature animal, float maxFoodPoints, float foodRegenPoints,
			DataParameter<Float> syncFood, DataParameter<Integer> syncDomestication,
			DataParameter<Integer> syncAge) {
		this.animal = animal;
		this.syncDomestication = syncDomestication;
		this.syncFood = syncFood;
		this.syncAge = syncAge;
		this.maxFoodPoints = maxFoodPoints;
		this.foodRegenPoints = foodRegenPoints;
		
		this.lastDistance = animal.distanceWalkedModified;
		this.breedTimer = -1;
		this.sterile = true;
		
	}
	
	public void writeToNbt(NBTTagCompound nbt) {
		nbt.setFloat("FoodPoints", getFoodPoints());
		nbt.setInteger("Domestication", getDomestication());
		nbt.setInteger("Age", getAge());
		nbt.setInteger("BreedTimer", getBreedTimer());
		nbt.setBoolean("Sterile", isSterile());
	}
	
	public void readFromNbt(NBTTagCompound nbt) {
		setFoodPoints(nbt.getFloat("FoodPoints"));
		setDomestication(nbt.getInteger("Domestication"));
		setAge(nbt.getInteger("Age"));
		setBreedTimer(nbt.getInteger("BreedTimer"));
		setSterile(nbt.getBoolean("Sterile"));
	}
	
	public void onUpdate() {
		float distance = animal.distanceWalkedModified;
		// Rarely, an error can occur where distance is NaN (divide by 0)
		if (Float.isNaN(distance)) {
			distance = lastDistance;
		}
		float diff = distance - lastDistance;
		addHunger(diff * 0.1f);
		
		lastDistance = distance;
		
		if (!animal.world.isRemote) {
			boolean enoughFood = getFoodPoints() >= foodRegenPoints;
			boolean correctTime = animal.ticksExisted % 40 == 0;
			if (enoughFood && correctTime) {
				animal.heal(1);
				addHunger(1);
			}
			addAge(1);
			if (!isSterile() && isAdult()) {
				addBreedTimer(-1);
			}
		}
		
	}
	
	// ================================================================================
	// DOMESTICATION
	// ================================================================================
	
	public int getDomestication() {
		return animal.getDataManager().get(syncDomestication);
	}
	
	public void setDomestication(int domestication) {
		if (domestication < 0) domestication = 0;
		if (domestication > 1000) domestication = 1000;
		animal.getDataManager().set(syncDomestication, domestication);
	}
	
	public void addDomestication(int domestication) {
		setDomestication(getDomestication() + domestication);
	}
	
	public boolean canHaveOwner() {
		return getDomestication() >= MOBS_CONFIG.bisonOwnableTameness;
	}
	
	public int getMaxRiders() {
		
		if (!isAdult()) {
			return 0;
		}
		
		if (canHaveOwner()) {
			
			double pctToTame = 1.0 * (getDomestication() - MOBS_CONFIG.bisonOwnableTameness)
					/ (1000 - MOBS_CONFIG.bisonOwnableTameness);
			return 1 + (int) (pctToTame * 4);
			
		} else if (getDomestication() >= MOBS_CONFIG.bisonRiderTameness) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public boolean isFullyDomesticated() {
		return getDomestication() == 1000;
	}
	
	// ================================================================================
	// FOOD POINTS
	// ================================================================================
	
	public float getFoodPoints() {
		return animal.getDataManager().get(syncFood);
	}
	
	public void setFoodPoints(float points) {
		animal.getDataManager().set(syncFood, points);
	}
	
	/**
	 * Make the animal hungrier by the given amount (subtracting from
	 * foodPoints)
	 * 
	 * @see #addFood(float)
	 */
	public void addHunger(float hunger) {
		addFood(-hunger);
	}
	
	/**
	 * Adds food points to the animal (adding to foodPoints).
	 */
	public void addFood(float food) {
		float foodPoints = getFoodPoints();
		foodPoints += food;
		if (foodPoints < 0) {
			foodPoints = 0;
		}
		if (foodPoints > maxFoodPoints) {
			foodPoints = maxFoodPoints;
		}
		setFoodPoints(foodPoints);
	}
	
	public float getSpeedMultiplier() {
		return 0.6f + 0.4f * getFoodPoints() / maxFoodPoints;
	}
	
	// ================================================================================
	// AGE
	// ================================================================================
	
	/**
	 * Gets the age in ticks
	 */
	public int getAge() {
		return animal.getDataManager().get(syncAge);
	}
	
	public void setAge(int age) {
		if (age < 0) age = 0;
		animal.getDataManager().set(syncAge, age);
	}
	
	public void addAge(int age) {
		setAge(getAge() + age);
	}
	
	public float getAgeDays() {
		return getAge() / 24000f;
	}
	
	public void setAgeDays(float days) {
		setAge((int) (days * 24000));
	}
	
	public float getSizeMultiplier() {
		return isAdult() ? 1 : 0.1f + getAgeDays() / getAdultAge() * 0.9f;
	}
	
	public boolean isAdult() {
		return getAgeDays() >= getAdultAge();
	}
	
	/**
	 * Returns the number of days it takes to become an adult.
	 */
	public int getAdultAge() {
		return 3;
	}
	
	// ================================================================================
	// BREEDING
	// ================================================================================
	
	/**
	 * Get the breed timer. If {@link #isSterile() is sterile}, returns -1.
	 */
	public int getBreedTimer() {
		return sterile ? -1 : breedTimer;
	}
	
	/**
	 * Set the breedTimer. Ignored if {@link #isSterile() is sterile}.
	 */
	public void setBreedTimer(int breedTimer) {
		if (!isSterile()) {
			if (breedTimer < 0) breedTimer = 0;
			this.breedTimer = breedTimer;
		}
	}
	
	public void addBreedTimer(int amount) {
		setBreedTimer(getBreedTimer() + amount);
	}
	
	public boolean isSterile() {
		return sterile;
	}
	
	public void setSterile(boolean sterile) {
		this.sterile = sterile;
	}
	
}
