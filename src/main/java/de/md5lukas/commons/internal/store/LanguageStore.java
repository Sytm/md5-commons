package de.md5lukas.commons.internal.store;

import org.bukkit.plugin.Plugin;

import java.util.UUID;

public interface LanguageStore {

	void init(Plugin plugin);

	String getLanguage(UUID uuid);

	void setLanguage(UUID uuid, String language);
}
