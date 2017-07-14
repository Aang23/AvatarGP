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
package com.crowsofwar.avatar;

import org.apache.logging.log4j.Logger;

public class AvatarLog {
	
	static Logger log;
	
	public static void debug(String s) {
		if (AvatarInfo.IS_DEVELOPMENT) log.debug("[Debug] " + s);
	}
	
	public static void info(String s) {
		log.info("[Info] " + s);
	}
	
	public static void error(String s) {
		log.error("[Error] " + s);
	}
	
	public static void error(String s, Throwable t) {
		log.error("[Error] " + s, t);
	}
	
	/**
	 * @deprecated Use {@link #warn(WarningType, String)}.
	 */
	@Deprecated
	public static void warn(String s) {
		warn(WarningType.UNKNOWN, s);
	}
	
	/**
	 * Output a warning with the given category.
	 * 
	 * @param type
	 *            Type of warning
	 * @param s
	 *            String to print
	 */
	public static void warn(WarningType type, String s) {
		log.warn("[Warn/" + type + "] " + s);
		if (type == WarningType.INVALID_CODE) {
			Thread.dumpStack();
		}
	}
	
	public static void warn(WarningType type, String s, Throwable t) {
		log.warn("[Warn/" + type + "]" + s, t);
		if (type == WarningType.INVALID_CODE) {
			Thread.dumpStack();
		}
	}
	
	/**
	 * Output a warning to the log that the player might have been hacking
	 */
	// TODO Notify the admins
	public static void warnHacking(String username, String s) {
		warn(WarningType.BAD_CLIENT_PACKET, "Player " + username + ": Unexpected data, " + s);
	}
	
	public enum WarningType {
		/**
		 * No warning type was specified
		 */
		UNKNOWN,
		/**
		 * Invalid data has been saved
		 */
		INVALID_SAVE,
		/**
		 * Invalid values for a method have been specified
		 */
		INVALID_CODE,
		/**
		 * A client sent abnormal input which might try to exploit glitches
		 */
		BAD_CLIENT_PACKET,
		/**
		 * Server sent abnormal input which is not 'correct' on client
		 */
		WEIRD_PACKET,
		/**
		 * Miswritten configuration files
		 */
		CONFIGURATION;
	}
	
}
