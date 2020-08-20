package de.dyroxplays.languageapi.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.dyroxplays.languageapi.LanguageAPI;
import de.dyroxplays.languageapi.manager.Language;

public class PlayerChangeLanguageEvent extends Event {

	public static HandlerList handlers = new HandlerList();

	Player player;
	Language language;

	public PlayerChangeLanguageEvent(Player player) {
		this.player = player;
		this.language = LanguageAPI.getPlayerLanguage(player);
	}

	public Player getPlayer() {
		return player;
	}

	public Language getLanguage() {
		return this.language;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
