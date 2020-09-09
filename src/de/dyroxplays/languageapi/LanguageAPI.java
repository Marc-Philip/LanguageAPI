package de.dyroxplays.languageapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.dyroxplays.languageapi.command.CMD_Language;
import de.dyroxplays.languageapi.listener.InvClick;
import de.dyroxplays.languageapi.listener.Join;
import de.dyroxplays.languageapi.manager.Language;
import de.dyroxplays.languageapi.manager.Manager;
import de.dyroxplays.languageapi.manager.MySQL;
import de.marc.languageapi.PlaceholderAPI;

public class LanguageAPI extends JavaPlugin {
	public static String prefix = "§8┃ §bSystem §8× ";

	private MySQL mysql;
	private Manager manager;

	@Override
	public void onEnable() {
		registerManagers();
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(prefix + "§aactivated");
		Bukkit.getConsoleSender().sendMessage(prefix + "§7was coded by Dyroxplays and Marc");
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(" ");
		
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getConsoleSender().sendMessage(prefix + "Found PlaceholderAPI, Hooked: " + new PlaceholderAPI().register());
        }
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(prefix + "§cdeactivated");
		Bukkit.getConsoleSender().sendMessage(prefix + "§7was coded by Dyroxplays and Marc");
		Bukkit.getConsoleSender().sendMessage(" ");
		Bukkit.getConsoleSender().sendMessage(" ");
	}

	void registerManagers() {
		loadConfig();
		prefix = getConfig().getString("prefix").replace("&", "§") + " ";
		mysql = new MySQL();
		manager = new Manager();

		new CMD_Language();
		new Join();
		new InvClick();

	}

	void loadConfig() {
		getConfig().options().copyDefaults(true);

		getConfig().addDefault("MYSQL.host", "host");
		getConfig().addDefault("MYSQL.port", "3306");
		getConfig().addDefault("MYSQL.username", "user");
		getConfig().addDefault("MYSQL.datebase", "db");
		getConfig().addDefault("MYSQL.password", "password123");
		getConfig().addDefault("MYSQL.tablename.messages", "LanguageAPI_messages");
		getConfig().addDefault("MYSQL.tablename.users", "LanguageAPI_user");

		getConfig().addDefault("prefix", "&8┃ &bLanguageAPI &8× ");
		getConfig().addDefault("globalprefix", "&8┃ &bSystem &8× ");
		getConfig().addDefault("error", "&cERROR: %name% not found");
		getConfig().addDefault("allowcmd", true);

		getConfig().options().header("##// Plugin Coded by Dyroxplays. \\##");
		saveConfig();
	}

	public static void sendMessage(Player player, String name, boolean showPrefix) {
		Language language = getPlugin().getManager().getPlayerLanguage(player);
		HashMap<String, String> messages = getPlugin().getManager().getMessages(language);
		if (!messages.containsKey(name)) {
			player.sendMessage(
					prefix + getPlugin().getConfig().getString("error").replace("&", "§").replace("%name%", name));
			Bukkit.getConsoleSender().sendMessage(
					prefix + getPlugin().getConfig().getString("error").replace("&", "§").replace("%name%", name));
			return;
		}
		player.sendMessage((showPrefix ? getPlugin().getConfig().getString("globalprefix").replace("&", "§") : "")
				+ messages.get(name));
	}

	public static String getMessage(String name, Language language, boolean showPrefix) {
		HashMap<String, String> messages = getPlugin().getManager().getMessages(language);
		if (!messages.containsKey(name)) {
			return prefix + getPlugin().getConfig().getString("error").replace("&", "§").replace("%name%", name);
		}
		return (showPrefix ? getPlugin().getConfig().getString("globalprefix").replace("&", "§") : "")
				+ messages.get(name);
	}
	
	public static String getMessage2(String name, Player p, boolean showPrefix) {
		HashMap<String, String> messages = getPlugin().getManager().getMessages(getPlayerLanguage(p));
		if (!messages.containsKey(name)) {
			return prefix + getPlugin().getConfig().getString("error").replace("&", "§").replace("%name%", name);
		}
		return (showPrefix ? getPlugin().getConfig().getString("globalprefix").replace("&", "§") : "")
				+ messages.get(name);
	}

	public static Language getPlayerLanguage(Player player) {
		return getPlugin().getManager().getPlayerLanguage(player);
	}

	public static Inventory getInventory(Inventory inv, Language language) {
		Inventory result;
		if (inv.getType() == InventoryType.CHEST)
			result = Bukkit.createInventory(inv.getHolder(), inv.getSize(), CMD_Language.getInvTitle());
		else
			result = Bukkit.createInventory(inv.getHolder(), inv.getType(), CMD_Language.getInvTitle());

		for (int i = 0; i < result.getSize(); i++) {
			if (inv.getItem(i) != null) {
				ItemStack item = inv.getItem(i).clone();
				HashMap<String, String> messages = getPlugin().getManager().getMessages(language);
				if (item.getType().name().contains("SKULL")) {
					SkullMeta meta = (SkullMeta) item.getItemMeta();
					if (meta.hasLore()) {
						List<String> lore = new ArrayList<>();
						for (int l = 0; l < meta.getLore().size(); l++) {
							if (messages.containsKey(meta.getLore().get(l))) {
								lore.add(messages.get(meta.getLore().get(l)));
							} else {
								lore.add(meta.getLore().get(l));
							}
						}
						meta.setLore(lore);
					}
					item.setItemMeta(meta);
				} else {
					ItemMeta meta = item.getItemMeta();
					if (meta.hasLore()) {
						List<String> lore = new ArrayList<>();
						for (int l = 0; l < meta.getLore().size(); l++) {
							if (messages.containsKey(meta.getLore().get(l))) {
								lore.add(messages.get(meta.getLore().get(l)));
							} else {
								lore.add(meta.getLore().get(l));
							}
						}
						meta.setLore(lore);
					}
					item.setItemMeta(meta);
				}
				result.setItem(i, item);
			}
		}
		return result;
	}

	public static LanguageAPI getPlugin() {
		return LanguageAPI.getPlugin(LanguageAPI.class);
	}

	public MySQL getMySQL() {
		return mysql;
	}

	public Manager getManager() {
		return manager;
	}

}
