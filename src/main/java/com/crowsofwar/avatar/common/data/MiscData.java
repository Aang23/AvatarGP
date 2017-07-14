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

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class MiscData {
	
	private final Runnable save;
	
	private float fallAbsorption;
	private int timeInAir;
	private int abilityCooldown;
	private boolean wallJumping;
	private int petSummonCooldown;
	
	public MiscData(Runnable save) {
		this.save = save;
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(fallAbsorption);
		buf.writeInt(timeInAir);
		buf.writeInt(abilityCooldown);
		buf.writeBoolean(wallJumping);
		buf.writeInt(petSummonCooldown);
	}
	
	public void fromBytes(ByteBuf buf) {
		fallAbsorption = buf.readFloat();
		timeInAir = buf.readInt();
		abilityCooldown = buf.readInt();
		wallJumping = buf.readBoolean();
		petSummonCooldown = buf.readInt();
	}
	
	public void readFromNbt(NBTTagCompound nbt) {
		fallAbsorption = nbt.getFloat("FallAbsorption");
		timeInAir = nbt.getInteger("TimeInAir");
		abilityCooldown = nbt.getInteger("AbilityCooldown");
		wallJumping = nbt.getBoolean("WallJumping");
		petSummonCooldown = nbt.getInteger("PetSummonCooldown");
	}
	
	public void writeToNbt(NBTTagCompound nbt) {
		nbt.setFloat("FallAbsorption", fallAbsorption);
		nbt.setInteger("TimeInAir", timeInAir);
		nbt.setInteger("AbilityCooldown", abilityCooldown);
		nbt.setBoolean("WallJumping", wallJumping);
		nbt.setInteger("PetSummonCooldown", petSummonCooldown);
	}
	
	public float getFallAbsorption() {
		return fallAbsorption;
	}
	
	public void setFallAbsorption(float fallAbsorption) {
		if (fallAbsorption == 0 || fallAbsorption > this.fallAbsorption) this.fallAbsorption = fallAbsorption;
	}
	
	public int getTimeInAir() {
		return timeInAir;
	}
	
	public void setTimeInAir(int time) {
		this.timeInAir = time;
	}
	
	public int getAbilityCooldown() {
		return abilityCooldown;
	}
	
	public void setAbilityCooldown(int cooldown) {
		if (cooldown < 0) cooldown = 0;
		this.abilityCooldown = cooldown;
		save.run();
	}
	
	public void decrementCooldown() {
		if (abilityCooldown > 0) {
			abilityCooldown--;
			save.run();
		}
	}
	
	public boolean isWallJumping() {
		return wallJumping;
	}
	
	public void setWallJumping(boolean wallJumping) {
		this.wallJumping = wallJumping;
	}
	
	public int getPetSummonCooldown() {
		return petSummonCooldown;
	}
	
	public void setPetSummonCooldown(int petSummonCooldown) {
		this.petSummonCooldown = petSummonCooldown;
	}
	
}
