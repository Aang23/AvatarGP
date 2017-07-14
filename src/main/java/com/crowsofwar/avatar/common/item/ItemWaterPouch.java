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
package com.crowsofwar.avatar.common.item;

import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ItemWaterPouch extends Item implements AvatarItem {
	
	public ItemWaterPouch() {
		setCreativeTab(AvatarItems.tabItems);
		setUnlocalizedName("water_pouch");
		setMaxStackSize(1);
		setMaxDamage(0);
		setHasSubtypes(false);
	}
	
	@Override
	public Item item() {
		return this;
	}
	
	@Override
	public String getModelName(int meta) {
		return "water_pouch";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltips,
							   ITooltipFlag advanced) {
		
		int meta = stack.getMetadata();
		tooltips.add(I18n.format("avatar.tooltip.water_pouch" + (meta == 0 ? ".empty" : ""), meta));
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		
		for (int meta = 0; meta <= 5; meta++) {
			subItems.add(new ItemStack(this, 1, meta));
		}
		
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		
		ItemStack stack = player.getHeldItem(hand);
		
		Vector eye = Vector.getEntityPos(player);
		Vector look = Vector.getLookRectangular(player);
		
		Raytrace.Result raytrace = Raytrace.predicateRaytrace(world, eye, look, 4,
				(pos, state) -> state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER);
		
		if (raytrace.hitSomething()) {
			BlockPos pos = raytrace.getPos().toBlockPos();
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) {
				stack.setItemDamage(5);
			}
		}
		
		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}
	
}
