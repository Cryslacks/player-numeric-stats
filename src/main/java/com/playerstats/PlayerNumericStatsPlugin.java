package net.runelite.client.plugins.playerstats;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Player numeric stats"
)
public class PlayerNumericStatsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PlayerNumericStatsOverlay playerNumericStatsOverlay;

	@Inject
	private PlayerNumericStatsConfig config;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(playerNumericStatsOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(playerNumericStatsOverlay);
	}


	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("playernumericstats"))
		{
			playerNumericStatsOverlay.updateConfig();
		}
	}

	@Provides
	PlayerNumericStatsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PlayerNumericStatsConfig.class);
	}
}
