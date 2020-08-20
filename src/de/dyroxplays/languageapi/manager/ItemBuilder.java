package de.dyroxplays.languageapi.manager;

import java.util.Arrays;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {

	private ItemStack item;

	/*public ItemBuilder(Material material) {
		this.item = new ItemStack(material);
	}

	public ItemBuilder(Material id) {
		this.item = new ItemStack(id, 1);
	}*/

	public ItemBuilder(Material id, int data) {
		this.item = new ItemStack(id, 1, (short) data);
	}

	public ItemBuilder(Material material, int size, short data) {
		this.item = new ItemStack(material, size, data);
	}

	public ItemStack create() {
		return this.item;
	}

	public ItemBuilder setSkullOwner(String name) {
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(name);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setLore(String... lore) {

		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder setName(String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return this;
	}

	public ItemBuilder addGlow() {

		net.minecraft.server.v1_16_R2.ItemStack nmsStack = org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_16_R2.NBTTagCompound tag = null;

		if (!nmsStack.hasTag()) {
			tag = new net.minecraft.server.v1_16_R2.NBTTagCompound();
			nmsStack.setTag(tag);

		}

		if (tag == null)
			tag = nmsStack.getTag();

		net.minecraft.server.v1_16_R2.NBTTagList ench = new net.minecraft.server.v1_16_R2.NBTTagList();
		tag.set("ench", ench);
		nmsStack.setTag(tag);

		item = org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack.asBukkitCopy(nmsStack);

		return this;
	}

	public ItemBuilder setColor(int red, int green, int blue) {
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(red, green, blue));
		item.setItemMeta(meta);
		return this;
	}
}
