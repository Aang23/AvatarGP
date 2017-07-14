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
package com.crowsofwar.avatar.common.bending.air;

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;
import static com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath.FIRST;
import static com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath.SECOND;

import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.common.bending.BendingAi;
import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.data.ctx.Bender;
import com.crowsofwar.avatar.common.entity.EntityAirBubble;
import com.crowsofwar.avatar.common.network.packets.PacketCErrorMessage;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityAirBubble extends AirAbility {
	
	public AbilityAirBubble() {
		super("air_bubble");
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		EntityLivingBase bender = ctx.getBenderEntity();
		World world = ctx.getWorld();
		BendingData data = ctx.getData();
		
		ItemStack chest = bender.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		boolean elytraOk = (STATS_CONFIG.allowAirBubbleElytra || chest.getItem() != Items.ELYTRA);
		
		if (!elytraOk && bender instanceof EntityPlayerMP) {
			AvatarMod.network.sendTo(new PacketCErrorMessage("avatar.airBubbleElytra"),
					(EntityPlayerMP) bender);
		}
		
		if (!data.hasStatusControl(StatusControl.BUBBLE_CONTRACT) && elytraOk) {
			
			if (!ctx.consumeChi(STATS_CONFIG.chiAirBubble)) return;
			
			float xp = data.getAbilityData(this).getTotalXp();
			
			float size = 1.5f;
			float health = 10 + ctx.getLevel() * 6;
			if (ctx.getLevel() > 0) size = 2.5f;
			if (ctx.isMasterLevel(FIRST)) size = 4f;
			if (ctx.isMasterLevel(SECOND)) health = 10f;
			
			EntityAirBubble bubble = new EntityAirBubble(world);
			bubble.setOwner(bender);
			bubble.setPosition(bender.posX, bender.posY, bender.posZ);
			bubble.setHealth(health);
			bubble.setMaxHealth(health);
			bubble.setSize(size);
			bubble.setAllowHovering(ctx.isMasterLevel(SECOND));
			world.spawnEntity(bubble);
			
			data.addStatusControl(StatusControl.BUBBLE_EXPAND);
			data.addStatusControl(StatusControl.BUBBLE_CONTRACT);
		}
		
	}
	
	@Override
	public BendingAi getAi(EntityLiving entity, Bender bender) {
		return new AiAirBubble(this, entity, bender);
	}
	
}
