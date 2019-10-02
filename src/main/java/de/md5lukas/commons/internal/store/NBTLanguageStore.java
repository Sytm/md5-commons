package de.md5lukas.commons.internal.store;

import de.md5lukas.commons.data.PlayerStore;
import de.md5lukas.commons.language.Languages;
import de.md5lukas.nbt.tags.CompoundTag;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class NBTLanguageStore implements LanguageStore {

	private Plugin plugin;

	@Override
	public void init(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getLanguage(UUID uuid) {
		CompoundTag tag = PlayerStore.getPlayerStore(uuid).getTag(plugin);
		if (tag.contains("language")) {
			return tag.getString("language");
		}
		return Languages.getDefaultLanguage();
	}

	@Override
	public void setLanguage(UUID uuid, String language) {
		PlayerStore.getPlayerStore(uuid).getTag(plugin).putString("language", language);
	}
}
