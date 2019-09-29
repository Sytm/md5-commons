package de.md5lukas.commons.inventory;

import de.md5lukas.commons.StringHelper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ItemBuilder {

	private ItemStack stack;
	private ItemMeta meta;

	public ItemBuilder(Material material, int amount) {
		stack = new ItemStack(material, amount);
		meta = stack.getItemMeta();
	}

	public ItemBuilder(Material material) {
		this(material, 1);
	}

	public ItemBuilder(ItemStack stack) {
		stack = new ItemStack(stack);
		meta = stack.getItemMeta();
	}

	public ItemBuilder name(String displayName) {
		meta.setDisplayName(displayName);
		applyMeta();
		return this;
	}

	public ItemBuilder lore(String[] lore) {
		return lore(Arrays.asList(lore));
	}

	public ItemBuilder lore(List<String> lore) {
		meta.setLore(lore);
		applyMeta();
		return this;
	}

	public ItemBuilder lore(String lore) {
		return lore(StringHelper.split(lore, '\n'));
	}

	public ItemBuilder lore(String lore, int maxLineLength) {
		List<String> lines = StringHelper.wrap(lore.replace('\n', Character.MIN_VALUE), maxLineLength);
		for (int i = 0; i < lines.size(); i++) {
			String last = StringHelper.getLastChatColor(lines.get(i));
			int nextIndex = i + 1;
			if (!last.isEmpty() && nextIndex < lines.size()) {
				lines.set(nextIndex, last + lines.get(nextIndex));
			}
		}
		return lore(lines);
	}

	public ItemBuilder appendLore(String line) {
		List<String> lore = meta.getLore();
		if (lore == null) lore = new ArrayList<>();
		lore.add(line);
		meta.setLore(lore);
		applyMeta();
		return this;
	}

	public ItemBuilder removeLore(String line) {
		List<String> lore = meta.getLore();
		if (lore != null) {
			lore.remove(line);
			meta.setLore(lore);
			applyMeta();
		}
		return this;
	}

	public ItemBuilder amount(int amount) {
		stack.setAmount(amount);
		return this;
	}

	public ItemBuilder color(Color color) {
		switch (stack.getType()) {
			case LEATHER_HELMET:
			case LEATHER_CHESTPLATE:
			case LEATHER_LEGGINGS:
			case LEATHER_BOOTS:
				LeatherArmorMeta lam = (LeatherArmorMeta) meta;
				lam.setColor(color);
				meta = lam;
				applyMeta();
				break;
			default:
				break;
		}
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment, int level) {
		stack.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment) {
		return enchantment(enchantment, 1);
	}

	private void applyMeta() {
		stack.setItemMeta(meta);
	}

	public ItemStack make() {
		return stack;
	}
}