package de.marc.languageapi;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.dyroxplays.languageapi.LanguageAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderAPI extends PlaceholderExpansion {
    public boolean persist() {
        return true;
    }
    
    public boolean canRegister() {
        return true;
    }
    
    public String onPlaceholderRequest(final Player player, final String s) {
    	try {
    		HashMap<String, String> messages = LanguageAPI.getPlugin().getManager().getMessages(LanguageAPI.getPlayerLanguage(player));
    		if (!messages.containsKey(s)) {
    			return "■";
    		}
    		return ("" + messages.get(s));
    	} catch (Exception ex) {
    		return "■";
    	}
    }
    
    public String getAuthor() {
        return LanguageAPI.getPlugin().getDescription().getAuthors().toString();
    }
    
    public String getIdentifier() {
        return "lapi";
    }
    
    public String getVersion() {
        return LanguageAPI.getPlugin().getDescription().getVersion();
    }
}
