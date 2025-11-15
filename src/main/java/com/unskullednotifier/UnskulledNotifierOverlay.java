package com.unskullednotifier;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
	private final ScaledImage cachedIcon = new ScaledImage();

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
		this.cachedIcon.scale = -1;
		this.cachedIcon.scaledImage = baseIcon;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.shouldShowOverlay())
		{
			return null;
		}

		BufferedImage scaledIcon = getScaledIcon();
		return new ImageComponent(scaledIcon).render(graphics);
	}

	private BufferedImage getScaledIcon()
	{
		int scale = Math.max(1, config.scale());
		if (cachedIcon.scale == scale && cachedIcon.scaledImage != null)
		{
			return cachedIcon.scaledImage;
		}

		BufferedImage scaled = new BufferedImage(
			baseIcon.getWidth() * scale,
			baseIcon.getHeight() * scale,
			BufferedImage.TYPE_INT_ARGB);

		AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		scaleOp.filter(baseIcon, scaled);

		cachedIcon.scale = scale;
		cachedIcon.scaledImage = scaled;
		return scaled;
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

	private static final class ScaledImage
	{
		private int scale;
		private BufferedImage scaledImage;
	}
}
