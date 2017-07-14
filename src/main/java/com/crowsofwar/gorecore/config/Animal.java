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
@HasCustomLoader(loaderClass = Animal.Loader.class)
public class Animal {
	
	@Load
	public String name;
	
	@Load
	public String species;
	
	@Load
	public int age;
	
	// can't be configured
	public boolean isAwesome;
	
	public Animal() {}
	
	public Animal(String name, String species, int age, boolean isAwesome) {
		this.name = name;
		this.species = species;
		this.age = age;
		this.isAwesome = isAwesome;
	}
	
	public static class Loader implements CustomObjectLoader<Animal> {
		
		@Override
		public void load(Object relevantConfigInfoWillGoHere, Animal obj) {
			obj.isAwesome = obj.species.equals("Crow");
		}
		
	}
	
	@Override
	public String toString() {
		return "Animal [name=" + name + ", species=" + species + ", age=" + age + ", isAwesome=" + isAwesome
				+ "]";
	}
	
}
