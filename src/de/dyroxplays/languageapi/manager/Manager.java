package de.dyroxplays.languageapi.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.dyroxplays.languageapi.LanguageAPI;
import de.dyroxplays.languageapi.listener.PlayerChangeLanguageEvent;

public class Manager {

	private static HashMap<Language, HashMap<String, String>> messages = new HashMap<>();
	private static HashMap<UUID, Language> playerLanguage = new HashMap<>();

	@SuppressWarnings("deprecation")
	public Manager() {

		Bukkit.getScheduler().runTaskAsynchronously(LanguageAPI.getPlugin(), new Runnable() {

			@Override
			public void run() {
				if (!existMessageName("language-changed")) {
					LanguageAPI.getPlugin().getMySQL().update("INSERT INTO "
							+ LanguageAPI.getPlugin().getConfig().getString("MYSQL.tablename.messages")
							+ " (name, english, german) VALUES ('language-changed', '§aYour language changed to §eenglish', '§aDeine Sprache wurde auf §edeutsch §aumgeändert');");
				}
				reloadMessages(null);
			}
		});

		Bukkit.getScheduler().scheduleAsyncRepeatingTask(LanguageAPI.getPlugin(), new Runnable() {

			@Override
			public void run() {
				reloadMessages(null);
			}
		}, 20 * 60 * 60 * 12, 20 * 60 * 60 * 12);

	}

	public void reloadMessages(Player player) {
		messages.clear();
		if (player == null)
			Bukkit.getConsoleSender().sendMessage(LanguageAPI.prefix + "§astarted to load messages...");
		else
			player.sendMessage(LanguageAPI.prefix + "§astarted to load messages...");
		Bukkit.getScheduler().runTaskAsynchronously(LanguageAPI.getPlugin(), new Runnable() {

			@Override
			public void run() {
				try {
					ResultSet rs = LanguageAPI.getPlugin().getMySQL().getResult("SELECT * FROM "
							+ LanguageAPI.getPlugin().getConfig().getString("MYSQL.tablename.messages") + ";");

					while (rs.next()) {
						String name = rs.getString("name");
						for (Language language : Language.values()) {
							String message = rs.getString(language.name());
							add(language, name, message);
						}
					}
					if (player == null)
						Bukkit.getConsoleSender().sendMessage(LanguageAPI.prefix + "§afinished with loading messages.");
					else
						player.sendMessage(LanguageAPI.prefix + "§afinished with loading messages.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void add(Language language, String name, String message) {
		HashMap<String, String> hash = new HashMap<>();
		if (messages.containsKey(language)) {
			hash = messages.get(language);
			if (hash.containsKey(name))
				hash.remove(name);
			hash.put(name, message);
			messages.remove(language);
		} else {
			hash.put(name, message);
		}
		messages.put(language, hash);
	}

	public HashMap<String, String> getMessages(Language language) {
		return messages.get(language);
	}

	private boolean existMessageName(String name) {
		try {
			ResultSet rs = LanguageAPI.getPlugin().getMySQL().getResult(
					"SELECT * FROM " + LanguageAPI.getPlugin().getConfig().getString("MYSQL.tablename.messages")
							+ " WHERE name= '" + name + "'");

			if (rs.next()) {
				return rs.getString("name") != null;
			}
			rs.close();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean existUUID(String uuid) {
		try {
			ResultSet rs = LanguageAPI.getPlugin().getMySQL()
					.getResult("SELECT * FROM " + LanguageAPI.getPlugin().getConfig().getString("MYSQL.tablename.users")
							+ " WHERE uuid= '" + uuid + "'");

			if (rs.next()) {
				return rs.getString("uuid") != null;
			}
			rs.close();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void registerPlayer(Player player) {
		String language = (player.spigot().getLocale().startsWith("de_") ? Language.german.name() : Language.english.name());
		if (playerLanguage.containsKey(player.getUniqueId()))
			playerLanguage.remove(player.getUniqueId());
		if (!existUUID(player.getUniqueId().toString())) {
			LanguageAPI.getPlugin().getMySQL()
					.update("INSERT INTO " + LanguageAPI.getPlugin().getConfig().getString("MYSQL.tablename.users")
							+ " (uuid, language) VALUES ('" + player.getUniqueId().toString() + "', '" + language
							+ "');");
			playerLanguage.put(player.getUniqueId(), Language.valueOf(language));
			LanguageAPI.sendMessage(player, "language-changed", true);
		} else {
			try {
				ResultSet rs = LanguageAPI.getPlugin().getMySQL()
						.getResult("SELECT `language` FROM "
								+ LanguageAPI.getPlugin().getConfig().getString("MYSQL.tablename.users")
								+ " WHERE uuid= '" + player.getUniqueId().toString() + "'");

				if ((!rs.next()) || (String.valueOf(rs.getString("language")) == null))
					;

				language = rs.getString("language");
				playerLanguage.put(player.getUniqueId(), Language.valueOf(language));
			} catch (SQLException e) {

			}
		}
	}

	public void updatePlayerMYSQL(Player player) {
		if (playerLanguage.containsKey(player.getUniqueId()))
			LanguageAPI.getPlugin().getMySQL()
					.update("UPDATE " + LanguageAPI.getPlugin().getConfig().getString("MYSQL.tablename.users")
							+ " SET language='" + playerLanguage.get(player.getUniqueId()).name() + "' WHERE uuid='"
							+ player.getUniqueId().toString() + "';");
		else
			registerPlayer(player);
	}

	public Language getPlayerLanguage(Player p) {
		if (playerLanguage.containsKey(p.getUniqueId()))
			return playerLanguage.get(p.getUniqueId());
		else
			return (p.spigot().getLocale().startsWith("de_") ? Language.german : Language.english);
	}

	public void setLanguage(Player p, Language language) {
		if (playerLanguage.containsKey(p.getUniqueId()))
			playerLanguage.remove(p.getUniqueId());
		playerLanguage.put(p.getUniqueId(), language);
		Bukkit.getPluginManager().callEvent(new PlayerChangeLanguageEvent(p));
	}

}
