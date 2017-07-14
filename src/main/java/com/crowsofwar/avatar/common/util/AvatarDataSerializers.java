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
package com.crowsofwar.avatar.common.util;

import com.crowsofwar.avatar.common.data.ctx.BenderInfo;
import com.crowsofwar.avatar.common.item.ItemBisonArmor.ArmorTier;
import com.crowsofwar.avatar.common.item.ItemBisonSaddle.SaddleTier;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

import java.io.IOException;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AvatarDataSerializers {
	
	public static final DataSerializer<Block> SERIALIZER_BLOCK = new AvatarSerializer<Block>() {
		
		@Override
		public void write(PacketBuffer buf, Block value) {
			// TODO Find out if DataSerializer<Block> actually works...
			buf.writeInt(Block.getIdFromBlock(value));
		}
		
		@Override
		public Block read(PacketBuffer buf) throws IOException {
			return Block.getBlockById(buf.readInt());
		}
		
		@Override
		public DataParameter<Block> createKey(int id) {
			return new DataParameter<>(id, this);
		}

		@Override
		public Block copyValue(Block block) {
			return block;
		}
	};
	public static final DataSerializer<Vector> SERIALIZER_VECTOR = new AvatarSerializer<Vector>() {
		
		@Override
		public void write(PacketBuffer buf, Vector value) {
			buf.writeDouble(value.x());
			buf.writeDouble(value.y());
			buf.writeDouble(value.z());
		}
		
		@Override
		public Vector read(PacketBuffer buf) throws IOException {
			return new Vector(buf.readDouble(), buf.readDouble(), buf.readDouble());
		}
		
		@Override
		public DataParameter<Vector> createKey(int id) {
			return new DataParameter<>(id, this);
		}

		@Override
		public Vector copyValue(Vector vec) {
			return vec.copy();
		}
	};
	public static final DataSerializer<BenderInfo> SERIALIZER_BENDER = new AvatarSerializer<BenderInfo>() {
		
		@Override
		public void write(PacketBuffer buf, BenderInfo info) {
			buf.writeBoolean(info.isPlayer());
			buf.writeBoolean(info.getId() != null);
			if (info.getId() != null) {
				buf.writeUniqueId(info.getId());
			}
		}
		
		@Override
		public BenderInfo read(PacketBuffer buf) throws IOException {
			boolean player = buf.readBoolean();
			if (buf.readBoolean()) {
				return new BenderInfo(player, buf.readUniqueId());
			} else {
				return new BenderInfo(player, null);
			}
		}
		
		@Override
		public DataParameter<BenderInfo> createKey(int id) {
			return new DataParameter<>(id, this);
		}

		@Override
		public BenderInfo copyValue(BenderInfo benderInfo) {
			return benderInfo;
		}

	};
	public static final DataSerializer<SaddleTier> SERIALIZER_SADDLE = new AvatarSerializer<SaddleTier>() {
		
		@Override
		public void write(PacketBuffer buf, SaddleTier value) {
			buf.writeInt(value == null ? -1 : value.id());
		}
		
		@Override
		public SaddleTier read(PacketBuffer buf) throws IOException {
			int id = buf.readInt();
			return id == -1 ? null : SaddleTier.fromId(id);
		}
		
		@Override
		public DataParameter<SaddleTier> createKey(int id) {
			return new DataParameter<>(id, this);
		}

		@Override
		public SaddleTier copyValue(SaddleTier tier) {
			return tier;
		}
	};
	public static final DataSerializer<ArmorTier> SERIALIZER_ARMOR = new AvatarSerializer<ArmorTier>() {
		
		@Override
		public void write(PacketBuffer buf, ArmorTier value) {
			buf.writeInt(value == null ? -1 : value.id());
		}
		
		@Override
		public ArmorTier read(PacketBuffer buf) throws IOException {
			int id = buf.readInt();
			return id == -1 ? null : ArmorTier.fromId(id);
		}
		
		@Override
		public DataParameter<ArmorTier> createKey(int id) {
			return new DataParameter<>(id, this);
		}

		@Override
		public ArmorTier copyValue(ArmorTier tier) {
			return tier;
		}
	};
	
	public static void register() {
		DataSerializers.registerSerializer(SERIALIZER_BLOCK);
		DataSerializers.registerSerializer(SERIALIZER_VECTOR);
		DataSerializers.registerSerializer(SERIALIZER_BENDER);
		DataSerializers.registerSerializer(SERIALIZER_SADDLE);
		DataSerializers.registerSerializer(SERIALIZER_ARMOR);
	}
	
	private static abstract class AvatarSerializer<T> implements DataSerializer<T> {
		
		protected AvatarSerializer() {}
		
	}
	
}
