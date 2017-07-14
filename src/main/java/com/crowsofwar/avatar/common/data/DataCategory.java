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

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.crowsofwar.avatar.common.network.DataTransmitter;
import com.crowsofwar.avatar.common.network.DataTransmitters;

import io.netty.buffer.ByteBuf;

/**
 * Separates all of the methods of BendingData into different categories. This
 * is so networking packets can only send some categories and not others
 * depending on what's changed.
 * <p>
 * For example, if a player's chi is recharging, the packets only need to send
 * info about the Chi and would add the {@link #CHI} DataCategory to the changed
 * list. But they wouldn't send info about bending controllers since that is
 * unnecessary.
 * 
 * @author CrowsOfWar
 */

public enum DataCategory {
	
	// @formatter:off
	BENDING_LIST(	data -> data.getAllBending(),			(data, obj) -> data.setAllBending(obj),			DataTransmitters.BENDING_LIST),
	STATUS_CONTROLS(data -> data.getAllStatusControls(),	(data, obj) -> data.setAllStatusControls(obj),	DataTransmitters.STATUS_CONTROLS),
	ABILITY_DATA(	data -> data.getAbilityDataMap(),		(data, obj) -> data.setAbilityDataMap(obj),		DataTransmitters.ABILITY_DATA),
	CHI(			data -> data.chi(),						(data, obj) -> data.setChi(obj),				DataTransmitters.CHI),
	MISC_DATA(		data -> data.getMiscData(),				(data, obj) -> data.setMiscData(obj),			DataTransmitters.MISC_DATA),
	TICK_HANDLERS(	data -> data.getAllTickHandlers(),		(data, obj) -> data.setAllTickHandlers(obj),	DataTransmitters.TICK_HANDLERS),
	ACTIVE_BENDING(	data -> data.getActiveBending(),		(data, obj) -> data.setActiveBending(obj),		DataTransmitters.ACTIVE_BENDING);
	// @formatter:on
	
	private final Function<BendingData, ?> getter;
	private final BiConsumer<BendingData, ?> setter;
	private final DataTransmitter<?> transmitter;
	
	private <T> DataCategory(Function<BendingData, T> getter, BiConsumer<BendingData, T> setter,
			DataTransmitter<?> transmitter) {
		this.getter = getter;
		this.setter = setter;
		this.transmitter = transmitter;
	}
	
	/**
	 * Finds the necessary data from the PlayerData and then writes it to the
	 * ByteBuf
	 */
	public void write(ByteBuf buf, BendingData data) {
		((DataTransmitter<Object>) transmitter).write(buf, getter.apply(data));
	}
	
	/**
	 * Reads from the ByteBuf and saves the result into player data
	 */
	public void read(ByteBuf buf, BendingData data) {
		Object obj = transmitter.read(buf, data);
		((BiConsumer<BendingData, Object>) setter).accept(data, obj);
	}
	
}
