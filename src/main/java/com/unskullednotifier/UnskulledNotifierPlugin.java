package com.unskullednotifier;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.SkullIcon;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Unskulled Notifier",
	description = "Displays a reminder when you are unskulled so you can re-skull before fighting.",
	tags = { "pvp", "wilderness", "overlay" }
)
public class UnskulledNotifierPlugin extends Plugin
{
	private static final Set<Integer> REVENANT_CAVE_REGION_IDS = ImmutableSet.of(
		12445, 12446, 12447, 12448,
		12701, 12702, 12703, 12704,
		12957, 12958, 12959, 12960,
		13213, 13214, 13215, 13216
	);

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private UnskulledNotifierOverlay overlay;

	@Inject
	private UnskulledNotifierConfig config;

	private boolean revenantCavesOnly;

	@Override
	protected void startUp()
	{
		updateConfig();
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
	}

	boolean shouldShowOverlay()
	{
		if (!isPlayerUnskulled())
		{
			return false;
		}

		return !revenantCavesOnly || isInRevenantCaves();
	}

	private boolean isPlayerUnskulled()
	{
		Player player = client.getLocalPlayer();
		return player != null && player.getSkullIcon() == SkullIcon.NONE;
	}

	private boolean isInRevenantCaves()
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return false;
		}

		WorldPoint location = player.getWorldLocation();
		return location != null && REVENANT_CAVE_REGION_IDS.contains(location.getRegionID());
	}

	@Provides
	UnskulledNotifierConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(UnskulledNotifierConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("unskullednotifier"))
		{
			return;
		}

		updateConfig();

		if (event.getKey().equals("scale"))
		{
			overlay.updateConfig();
		}
	}

	private void updateConfig()
	{
		revenantCavesOnly = config.revenantCavesOnly();
	}
}
