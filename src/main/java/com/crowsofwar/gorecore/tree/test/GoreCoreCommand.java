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
package com.crowsofwar.gorecore.tree.test;

import com.crowsofwar.gorecore.format.FormattedMessage;
import com.crowsofwar.gorecore.tree.*;
import com.crowsofwar.gorecore.util.AccountUUIDs;

import static com.crowsofwar.gorecore.tree.test.GoreCoreChatMessages.*;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class GoreCoreCommand extends TreeCommand {
	
	@Override
	public String getName() {
		return "gorecore";
	}
	
	@Override
	protected ICommandNode[] addCommands() {
		
		NodeFunctional reloadId = new NodeBuilder("fixid")//
				.addArgument(new ArgumentPlayerName("player"))//
				.addArgumentDirect("confirm", ITypeConverter.CONVERTER_STRING, "")//
				.build(popper -> {
					
					String username = popper.get();
					String confirm = popper.get();
					
					if (!confirm.equals("destroy-data")) {
						MSG_FIXID_CONFIRM.send(popper.from(), username);
						MSG_FIXID_CONFIRM2.send(popper.from(), username);
						return;
					}
					
					FormattedMessage msg;
					if (AccountUUIDs.getId(username).isTemporary()) {
						msg = AccountUUIDs.tryFixId(username) ? MSG_FIXID_SUCCESS : MSG_FIXID_FAILURE;
					} else {
						msg = MSG_FIXID_ONLINE;
					}
					msg.send(popper.from(), username);
					
				});
		
		return new ICommandNode[] { reloadId };
	}
	
}
