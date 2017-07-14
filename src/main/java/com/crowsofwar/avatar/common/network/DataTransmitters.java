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
package com.crowsofwar.avatar.common.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crowsofwar.avatar.AvatarLog;
import com.crowsofwar.avatar.AvatarLog.WarningType;
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingManager;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.Chi;
import com.crowsofwar.avatar.common.data.DataCategory;
import com.crowsofwar.avatar.common.data.MiscData;
import com.crowsofwar.avatar.common.data.TickHandler;

import io.netty.buffer.ByteBuf;

/**
 * DataTransmitters are responsible for reading and writing certain parts of
 * player data to the network. For example, there is a transmitter for the
 * bending list, the ability data, and chi.
 * 
 * @author CrowsOfWar
 */
public class DataTransmitters {
	
	public static final DataTransmitter<List<BendingController>> BENDING_LIST = new DataTransmitter<List<BendingController>>() {
		
		@Override
		public void write(ByteBuf buf, List<BendingController> t) {
			buf.writeInt(t.size());
			for (BendingController controller : t)
				buf.writeInt(controller.getType().id());
		}
		
		@Override
		public List<BendingController> read(ByteBuf buf, BendingData data) {
			int size = buf.readInt();
			List<BendingController> out = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				out.add(BendingManager.getBending(BendingType.find(buf.readInt())));
			}
			return out;
		}
	};
	
	public static final DataTransmitter<Map<BendingAbility, AbilityData>> ABILITY_DATA = new DataTransmitter<Map<BendingAbility, AbilityData>>() {
		
		@Override
		public void write(ByteBuf buf, Map<BendingAbility, AbilityData> t) {
			Set<Map.Entry<BendingAbility, AbilityData>> entries = t.entrySet();
			buf.writeInt(entries.size());
			for (Map.Entry<BendingAbility, AbilityData> entry : entries) {
				entry.getValue().toBytes(buf);
			}
		}
		
		@Override
		public Map<BendingAbility, AbilityData> read(ByteBuf buf, BendingData data) {
			Map<BendingAbility, AbilityData> out = new HashMap<>();
			int size = buf.readInt();
			for (int i = 0; i < size; i++) {
				AbilityData abilityData = AbilityData.createFromBytes(buf, data);
				if (abilityData == null) {
					AvatarLog.warn(WarningType.WEIRD_PACKET, "Invalid ability ID sent for ability data");
				} else {
					out.put(abilityData.getAbility(), abilityData);
				}
			}
			return out;
		}
	};
	
	public static final DataTransmitter<List<StatusControl>> STATUS_CONTROLS = new DataTransmitter<List<StatusControl>>() {
		
		@Override
		public void write(ByteBuf buf, List<StatusControl> t) {
			buf.writeInt(t.size());
			for (StatusControl sc : t) {
				buf.writeInt(sc.id());
			}
		}
		
		@Override
		public List<StatusControl> read(ByteBuf buf, BendingData data) {
			int size = buf.readInt();
			List<StatusControl> out = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				StatusControl sc = StatusControl.lookup(buf.readInt());
				if (sc == null)
					AvatarLog.warn(WarningType.WEIRD_PACKET, "Invalid status control id");
				else
					out.add(sc);
			}
			return out;
		}
	};
	
	public static final DataTransmitter<Boolean> BOOLEAN = new DataTransmitter<Boolean>() {
		
		@Override
		public void write(ByteBuf buf, Boolean t) {
			buf.writeBoolean(t);
		}
		
		@Override
		public Boolean read(ByteBuf buf, BendingData data) {
			return buf.readBoolean();
		}
	};
	
	public static final DataTransmitter<Chi> CHI = new DataTransmitter<Chi>() {
		
		@Override
		public void write(ByteBuf buf, Chi t) {
			t.toBytes(buf);
		}
		
		@Override
		public Chi read(ByteBuf buf, BendingData data) {
			Chi chi = new Chi(data);
			chi.fromBytes(buf);
			return chi;
		}
	};
	
	public static final DataTransmitter<List<TickHandler>> TICK_HANDLERS = new DataTransmitter<List<TickHandler>>() {
		
		@Override
		public void write(ByteBuf buf, List<TickHandler> list) {
			buf.writeInt(list.size());
			for (TickHandler handler : list) {
				buf.writeInt(handler.id());
			}
		}
		
		@Override
		public List<TickHandler> read(ByteBuf buf, BendingData data) {
			List<TickHandler> list = new ArrayList<>();
			int length = buf.readInt();
			for (int i = 0; i < length; i++) {
				list.add(TickHandler.fromBytes(buf));
			}
			return list;
		}
		
	};
	
	public static final DataTransmitter<MiscData> MISC_DATA = new DataTransmitter<MiscData>() {
		
		@Override
		public void write(ByteBuf buf, MiscData t) {
			t.toBytes(buf);
		}
		
		@Override
		public MiscData read(ByteBuf buf, BendingData data) {
			MiscData misc = new MiscData(() -> data.save(DataCategory.MISC_DATA));
			misc.fromBytes(buf);
			return misc;
		}
	};
	
	public static final DataTransmitter<BendingController> ACTIVE_BENDING = new DataTransmitter<BendingController>() {
		
		@Override
		public void write(ByteBuf buf, BendingController t) {
			buf.writeInt(t == null ? -1 : t.getType().id());
		}
		
		@Override
		public BendingController read(ByteBuf buf, BendingData data) {
			int id = buf.readInt();
			if (id == -1) {
				return null;
			}
			BendingType type = BendingType.find(id);
			return BendingManager.getBending(type);
		}
	};
	
}
