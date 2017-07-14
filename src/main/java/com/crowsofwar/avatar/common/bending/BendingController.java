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
package com.crowsofwar.avatar.common.bending;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.common.gui.BendingMenuInfo;
import com.crowsofwar.gorecore.util.GoreCoreNBTInterfaces.CreateFromNBT;
import com.crowsofwar.gorecore.util.GoreCoreNBTInterfaces.ReadableWritable;
import com.crowsofwar.gorecore.util.GoreCoreNBTInterfaces.WriteToNBT;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Base class for bending abilities. All bending classes extend this one. They
 * can save data to NBT if necessary. Functionality for bending should be in
 * subclasses. Bending controllers are singletons, but must be accessed through
 * {@link BendingManager}.
 * <p>
 * For the sake of abstraction, you won't need to refer to bending controllers
 * by their concrete names.
 * <p>
 * Subclasses have access to client input via optionally* implementable hook
 * methods.
 * <p>
 * *Optionally = the subclass must declare the method, but does not need to put
 * any code inside of it.
 *
 * @param <STATE>
 *            The BendingState this controller is using
 * 
 */
public abstract class BendingController implements ReadableWritable {
	
	public static final CreateFromNBT<BendingController> creator = new CreateFromNBT<BendingController>() {
		@Override
		public BendingController create(NBTTagCompound nbt, Object[] methodsExtraData, Object[] extraData) {
			int id = nbt.getInteger("ControllerID");
			try {
				BendingController bc = BendingManager.getBending(id);
				return bc;
			} catch (Exception e) {
				AvatarLog.error(
						"Could not find bending controller from ID '" + id + "' - please check NBT data");
				e.printStackTrace();
				return null;
			}
		}
	};
	
	public static final WriteToNBT<BendingController> writer = new WriteToNBT<BendingController>() {
		@Override
		public void write(NBTTagCompound nbt, BendingController object, Object[] methodsExtraData,
				Object[] extraData) {
			nbt.setInteger("ControllerID", object.getID());
		}
	};
	
	/**
	 * RNG available for convenient use.
	 */
	public static final Random random = new Random();
	
	private final List<BendingAbility> abilities;
	
	public BendingController() {
		this.abilities = new ArrayList<>();
	}
	
	protected void addAbility(BendingAbility ability) {
		this.abilities.add(ability);
	}
	
	/**
	 * @deprecated Use {@link #getType()} instead.
	 */
	@Deprecated
	public final int getID() {
		return getType().id();
	}
	
	/**
	 * Gets an identifier for this bending controller.
	 */
	public abstract BendingType getType();
	
	/**
	 * Get information about this bending controller's radial menu.
	 */
	public abstract BendingMenuInfo getRadialMenu();
	
	/**
	 * Get the name of this bending controller in lowercase. e.g. "earthbending"
	 */
	public abstract String getControllerName();
	
	public List<BendingAbility> getAllAbilities() {
		return this.abilities;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {}
	
	public static BendingController find(int id) {
		
		try {
			BendingController bc = BendingManager.getBending(id);
			return bc;
		} catch (Exception e) {
			AvatarLog.warn(AvatarLog.WarningType.INVALID_SAVE,
					"Could not find bending controller from ID '" + id + "' - please check NBT data");
			e.printStackTrace();
			return null;
		}
		
	}
	
}
