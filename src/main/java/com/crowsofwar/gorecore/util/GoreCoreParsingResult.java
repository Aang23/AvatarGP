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
package com.crowsofwar.gorecore.util;

/**
 * A list of results for generic type parsing given using
 * {@link GoreCoreParsingUtil}.
 * 
 * @author CrowsOfWar
 * @see GoreCoreParsingUtil
 */
public final class GoreCoreParsingResult {
	
	public static GoreCoreParsingResult.ResultInteger generateIntegerResult(int value, boolean successful) {
		return new ResultInteger(value, successful);
	}
	
	public static GoreCoreParsingResult.ResultFloat generateFloatResult(float value, boolean successful) {
		return new ResultFloat(value, successful);
	}
	
	public static GoreCoreParsingResult.ResultDouble generateDoubleResult(double value, boolean successful) {
		return new ResultDouble(value, successful);
	}
	
	public static GoreCoreParsingResult.ResultLong generateLongResult(long value, boolean successful) {
		return new ResultLong(value, successful);
	}
	
	public static GoreCoreParsingResult.ResultBoolean generateBooleanResult(boolean value,
			boolean successful) {
		return new ResultBoolean(value, successful);
	}
	
	private static class ResultBase<T> {
		private final T value;
		private final boolean successful;
		
		protected ResultBase(T value, boolean successful) {
			this.value = value;
			this.successful = successful;
		}
		
		/**
		 * Get the result of parsing the string. This will be the default value
		 * for the generic type if {@link #wasSuccessful() an error occured}.
		 */
		public T getResult() {
			return value;
		}
		
		/**
		 * Returns whether parsing the string was successful - that is, if the
		 * string was correct for this type.
		 */
		public boolean wasSuccessful() {
			return successful;
		}
		
	}
	
	public static class ResultInteger extends ResultBase<Integer> {
		public ResultInteger(Integer value, boolean successful) {
			super(value, successful);
		}
	}
	
	public static class ResultFloat extends ResultBase<Float> {
		public ResultFloat(Float value, boolean successful) {
			super(value, successful);
		}
	}
	
	public static class ResultDouble extends ResultBase<Double> {
		public ResultDouble(Double value, boolean successful) {
			super(value, successful);
		}
	}
	
	public static class ResultLong extends ResultBase<Long> {
		public ResultLong(Long value, boolean successful) {
			super(value, successful);
		}
	}
	
	public static class ResultBoolean extends ResultBase<Boolean> {
		public ResultBoolean(Boolean value, boolean successful) {
			super(value, successful);
		}
	}
	
}
