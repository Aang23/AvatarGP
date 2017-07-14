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

import static com.crowsofwar.gorecore.util.GoreCoreNBTUtil.nestedCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbstractBendingData;
import com.crowsofwar.avatar.common.data.DataCategory;
import com.crowsofwar.avatar.common.data.TickHandler;
import com.crowsofwar.avatar.common.util.AvatarUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityBenderData extends AbstractBendingData {
	
	private final EntityLivingBase entity;
	
	public EntityBenderData(EntityLivingBase entity) {
		this.entity = entity;
	}
	
	@Override
	public void save(DataCategory category) {}
	
	public void writeToNbt(NBTTagCompound writeTo) {
		
		AvatarUtils.writeList(getAllBending(),
				(compound, controller) -> compound.setInteger("ControllerID", controller.getID()), writeTo,
				"BendingControllers");
		
		AvatarUtils.writeList(getAllStatusControls(),
				(nbtTag, control) -> nbtTag.setInteger("Id", control.id()), writeTo, "StatusControls");
		
		AvatarUtils.writeMap(getAbilityDataMap(), //
				(nbt, ability) -> {
					nbt.setInteger("Id", ability.getId());
					nbt.setString("_AbilityName", ability.getName());
				}, (nbt, data) -> {
					nbt.setInteger("AbilityId", data.getAbility().getId());
					data.writeToNbt(nbt);
				}, writeTo, "AbilityData");
		
		getMiscData().writeToNbt(nestedCompound(writeTo, "Misc"));
		
		chi().writeToNBT(writeTo);
		
		AvatarUtils.writeList(getAllTickHandlers(), //
				(nbt, handler) -> nbt.setInteger("Id", handler.id()), //
				writeTo, "TickHandlers");
		
	}
	
	public void readFromNbt(NBTTagCompound readFrom) {
		
		List<BendingController> bendings = new ArrayList<>();
		AvatarUtils.readList(bendings,
				compound -> BendingController.find(compound.getInteger("ControllerID")), readFrom,
				"BendingControllers");
		clearBending();
		for (BendingController bending : bendings) {
			addBending(bending);
		}
		
		List<StatusControl> scs = new ArrayList<>();
		AvatarUtils.readList(scs, nbtTag -> StatusControl.lookup(nbtTag.getInteger("Id")), readFrom,
				"StatusControls");
		clearStatusControls();
		for (StatusControl sc : scs) {
			addStatusControl(sc);
		}
		
		Map<BendingAbility, AbilityData> abilityData = new HashMap<>();
		AvatarUtils.readMap(abilityData, nbt -> BendingManager.getAbility(nbt.getInteger("Id")), nbt -> {
			BendingAbility ability = BendingManager.getAbility(nbt.getInteger("AbilityId"));
			AbilityData data = new AbilityData(this, ability);
			data.readFromNbt(nbt);
			return data;
		}, readFrom, "AbilityData");
		clearAbilityData();
		for (Map.Entry<BendingAbility, AbilityData> entry : abilityData.entrySet()) {
			setAbilityData(entry.getKey(), entry.getValue());
		}
		
		getMiscData().readFromNbt(nestedCompound(readFrom, "Misc"));
		
		chi().readFromNBT(readFrom);
		
		List<TickHandler> tickHandlers = new ArrayList<>();
		AvatarUtils.readList(tickHandlers, //
				nbt -> TickHandler.fromId(nbt.getInteger("Id")), //
				readFrom, "TickHandlers");
		clearTickHandlers();
		for (TickHandler handler : tickHandlers) {
			addTickHandler(handler);
		}
		
	}
	
}
