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

import com.crowsofwar.avatar.common.AvatarDamageSource;
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.config.ConfigSkills;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.EntityFireArc;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.world.World;

import java.util.List;

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public abstract class FireArcBehavior extends Behavior<EntityFireArc> {
	
	public static final DataSerializer<FireArcBehavior> DATA_SERIALIZER = new Behavior.BehaviorSerializer<>();
	
	public static void register() {
		DataSerializers.registerSerializer(DATA_SERIALIZER);
		
		registerBehavior(PlayerControlled.class);
		registerBehavior(Thrown.class);
		registerBehavior(Idle.class);
		
	}
	
	public static class PlayerControlled extends FireArcBehavior {
		
		public PlayerControlled() {}
		
		@Override
		public FireArcBehavior onUpdate(EntityFireArc entity) {
			
			EntityLivingBase owner = entity.getOwner();
			if (owner == null) {
				return this;
			}
			World world = owner.world;
			
			Vector look = Vector.toRectangular(Math.toRadians(owner.rotationYaw),
					Math.toRadians(owner.rotationPitch));
			Vector lookPos = Vector.getEyePos(owner).plus(look.times(3));
			Vector motion = lookPos.minus(new Vector(entity));
			motion.mul(.3);
			entity.move(MoverType.SELF, motion.x(), motion.y(), motion.z());
			
			return this;
			
		}
		
		@Override
		public void fromBytes(PacketBuffer buf) {}
		
		@Override
		public void toBytes(PacketBuffer buf) {}
		
		@Override
		public void load(NBTTagCompound nbt) {}
		
		@Override
		public void save(NBTTagCompound nbt) {}
		
	}
	
	public static class Thrown extends FireArcBehavior {
		
		@Override
		public FireArcBehavior onUpdate(EntityFireArc entity) {
			entity.velocity().add(0, -9.81 / 60, 0);
			
			List<EntityLivingBase> collidedList = entity.getEntityWorld().getEntitiesWithinAABB(
					EntityLivingBase.class, entity.getEntityBoundingBox().expand(0.9, 0.9, 0.9),
					collided -> collided != entity.getOwner());
			
			for (EntityLivingBase collided : collidedList) {
				
				double push = STATS_CONFIG.fireballSettings.push;
				collided.addVelocity(entity.motionX * push, 0.4 * push, entity.motionZ * push);
				collided.attackEntityFrom(AvatarDamageSource.causeFireDamage(collided, entity.getOwner()),
						STATS_CONFIG.fireballSettings.damage * entity.getDamageMult());
				collided.setFire(3);
				
				if (!entity.world.isRemote) {
					BendingData data = Bender.create(entity.getOwner()).getData();
					if (data != null) {
						data.getAbilityData(BendingAbility.ABILITY_FIRE_ARC)
								.addXp(ConfigSkills.SKILLS_CONFIG.fireHit);
					}
				}
				
			}
			
			if (!collidedList.isEmpty() && entity.getOwner() != null) {
				BendingData data = Bender.getData(entity.getOwner());
				AbilityData abilityData = data.getAbilityData(BendingAbility.ABILITY_FIRE_ARC);
				if (abilityData.isMasterPath(AbilityTreePath.SECOND)) {
					data.addStatusControl(StatusControl.THROW_FIRE);
					return new FireArcBehavior.PlayerControlled();
				}
			}
			
			return this;
		}
		
		@Override
		public void fromBytes(PacketBuffer buf) {}
		
		@Override
		public void toBytes(PacketBuffer buf) {}
		
		@Override
		public void load(NBTTagCompound nbt) {}
		
		@Override
		public void save(NBTTagCompound nbt) {}
		
	}
	
	public static class Idle extends FireArcBehavior {
		
		@Override
		public FireArcBehavior onUpdate(EntityFireArc entity) {
			return this;
		}
		
		@Override
		public void fromBytes(PacketBuffer buf) {}
		
		@Override
		public void toBytes(PacketBuffer buf) {}
		
		@Override
		public void load(NBTTagCompound nbt) {}
		
		@Override
		public void save(NBTTagCompound nbt) {}
		
	}
	
}
