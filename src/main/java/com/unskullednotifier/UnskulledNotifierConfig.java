package com.unskullednotifier;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("unskullednotifier")
public interface UnskulledNotifierConfig extends Config
{
	@ConfigItem(
		keyName = "scale",
		name = "Scale",
		description = "Size multiplier that is applied to the overlay icon"
	)
	default int scale()
	{
		return 1;
	}

	@ConfigItem(
		keyName = "revenantCavesOnly",
		name = "Revenant caves only",
		description = "Only show the overlay while you are inside the Revenant Caves"
	)
	default boolean revenantCavesOnly()
	{
		return false;
	}
}
