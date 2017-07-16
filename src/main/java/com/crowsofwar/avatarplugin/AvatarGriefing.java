package com.crowsofwar.avatarplugin;

import com.crowsofwar.avatar.AvatarLog;

/**
 * Allows access to an AvatarGriefingApi
 *
 * @author CrowsOfWar
 */
public class AvatarGriefing {

	private static AvatarGriefingApi api;

	public static AvatarGriefingApi getApi() {
		if (api == null) {
			api = initApi();
		}
		return api;
	}

	private static AvatarGriefingApi initApi() {
		// see if grief prevention integration is loaded
		try {

			Class<?> gpIntegration =
					Class.forName("com.crowsofwar.avatarplugin.AvatarGriefApiGriefPrevention");

			// grief prevention integration is loaded
			try {
				return (AvatarGriefingApi) gpIntegration.newInstance();
			} catch (ReflectiveOperationException ex) {
				AvatarLog.error("Unable to load GriefPrevention integration, even though it was installed", ex);
				return new AvatarGriefApiUnprotected();
			}

		} catch (ClassNotFoundException ex) {

			// grief prevention integration not loaded
			return new AvatarGriefApiUnprotected();

		}
	}

}
