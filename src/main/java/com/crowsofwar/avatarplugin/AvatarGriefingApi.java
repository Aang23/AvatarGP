package com.crowsofwar.avatarplugin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * An API to check if an area can be modified.
 *
 * @author CrowsOfWar
 */
public interface AvatarGriefingApi {

	boolean canModify(World world, BlockPos pos, UUID playerId);

}
