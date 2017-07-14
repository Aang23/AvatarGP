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
package com.crowsofwar.gorecore.config;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class ConfigurationException extends RuntimeException {
	
	private ConfigurationException(String message) {
		super(message);
	}
	
	private ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * The end-user made a mistake by creating an invalid configuration file.
	 * 
	 * @author CrowsOfWar
	 */
	public static class UserMistake extends ConfigurationException {
		
		public UserMistake(String message) {
			super(message);
		}
		
	}
	
	/**
	 * An exception occurred while trying to access the configuration file on
	 * disk.
	 * 
	 * @author CrowsOfWar
	 */
	public static class LoadingException extends ConfigurationException {
		
		public LoadingException(String message, Throwable cause) {
			super(message, cause);
		}
		
		public LoadingException(String message) {
			super(message);
		}
		
	}
	
	/**
	 * An exception occurred while using reflection to set values of the object.
	 * 
	 * @author CrowsOfWar
	 */
	public static class ReflectionException extends ConfigurationException {
		
		public ReflectionException(String message, Throwable cause) {
			super(message, cause);
		}
		
	}
	
	/**
	 * An exception occurred for some other reason, which we haven't explained
	 * 
	 * @author CrowsOfWar
	 */
	public static class Unexpected extends ConfigurationException {
		
		public Unexpected(String message, Throwable cause) {
			super(message, cause);
		}
		
	}
	
}
