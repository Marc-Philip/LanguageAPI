package de.dyroxplays.languageapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.dyroxplays.languageapi.LanguageAPI;

public class Join implements Listener {

	public Join() {
		Bukkit.getPluginManager().registerEvents(this, LanguageAPI.getPlugin());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		try {
			Player p = e.getPlayer();
			Bukkit.getScheduler().runTaskLaterAsynchronously(LanguageAPI.getPlugin(), new Runnable() {

				@Override
				public void run() {
					LanguageAPI.getPlugin().getManager().registerPlayer(p);
				}
			}, 30);
		} catch (Exception e2) {
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		try {
			Player p = e.getPlayer();
			Bukkit.getScheduler().runTaskLaterAsynchronously(LanguageAPI.getPlugin(), new Runnable() {

				@Override
				public void run() {
					LanguageAPI.getPlugin().getManager().updatePlayerMYSQL(p);
				}
			}, 30);
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}

}
