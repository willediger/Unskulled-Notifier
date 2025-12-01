package com.unskullednotifier;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.util.ImageUtil;

class UnskulledNotifierOverlay extends Overlay
{
	private final UnskulledNotifierPlugin plugin;
	private final UnskulledNotifierConfig config;
	private final BufferedImage baseIcon;
	private BufferedImage icon;

	@Inject
	private UnskulledNotifierOverlay(UnskulledNotifierPlugin plugin, UnskulledNotifierConfig config)
	{
		super(plugin);
		this.plugin = plugin;
		this.config = config;

		setLayer(OverlayLayer.ALWAYS_ON_TOP);
		setPosition(OverlayPosition.BOTTOM_LEFT);
		setPriority(OverlayPriority.HIGH);

		this.baseIcon = loadIcon();
		updateConfig();
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.shouldShowOverlay())
		{
			return null;
		}

		return new ImageComponent(icon).render(graphics);
	}

	public void updateConfig()
	{
		int scale = Math.max(1, config.scale());
		icon = ImageUtil.resizeImage(baseIcon, baseIcon.getWidth() * scale, baseIcon.getHeight() * scale);
	}

	private static BufferedImage loadIcon()
	{
		BufferedImage icon = ImageUtil.loadImageResource(UnskulledNotifierPlugin.class, "icon.png");
		if (icon == null)
		{
			throw new IllegalStateException("Unable to load icon.png resource for Unskulled Notifier overlay");
		}

		return icon;
	}
}
