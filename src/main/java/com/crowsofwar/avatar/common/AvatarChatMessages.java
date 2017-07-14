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
package com.crowsofwar.avatar.common;

import static com.crowsofwar.gorecore.format.FormattedMessage.newChatMessage;

import com.crowsofwar.gorecore.format.FormattedMessage;
import com.crowsofwar.gorecore.format.MessageConfiguration;

import net.minecraft.util.text.TextFormatting;

public class AvatarChatMessages {
	
	// @formatter:off
	public static final MessageConfiguration CFG = new MessageConfiguration()
			.addColor("value", TextFormatting.AQUA)
			.addColor("error", TextFormatting.RED)
			.addColor("error_value", TextFormatting.DARK_RED)
			.addColor("title", TextFormatting.GOLD);
	public static final FormattedMessage MSG_BENDING_BRANCH_INFO = newChatMessage(CFG, "avatar.cmd.bending");
	public static final FormattedMessage MSG_PLAYER_DATA_NO_DATA = newChatMessage(CFG, "avatar.cmd.bending.list.noData", "player");
	public static final FormattedMessage MSG_BENDING_LIST_NONBENDER = newChatMessage(CFG, "avatar.cmd.bending.list.nonbender", "player");
	public static final FormattedMessage MSG_BENDING_LIST_ITEM = newChatMessage(CFG, "avatar.cmd.bending.list.item", "bending");
	public static final FormattedMessage MSG_BENDING_LIST_TOP = newChatMessage(CFG, "avatar.cmd.bending.list.top", "player", "amount");
	
	public static final FormattedMessage MSG_BENDING_ADD_ALREADY_HAS = newChatMessage(CFG, "avatar.cmd.bending.add.alreadyHas", "player",
			"bending");
	public static final FormattedMessage MSG_BENDING_ADD_SUCCESS = newChatMessage(CFG, "avatar.cmd.bending.add.success", "player", "bending");
	
	public static final FormattedMessage MSG_BENDING_REMOVE_DOESNT_HAVE = newChatMessage(CFG, "avatar.cmd.bending.remove.doesntHave", "player",
			"bending");
	public static final FormattedMessage MSG_BENDING_REMOVE_SUCCESS = newChatMessage(CFG, "avatar.cmd.bending.remove.success", "player",
			"bending");
	
	public static final FormattedMessage MSG_EARTHBENDING = newChatMessage(CFG, "avatar.earthbending");
	public static final FormattedMessage MSG_FIREBENDING = newChatMessage(CFG, "avatar.firebending");
	public static final FormattedMessage MSG_WATERBENDING = newChatMessage(CFG, "avatar.waterbending");
	
	public static final FormattedMessage MSG_CONFIG_EXCEPTION_1 = newChatMessage(CFG, "avatar.cmd.cfg.exception1");
	public static final FormattedMessage MSG_CONFIG_EXCEPTION_2 = newChatMessage(CFG, "avatar.cmd.cfg.exception2", "details");
	public static final FormattedMessage MSG_CONFIG_SUCCESS = newChatMessage(CFG, "avatar.cmd.cfg.successful");
	
	public static final FormattedMessage MSG_ABILITY_SET_RANGE = newChatMessage(CFG, "avatar.cmd.ability.set.range");
	public static final FormattedMessage MSG_ABILITY_SET_SUCCESS = newChatMessage(CFG, "avatar.cmd.ability.set.success", "player", "ability", "amount");
	
	public static final FormattedMessage MSG_ABILITY_GET = newChatMessage(CFG, "avatar.cmd.ability.get", "player", "ability", "amount");
	
	public static final FormattedMessage MSG_XPSET_SUCCESS = newChatMessage(CFG, "avatar.cmd.xpset", "player", "ability", "spec");
	
	public static final FormattedMessage MSG_PROGRESS_POINT_ADDED = newChatMessage(CFG, "avatar.cmd.pp.add", "player", "pps", "bending");
	public static final FormattedMessage MSG_PROGRESS_POINT_GET = newChatMessage(CFG, "avatar.cmd.pp.get", "player", "pps", "bending");
	public static final FormattedMessage MSG_PROGRESS_POINT_SET = newChatMessage(CFG, "avatar.cmd.pp.set", "player", "pps", "bending");
	public static final FormattedMessage MSG_PROGRESS_POINT_SET_RANGE = newChatMessage(CFG, "avatar.cmd.pp.set.range");
	
	public static final FormattedMessage MSG_DONT_HAVE_BENDING = newChatMessage(CFG, "avatar.donthavebending", "username");
	
	public static final FormattedMessage MSG_BISON_WHISTLE_SUMMON = newChatMessage(CFG, "avatar.bisonWhistle.summon", "time");
	public static final FormattedMessage MSG_BISON_WHISTLE_ASSIGN = newChatMessage(CFG, "avatar.bisonWhistle.assign", "bison");
	public static final FormattedMessage MSG_BISON_WHISTLE_NOSUMMON = newChatMessage(CFG, "avatar.bisonWhistle.notAssigned");
	public static final FormattedMessage MSG_BISON_WHISTLE_NOT_FOUND = newChatMessage(CFG, "avatar.bisonWhistle.notFound", "bison");
	public static final FormattedMessage MSG_BISON_WHISTLE_NOTOWNED = newChatMessage(CFG, "avatar.bisonWhistle.notOwned");
	public static final FormattedMessage MSG_BISON_WHISTLE_UNTAMED = newChatMessage(CFG, "avatar.bisonWhistle.untamed");
	
	public static final FormattedMessage MSG_SKY_BISON_STATS = newChatMessage(CFG, "avatar.bisonStats", "food", "health", "domestication");
	public static final FormattedMessage MSG_BISON_TRANSFER_OLD = newChatMessage(CFG, "avatar.bisonWhistle.transferAway", "bison", "newOwner");
	public static final FormattedMessage MSG_BISON_TRANSFER_NEW = newChatMessage(CFG, "avatar.bisonWhistle.transferTo", "bison", "oldOwner");
	public static final FormattedMessage MSG_BISON_TRANSFER_NONE = newChatMessage(CFG, "avatar.bisonWhistle.noTransfer");
	public static final FormattedMessage MSG_BISON_TRANSFER_OLD_START = newChatMessage(CFG, "avatar.bisonWhistle.transferAway.start", "bison", "newOwner");
	public static final FormattedMessage MSG_BISON_TRANSFER_NEW_START = newChatMessage(CFG, "avatar.bisonWhistle.transferTo.start", "bison", "oldOwner");
	public static final FormattedMessage MSG_BISON_TRANSFER_OLD_IGNORE = newChatMessage(CFG, "avatar.bisonWhistle.transferAway.ignore", "newOwner");
	public static final FormattedMessage MSG_BISON_TRANSFER_NEW_IGNORE = newChatMessage(CFG, "avatar.bisonWhistle.transferTo.ignore", "oldOwner");
	public static final FormattedMessage MSG_BISON_TRANSFER_OFFLINE = newChatMessage(CFG, "avatar.bisonWhistle.transferOffline", "owner");
	
	public static final FormattedMessage MSG_HUMANBENDER_NO_SCROLLS = newChatMessage(CFG, "avatar.outOfScrolls");
	
	/**
	 * Call the static initializers
	 */
	public static void loadAll() {}
	
}
