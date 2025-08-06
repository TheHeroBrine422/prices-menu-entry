package com.menuprices;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.LinkBrowser;

@Slf4j
@PluginDescriptor(
	name = "Menu Prices Entry"
)
public class MenuPricesPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemManager itemManager;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Prices Menu Entry started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Prices Menu Entry stopped!");
	}

	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{
		final MenuEntry[] entries = event.getMenuEntries();
		for (int idx = entries.length - 1; idx >= 0; --idx)
		{
			final MenuEntry entry = entries[idx];
			final Widget w = entry.getWidget();
			final int canonItemId = itemManager.canonicalize(entry.getItemId());

			if (w != null && WidgetUtil.componentToInterface(w.getId()) == InterfaceID.INVENTORY && "Examine".equals(entry.getOption()) && entry.getIdentifier() == 10) {
				if (itemManager.getItemComposition(canonItemId).isTradeable()) {
					client.getMenu().createMenuEntry(idx)
						.setOption("Open Wiki Prices")
						.setTarget(entry.getTarget())
						.setType(MenuAction.RUNELITE)
						.onClick(consumer -> {
							LinkBrowser.browse("https://prices.runescape.wiki/osrs/item/" + canonItemId);
						});
				}
			}
		}
	}
}
