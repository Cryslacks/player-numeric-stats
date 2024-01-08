/*
 * Copyright (c) 2022, LlemonDuck <napkinorton@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.playerstats;

import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;

@Singleton
class PlayerNumericStatsOverlay extends Overlay
{

	/**
	 * This overlay is largely based on the plugin Party (PartyStatusOverlay.java), but with some small edits.
	 */
	private static Color COLOR_HEALTH_MAX = Color.green;
	private static Color COLOR_HEALTH_MIN = Color.red;
	private static Color COLOR_PRAYER = new Color(50, 200, 200);
	private static Color COLOR_STAMINA = new Color(160, 124, 72);
	private static Color COLOR_STAMINA_BOOSTED = new Color(220, 184, 132);
	private static Color COLOR_SPEC = new Color(225, 225, 0);
	private static Font OVERLAY_FONT = FontManager.getRunescapeBoldFont().deriveFont(16f);

	private final Client client;
	private final SpriteManager spriteManager;
	private final PlayerNumericStatsConfig config;
	private final ItemManager itemManager;

	private boolean renderHealth = false;
	private boolean renderPrayer = false;
	private boolean renderStamina = false;
	private boolean renderSpec = false;
	private boolean renderVeng = false;

	private boolean displayStam = false;
	private boolean displayOverload = false;
	private boolean displayPoT = false;

	private boolean healthLeft = true;
	private boolean prayerLeft = true;
	private boolean staminaLeft = false;
	private boolean specLeft = false;

	private int offsetLeft = 15;
	private int offsetRight = 5;

	@Inject
	private PlayerNumericStatsOverlay(
		Client client, SpriteManager spriteManager, PlayerNumericStatsConfig config, ItemManager itemManager
	)
	{
		this.client = client;
		this.spriteManager = spriteManager;
		this.config = config;
		this.itemManager = itemManager;

		updateConfig();

		setLayer(OverlayLayer.UNDER_WIDGETS);
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Player player = client.getLocalPlayer();

		int renderIxRight = 0;
		int renderIxLeft = 0;
		graphics.setFont(OVERLAY_FONT);

		final int healthCurrent = client.getBoostedSkillLevel(Skill.HITPOINTS);
		final int prayerCurrent = client.getBoostedSkillLevel(Skill.PRAYER);
		final int healthMax = client.getRealSkillLevel(Skill.HITPOINTS);
		final int runEnergy = (int) (client.getEnergy() / 100.0);
		final int specEnergy = client.getVarpValue(VarPlayer.SPECIAL_ATTACK_PERCENT) / 10;

		final boolean vengActive = client.getVarbitValue(Varbits.VENGEANCE_ACTIVE) == 1;
		final boolean runEnergyBoosted = client.getVarbitValue(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) != 0;
		final boolean hasPoT = client.getVarbitValue(5417) != 0; // Varbit of Prayer Enhance (+)
		final boolean hasOverload = client.getVarbitValue(Varbits.COX_OVERLOAD_REFRESHES_REMAINING) != 0;


		if (renderHealth)
		{
			double healthRatio = Math.min(1.0, (double) healthCurrent / healthMax);
			Color healthColor = ColorUtil.colorLerp(COLOR_HEALTH_MIN, COLOR_HEALTH_MAX, healthRatio);
			renderPlayerOverlay(graphics, player, String.valueOf(healthCurrent), healthColor, healthLeft ? renderIxLeft++ : renderIxRight++, healthLeft);

			if (displayOverload && hasOverload) {
				BufferedImage overloadIcon = itemManager.getImage(20996);

				if (overloadIcon != null)
				{
					BufferedImage overloadIconResized = rescale(overloadIcon, 18, 16);

					renderPlayerOverlayNextToText(graphics, player, overloadIconResized, healthLeft ? renderIxLeft : renderIxRight, healthLeft);
				}
			}
		}
		if (renderPrayer)
		{
			renderPlayerOverlay(graphics, player, String.valueOf(prayerCurrent), COLOR_PRAYER, prayerLeft ? renderIxLeft++ : renderIxRight++, prayerLeft);

			if (displayPoT && hasPoT) {
				BufferedImage potIcon = itemManager.getImage(20972);

				if (potIcon != null)
				{
					BufferedImage potIconResized = rescale(potIcon, 18, 16);

					renderPlayerOverlayNextToText(graphics, player, potIconResized, prayerLeft ? renderIxLeft : renderIxRight, prayerLeft);
				}
			}
		}
		if (renderStamina)
		{
			renderPlayerOverlay(graphics, player, String.valueOf(runEnergy), runEnergyBoosted ? COLOR_STAMINA_BOOSTED : COLOR_STAMINA, staminaLeft ? renderIxLeft++ : renderIxRight++, staminaLeft);

			if (displayStam && runEnergyBoosted) {
				BufferedImage stamIcon = itemManager.getImage(12625);

				if (stamIcon != null)
				{
					BufferedImage stamIconResized = rescale(stamIcon, 18, 16);

					renderPlayerOverlayNextToText(graphics, player, stamIconResized, staminaLeft ? renderIxLeft : renderIxRight, staminaLeft);
				}
			}
		}
		if (renderSpec)
		{
			renderPlayerOverlay(graphics, player, String.valueOf(specEnergy), COLOR_SPEC, specLeft ? renderIxLeft++ : renderIxRight++, specLeft);
		}
		if (renderVeng && vengActive)
		{
			BufferedImage vengIcon = spriteManager.getSprite(SpriteID.SPELL_VENGEANCE_OTHER, 0);
			if (vengIcon != null)
			{
				renderPlayerOverlay(graphics, player, vengIcon);
			}
		}

		return null;
	}

	private void renderPlayerOverlay(Graphics2D graphics, Player player, String text, Color color, int renderIx, boolean left)
	{
		Point point = Perspective.localToCanvas(client, player.getLocalLocation(), client.getPlane(), player.getLogicalHeight());

		if (point != null)
		{
			FontMetrics fm = graphics.getFontMetrics();
			int size = fm.getHeight();
			int zOffset = size * renderIx;

			int x;
			if (left) {
				x = point.getX() - size - offsetLeft;
			} else {
				x = point.getX() + size + offsetRight;
			}

			OverlayUtil.renderTextLocation(
				graphics,
				new Point(x, point.getY() + zOffset),
				text,
				color
			);
		}
	}

	private void renderPlayerOverlay(Graphics2D graphics, Player player, BufferedImage image)
	{
		Point textLocation = player.getCanvasImageLocation(image, player.getLogicalHeight() / 2);
		if (textLocation != null)
		{
			OverlayUtil.renderImageLocation(graphics, textLocation, image);
		}
	}
	private void renderPlayerOverlayNextToText(Graphics2D graphics, Player player, BufferedImage image, int renderIx, boolean left)
	{
		Point point = Perspective.localToCanvas(client, player.getLocalLocation(), client.getPlane(), player.getLogicalHeight());
		if (point != null)
		{

			int size = OVERLAY_FONT.getSize();
			int zOffset = size * (renderIx - 2);
			int offsetFromText = -15;

			int x;
			if (left) {
				x = point.getX() - size - offsetLeft + offsetFromText;
			} else {
				x = point.getX() + size + offsetRight + offsetFromText;
			}

			OverlayUtil.renderImageLocation(graphics, new Point(x, point.getY() + zOffset), image);
		}
	}

	private BufferedImage rescale(BufferedImage img, int newWidth, int newHeight)
	{
		Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		BufferedImage imgResized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2D = imgResized.createGraphics();
		graphics2D.drawImage(img, 0, 0, newWidth, newHeight, null);
		graphics2D.dispose();

		return imgResized;
	}

	void updateConfig()
	{
		this.renderHealth = config.healthPosition() != PlayerStatsPositionType.None;
		this.renderPrayer = config.prayerPosition() != PlayerStatsPositionType.None;
		this.renderStamina = config.runEnergyPosition() != PlayerStatsPositionType.None;
		this.renderSpec = config.specPosition() != PlayerStatsPositionType.None;
		this.renderVeng = config.displayVeng();

		this.displayStam = config.displayStam();
		this.displayOverload = config.displayOverload();
		this.displayPoT = config.displayPoT();

		this.healthLeft = config.healthPosition() == PlayerStatsPositionType.Left;
		this.prayerLeft = config.prayerPosition() == PlayerStatsPositionType.Left;
		this.staminaLeft = config.runEnergyPosition() == PlayerStatsPositionType.Left;
		this.specLeft = config.specPosition() == PlayerStatsPositionType.Left;

		this.offsetRight = config.offsetRight();
		this.offsetLeft = config.offsetLeft();

		this.COLOR_HEALTH_MAX = config.maxHealthColor();
		this.COLOR_HEALTH_MIN = config.minHealthColor();
		this.COLOR_PRAYER = config.prayerColor();
		this.COLOR_STAMINA = config.runEnergyColor();
		this.COLOR_STAMINA_BOOSTED = config.staminaBoostedColor();
		this.COLOR_SPEC = config.specColor();
	}
}
