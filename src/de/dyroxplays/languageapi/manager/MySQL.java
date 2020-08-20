package de.dyroxplays.languageapi.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import de.dyroxplays.languageapi.LanguageAPI;

public class MySQL {
	String host;
	String port;
	String username;
	String database;
	String password;
	Connection con;

	public MySQL() {
		host = LanguageAPI.getPlugin().getConfig().getString("MYSQL.host");
		port = LanguageAPI.getPlugin().getConfig().getString("MYSQL.port");
		username = LanguageAPI.getPlugin().getConfig().getString("MYSQL.username");
		database = LanguageAPI.getPlugin().getConfig().getString("MYSQL.datebase");
		password = LanguageAPI.getPlugin().getConfig().getString("MYSQL.password");
		connect(true);
		createTable();
	}

	public void connect(boolean showMessage) {
		if (!isConnected()) {
			try {
				con = DriverManager.getConnection(
						"jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username,
						password);
				if (showMessage)
					Bukkit.getConsoleSender().sendMessage(LanguageAPI.prefix + "§9MySQL: §aVerbindung hergestellt!");
			} catch (SQLException e) {
				if (showMessage)
					Bukkit.getConsoleSender()
							.sendMessage(LanguageAPI.prefix + "§9MySQL: §ckonnte keine Verbindung hergestellt werden!");
			}
		}
	}

	public void close() {
		if (isConnected()) {
			try {
				con.close();
				con = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void createTable() {
		update("CREATE TABLE IF NOT EXISTS " + LanguageAPI.getPlugin().getConfig().getString("MYSQL.tablename.messages")
				+ " (name VARCHAR(100), english VARCHAR(100), german VARCHAR(100));");
		update("CREATE TABLE IF NOT EXISTS " + LanguageAPI.getPlugin().getConfig().getString("MYSQL.tablename.users")
				+ " (uuid VARCHAR(100), language VARCHAR(100));");
	}

	public boolean isConnected() {
		return con != null;
	}

	public void update(String qry) {
		connect(false);
		try {
			con.createStatement().executeUpdate(qry);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
	}

	public ResultSet getResult(String qry) {
		connect(false);
		try {
			return con.createStatement().executeQuery(qry);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		return null;
	}

}
