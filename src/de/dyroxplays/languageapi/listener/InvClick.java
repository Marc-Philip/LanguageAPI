package de.dyroxplays.languageapi.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.dyroxplays.languageapi.LanguageAPI;
import de.dyroxplays.languageapi.command.CMD_Language;
import de.dyroxplays.languageapi.manager.Language;

public class InvClick implements Listener {

	public InvClick() {
		Bukkit.getPluginManager().registerEvents(this, LanguageAPI.getPlugin());
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		try {
			Player p = (Player) e.getWhoClicked();
			if (e.getView().getTitle().equals(CMD_Language.getInvTitle())) {
				e.setCancelled(true);
				if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equals(CMD_Language.german.getItemMeta().getDisplayName())) {
					if (LanguageAPI.getPlugin().getManager().getPlayerLanguage(p) != Language.german) {
						LanguageAPI.getPlugin().getManager().setLanguage(p, Language.german);
						LanguageAPI.sendMessage(p, "language-changed", true);
						p.closeInventory();
					}
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equals(CMD_Language.english.getItemMeta().getDisplayName())) {
					if (LanguageAPI.getPlugin().getManager().getPlayerLanguage(p) != Language.english) {
						LanguageAPI.getPlugin().getManager().setLanguage(p, Language.english);
						LanguageAPI.sendMessage(p, "language-changed", true);
						p.closeInventory();
					}
				}
			}
		} catch (Exception e2) {
		}
	}

}
