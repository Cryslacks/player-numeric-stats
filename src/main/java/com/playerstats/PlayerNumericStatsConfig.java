package com.playerstats;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("playernumericstats")
public interface PlayerNumericStatsConfig extends Config
{
	@ConfigItem(
			keyName = "healthPosition",
			name = "Health position",
			description = "Where and if to display health.",
			position = 0
	)
	default PlayerStatsPositionType healthPosition()
	{
		return PlayerStatsPositionType.None;
	}

	@ConfigItem(
			keyName = "prayerPosition",
			name = "Prayer position",
			description = "Where and if to display prayer.",
			position = 1
	)
	default PlayerStatsPositionType prayerPosition()
	{
		return PlayerStatsPositionType.None;
	}

	@ConfigItem(
			keyName = "runEnergyPosition",
			name = "Run energy position",
			description = "Where and if to display run energy.",
			position = 2
	)
	default PlayerStatsPositionType runEnergyPosition()
	{
		return PlayerStatsPositionType.None;
	}

	@ConfigItem(
			keyName = "specPosition",
			name = "Spec position",
			description = "Where and if to display spec.",
			position = 3
	)
	default PlayerStatsPositionType specPosition()
	{
		return PlayerStatsPositionType.None;
	}

	@ConfigItem(
			keyName = "displayVeng",
			name = "Display vengeance",
			description = "Show vengeance on the player model.",
			position = 4
	)
	default boolean displayVeng()
	{
		return false;
	}

	@ConfigItem(
			keyName = "displayStam",
			name = "Display stamina pot",
			description = "Show stamina on the player model.",
			position = 5
	)
	default boolean displayStam()
	{
		return false;
	}

	@ConfigItem(
			keyName = "displayOverload",
			name = "Display overload",
			description = "Show overload effects on the player model.",
			position = 6
	)
	default boolean displayOverload()
	{
		return false;
	}

	@ConfigItem(
			keyName = "displayPoT",
			name = "Display PoT",
			description = "Show prayer over time effects on the player model.",
			position = 7
	)
	default boolean displayPoT()
	{
		return false;
	}

	@ConfigItem(
			keyName = "offsetRight",
			name = "Offset right",
			description = "Configures the offset from the player model to draw text right from.",
			position = 8
	)
	default int offsetRight()
	{
		return 5;
	}

	@ConfigItem(
			keyName = "offsetLeft",
			name = "Offset left",
			description = "Configures the offset from the player model to draw text left from.",
			position = 9
	)
	default int offsetLeft()
	{
		return 15;
	}

	@Alpha
	@ConfigItem(
			keyName = "maxHealthColor",
			name = "Max health color",
			description = "Configures the color for health text when your at max health",
			position = 10
	)
	default Color maxHealthColor()
	{
		return Color.green;
	}

	@Alpha
	@ConfigItem(
			keyName = "minHealthColor",
			name = "Min health color",
			description = "Configures the color for health text when your at min health",
			position = 11
	)
	default Color minHealthColor()
	{
		return Color.red;
	}

	@Alpha
	@ConfigItem(
			keyName = "prayerColor",
			name = "Prayer color",
			description = "Configures the color for prayer text",
			position = 12
	)
	default Color prayerColor()
	{
		return new Color(50, 200, 200);
	}

	@Alpha
	@ConfigItem(
			keyName = "runEnergyColor",
			name = "Run energy color",
			description = "Configures the color for run energy text",
			position = 13
	)
	default Color runEnergyColor()
	{
		return new Color(160, 124, 72);
	}

	@Alpha
	@ConfigItem(
			keyName = "staminaBoostedColor",
			name = "Stamina boosted color",
			description = "Configures the color for run energy text when boosted",
			position = 14
	)
	default Color staminaBoostedColor()
	{
		return new Color(220, 184, 132);
	}

	@Alpha
	@ConfigItem(
			keyName = "specColor",
			name = "Spec color",
			description = "Configures the color for special attack text",
			position = 15
	)
	default Color specColor()
	{
		return Color.yellow;
	}
}
