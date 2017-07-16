package com.crowsofwar.avatarplugin;

import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.api.GriefPreventionApi;
import me.ryanhamshire.griefprevention.api.claim.Claim;
import me.ryanhamshire.griefprevention.api.claim.ClaimManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

@Plugin(id = "avatarplugin", name = "AvatarMod2 GriefPrevention Integration Plugin", version = "1.0")
public class AvatarPlugin {

	private static GriefPreventionApi gpApi;

	@Listener
	public void onPostInit(GamePostInitializationEvent e) {
		gpApi = GriefPrevention.getApi();
	}

	@Listener
	public void onDropItem(DropItemEvent e) {
		Optional<Entity> playerOptional = e.getCause().first(Entity.class);
		if (playerOptional.isPresent()) {
			Entity player = playerOptional.get();
			ClaimManager claimManager = gpApi.getClaimManager(player.getWorld());
			Claim claim = claimManager.getClaimAt(player.getLocation(), false);
			Claim wilderness = claimManager.getWildernessClaim();

			System.out.println("World: " + player.getWorld().getClass().getName());
			if (claim == wilderness) {
				System.out.println("Dropped an item in the wilderness");
			} else {
				System.out.println("Droppepd an item in someone's claim: " + claim.getOwnerName());
			}

		}

		net.minecraft.world.World forgeWorld = null;
//		AvatarGriefingApi api = null;
//		api.canModify(forgeWorld, BlockPos.ORIGIN);

	}

	public static GriefPreventionApi getGpApi() {
		return gpApi;
	}

}
