package com.crowsofwar.avatarplugin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AvatarGriefApiUnprotected implements AvatarGriefingApi {
	@Override
	public boolean canModify(World world, BlockPos pos, EntityPlayer player) {
		return !world.isSpawnChunk(pos.getX(), pos.getZ());
	}
}
