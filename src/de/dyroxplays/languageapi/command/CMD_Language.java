package de.dyroxplays.languageapi.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.dyroxplays.languageapi.LanguageAPI;
import de.dyroxplays.languageapi.manager.HeadBuilder;
import de.dyroxplays.languageapi.manager.ItemBuilder;
import de.dyroxplays.languageapi.manager.Language;

public class CMD_Language implements CommandExecutor {

	public static String inv_title = "§8┃ §bSelect a language";
	public static Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, inv_title);
	public static ItemStack german = HeadBuilder.createSkull(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ==",
			"§bGerman", null, 1);

	public static ItemStack english = HeadBuilder.createSkull(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhYzk3NzRkYTEyMTcyNDg1MzJjZTE0N2Y3ODMxZjY3YTEyZmRjY2ExY2YwY2I0YjM4NDhkZTZiYzk0YjQifX19",
			"§bEnglish", null, 1);
	private static ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 9).setName(" ").create();

	public CMD_Language() { 

		for (int slot = 0; slot < inv.getSize(); slot++) {
			inv.setItem(slot, glass);
		}

		// 0 1 2 3 4

		inv.setItem(3, german);
		inv.setItem(1, english);

		LanguageAPI.getPlugin().getCommand("language").setExecutor(this);
	}
	
	public static String getInvTitle() {
		return inv_title;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("languageapi.reload")) {
			Bukkit.getScheduler().runTaskAsynchronously(LanguageAPI.getPlugin(), new Runnable() {

				@Override
				public void run() {
					LanguageAPI.getPlugin().getManager()
							.reloadMessages(sender instanceof Player ? ((Player) sender) : null);
				}
			});
			return true;
		}
		if (sender instanceof Player) {
			if (LanguageAPI.getPlugin().getConfig().getBoolean("allowcmd")) {
				Inventory pinv = Bukkit.createInventory(null, InventoryType.HOPPER, inv_title);
				for (int slot = 0; slot < pinv.getSize(); slot++) {
					pinv.setItem(slot, glass);
				}
				pinv.setItem(3, german);
				pinv.setItem(1, english);
				if (LanguageAPI.getPlayerLanguage((Player) sender) == Language.german) {
					ItemStack item = pinv.getItem(3).clone();
					net.minecraft.server.v1_8_R3.ItemStack nmsStack = org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asNMSCopy(item);
					net.minecraft.server.v1_8_R3.NBTTagCompound tag = null;

					if (!nmsStack.hasTag()) {
						tag = new net.minecraft.server.v1_8_R3.NBTTagCompound();
						nmsStack.setTag(tag);

					}

					if (tag == null)
						tag = nmsStack.getTag();

					net.minecraft.server.v1_8_R3.NBTTagList ench = new net.minecraft.server.v1_8_R3.NBTTagList();
					tag.set("ench", ench);
					nmsStack.setTag(tag);

					item = org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asBukkitCopy(nmsStack);
					pinv.setItem(3, item);
				} else if (LanguageAPI.getPlayerLanguage((Player) sender) == Language.english) {
					ItemStack item = pinv.getItem(1).clone();
					net.minecraft.server.v1_8_R3.ItemStack nmsStack = org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asNMSCopy(item);
					net.minecraft.server.v1_8_R3.NBTTagCompound tag = null;

					if (!nmsStack.hasTag()) {
						tag = new net.minecraft.server.v1_8_R3.NBTTagCompound();
						nmsStack.setTag(tag);

					}

					if (tag == null)
						tag = nmsStack.getTag();

					net.minecraft.server.v1_8_R3.NBTTagList ench = new net.minecraft.server.v1_8_R3.NBTTagList();
					tag.set("ench", ench);
					nmsStack.setTag(tag);

					item = org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asBukkitCopy(nmsStack);
					pinv.setItem(1, item);
				}
				((Player) sender).openInventory(pinv);
				((Player) sender).playSound(((Player) sender).getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f);
			}
		}
		return true;
	}

}
