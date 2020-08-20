package de.dyroxplays.languageapi.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

//Ehm Marc, warum denn so kompliziert?
//Dieser Headbuilder ist unabhängig von der Mojang API, erstellt also Köpfe mit Hilfe von Texturen.
//Das ist schnell und vor allem deutlich zuverlässiger!

//value ist der Texturwert. Beispielseite: https://minecraft-heads.com/custom-heads/alphabet/28595-lucky-block-orange (unter "other")
//Wenn man keine Lore hat, kann man null mitgeben

public class HeadBuilder {
	public static ItemStack createSkull(String value, String name, String lore, int amount) {
		ItemStack head = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);

		if (value.isEmpty())
			return head;

		SkullMeta headMeta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);

		profile.getProperties().put("textures", new Property("textures", value));

		try {
			Field profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
		} catch (Exception ex) {
			System.out.println("An error occured while setting up a head from a custom texture.");
		}

		headMeta.setDisplayName(name);

		if (lore != null) {
			List<String> lore_as_list = new ArrayList<String>();
			lore_as_list.add(" ");
			lore_as_list.add(lore);
			headMeta.setLore(lore_as_list);
		}

		head.setAmount(amount);

		head.setItemMeta(headMeta);

		return head;
	}
}
