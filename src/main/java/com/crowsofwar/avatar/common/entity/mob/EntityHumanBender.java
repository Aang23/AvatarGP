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
package com.crowsofwar.avatar.common.entity.mob;

import static com.crowsofwar.avatar.common.AvatarChatMessages.MSG_HUMANBENDER_NO_SCROLLS;

import javax.annotation.Nullable;

import com.crowsofwar.avatar.common.entity.ai.EntityAiGiveScroll;
import com.crowsofwar.avatar.common.item.ItemScroll.ScrollType;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public abstract class EntityHumanBender extends EntityBender {
	
	private static final DataParameter<Integer> SYNC_SKIN = EntityDataManager
			.createKey(EntityHumanBender.class, DataSerializers.VARINT);
	
	private EntityAiGiveScroll aiGiveScroll;
	private int scrollsLeft;
	
	/**
	 * @param world
	 */
	public EntityHumanBender(World world) {
		super(world);
		scrollsLeft = rand.nextInt(3) + 1;
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_SKIN, (int) (rand.nextDouble() * getNumSkins()));
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3);
	}
	
	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		
		// this.targetTasks.addTask(2,
		// new EntityAINearestAttackableTarget(this, EntityPlayer.class, true,
		// false));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, EntityHumanBender.class));
		
		addBendingTasks();
		
		this.tasks.addTask(4, aiGiveScroll = new EntityAiGiveScroll(this, getScrollType()));
		this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setSkin(nbt.getInteger("Skin"));
		scrollsLeft = nbt.getInteger("Scrolls");
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("Skin", getSkin());
		nbt.setInteger("Scrolls", scrollsLeft);
	}
	
	protected abstract void addBendingTasks();
	
	protected abstract ScrollType getScrollType();
	
	protected abstract int getNumSkins();
	
	public int getSkin() {
		return dataManager.get(SYNC_SKIN);
	}
	
	public void setSkin(int skin) {
		dataManager.set(SYNC_SKIN, skin);
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty,
			@Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		setEquipmentBasedOnDifficulty(difficulty);
		setHomePosAndDistance(getPosition(), 20);
		
		return livingdata;
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int i = 0;
		
		if (entityIn instanceof EntityLivingBase) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(),
					((EntityLivingBase) entityIn).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}
		
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
		
		if (flag) {
			if (i > 0 && entityIn instanceof EntityLivingBase) {
				((EntityLivingBase) entityIn).knockBack(this, i * 0.5F,
						MathHelper.sin(this.rotationYaw * 0.017453292F),
						(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}
			
			int j = EnchantmentHelper.getFireAspectModifier(this);
			
			if (j > 0) {
				entityIn.setFire(j * 4);
			}
			
			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityIn;
				ItemStack itemstack = this.getHeldItemMainhand();
				ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack()
						: ItemStack.EMPTY;
				
				if (!itemstack.isEmpty() && !itemstack1.isEmpty()
						&& itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
					float f1 = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
					
					if (this.rand.nextFloat() < f1) {
						entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
						this.world.setEntityState(entityplayer, (byte) 30);
					}
				}
			}
			
			this.applyEnchantments(this, entityIn);
		}
		
		return flag;
	}
	
	@Override
	protected abstract ResourceLocation getLootTable();
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem() == Items.DIAMOND && !world.isRemote) {
			
			if (scrollsLeft > 0) {
				if (aiGiveScroll.giveScrollTo(player)) {
					// Take diamond
					scrollsLeft--;
					if (!player.capabilities.isCreativeMode) {
						stack.shrink(1);
					}
				}
			} else {
				MSG_HUMANBENDER_NO_SCROLLS.send(player);
			}
			
			return true;
			
		}
		
		return false;
		
	}
	
}
