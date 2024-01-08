package com.playerstats;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PlayerNumericStatsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(net.runelite.client.plugins.playerstats.PlayerNumericStatsPlugin.class);
		RuneLite.main(args);
	}
}