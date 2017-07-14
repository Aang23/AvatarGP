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
package com.crowsofwar.avatar.client.render;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import com.crowsofwar.avatar.common.entity.EntityAirblade;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class RenderAirblade extends Render<EntityAirblade> {
	
	public static final ResourceLocation TEXTURE = new ResourceLocation("avatarmod",
			"textures/entity/airblade.png");
	private static final Random random = new Random();
	
	public RenderAirblade(RenderManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void doRender(EntityAirblade entity, double x, double y, double z, float entityYaw,
			float partialTicks) {
		
		float ticks = (entity.ticksExisted + partialTicks) / 3;
		
		Matrix4f mat = new Matrix4f();
		mat.translate((float) x, (float) y + .1f, (float) z);
		mat.rotate(ticks, 0, 1, 0);
		
		//@formatter:off
		float n = -.75f, p = .75f;
		Vector4f nw = new Vector4f(n, 0, n, 1).mul(mat);
		Vector4f ne = new Vector4f(p, 0, n, 1).mul(mat);
		Vector4f sw = new Vector4f(n, 0, p, 1).mul(mat);
		Vector4f se = new Vector4f(p, 0, p, 1).mul(mat);
		//@formatter:on
		
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		
		GlStateManager.enableBlend();
		GlStateManager.disableLighting();
		drawQuad(2, nw, ne, se, sw, 0, 0, 1, 1);
		GlStateManager.enableLighting();
		
		if (entity.ticksExisted % 3 == 0) {
			World world = entity.world;
			AxisAlignedBB boundingBox = entity.getEntityBoundingBox();
			double spawnX = boundingBox.minX + random.nextDouble() * (boundingBox.maxX - boundingBox.minX);
			double spawnY = boundingBox.minY + random.nextDouble() * (boundingBox.maxY - boundingBox.minY);
			double spawnZ = boundingBox.minZ + random.nextDouble() * (boundingBox.maxZ - boundingBox.minZ);
			world.spawnParticle(EnumParticleTypes.CLOUD, spawnX, spawnY, spawnZ, 0, 0, 0);
		}
		
	}
	
	private void drawQuad(int normal, Vector4f pos1, Vector4f pos2, Vector4f pos3, Vector4f pos4, double u1,
			double v1, double u2, double v2) {
		
		Tessellator t = Tessellator.getInstance();
		BufferBuilder vb = t.getBuffer();
		
		if (normal == 0 || normal == 2) {
			vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			vb.pos(pos1.x, pos1.y, pos1.z).tex(u2, v1).endVertex();
			vb.pos(pos2.x, pos2.y, pos2.z).tex(u2, v2).endVertex();
			vb.pos(pos3.x, pos3.y, pos3.z).tex(u1, v2).endVertex();
			vb.pos(pos4.x, pos4.y, pos4.z).tex(u1, v1).endVertex();
			t.draw();
		}
		if (normal == 1 || normal == 2) {
			vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			vb.pos(pos1.x, pos1.y, pos1.z).tex(u2, v1).endVertex();
			vb.pos(pos4.x, pos4.y, pos4.z).tex(u1, v1).endVertex();
			vb.pos(pos3.x, pos3.y, pos3.z).tex(u1, v2).endVertex();
			vb.pos(pos2.x, pos2.y, pos2.z).tex(u2, v2).endVertex();
			t.draw();
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityAirblade entity) {
		return null;
	}
	
}
