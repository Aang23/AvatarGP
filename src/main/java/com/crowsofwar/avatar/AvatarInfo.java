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

/**
 * Not using semantic versioning. This isn't an API, and I'm too lazy to be
 * backwards-compatible.
 * <p>
 * Versioning scheme: AV{DEV_STAGE}{UPDATE}.{PATCH}{DEV_BUILD}
 * <p>
 * DEV_STAGE is for alpha("a"), beta("b"), or full release("").
 * <p>
 * If development version, DEV_BUILD is "_dev"
 * <p>
 * E.g. AV_B3.2-dev -> Beta, Update 3, patch 2, development build
 * 
 * @author CrowsOfWar
 */
public class AvatarInfo {
	
	// Things that are adjustable
	
	/**
	 * Incremented for every major update.
	 */
	public static final int VERSION_UPDATE = 4;
	/**
	 * Incremented for minor bug fixes.
	 */
	public static final int VERSION_PATCH = 2;
	/**
	 * "a" for alpha.
	 * <p>
	 * "b" for beta.
	 * <p>
	 * "" for full release.
	 */
	public static final String DEV_STAGE = "a";
	/**
	 * Type of version; 0 for production; 1 for development; 2 for preview 1; 3
	 * for preview 2, etc
	 * <p>
	 * Accessed via {@link #IS_PRODUCTION}, {@link #IS_PREVIEW},
	 * {@link #IS_DEVELOPMENT}
	 */
	private static final int VERSION_TYPE = 0;
	
	// Not adjustable / automatically calculated
	
	public static final String MOD_ID = "avatarmod";
	public static final String MOD_NAME = "Avatar Mod: Out of the Iceberg";
	
	public static final boolean IS_PRODUCTION = VERSION_TYPE == 0;
	public static final boolean IS_DEVELOPMENT = VERSION_TYPE == 1;
	public static final boolean IS_PREVIEW = VERSION_TYPE >= 2;
	
	public static final String MC_VERSION = "1.11.2";
	public static final String VERSION = DEV_STAGE + VERSION_UPDATE + "." + VERSION_PATCH
			+ (IS_PRODUCTION ? "" : (IS_PREVIEW ? "_preview" + (VERSION_TYPE - 1) : "_dev"));
	
	public enum VersionType {
		
		PRODUCTION,
		PREVIEW,
		DEVELOPMENT
	
	}
	
}
