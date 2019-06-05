package com.crowsofwar.avatarplugin;


import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.api.GriefPreventionApi;
import me.ryanhamshire.griefprevention.api.claim.Claim;
import me.ryanhamshire.griefprevention.api.claim.ClaimManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.world.Location;

import java.util.UUID;

public class AvatarGriefApiGriefPrevention implements AvatarGriefingApi {

	@Override
	public boolean canModify(World forgeWorld, BlockPos pos, EntityPlayer player) {

		org.spongepowered.api.world.World spongeWorld = (org.spongepowered.api.world.World) forgeWorld;
		GriefPreventionApi gApi = GriefPrevention.getApi();

		ClaimManager claimManager = gApi.getClaimManager(spongeWorld);
		Location<org.spongepowered.api.world.World> location = new Location<org.spongepowered.api.world.World>(spongeWorld, pos.getX(), pos.getY(), pos.getZ());
		Claim claim = claimManager.getClaimAt(location);

		return claim.isTrusted(player.getUniqueID()); // sponge uses entity Id

	}
}
