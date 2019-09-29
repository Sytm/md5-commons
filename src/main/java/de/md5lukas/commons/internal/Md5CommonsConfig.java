package de.md5lukas.commons.internal;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.TimeUnit;

public class Md5CommonsConfig {

	private static boolean loaded = false;
	private static String DEFAULT_LANGUAGE;
	private static long UUID_CACHE_MAX_SIZE, UUID_CACHE_EXPIRE_AFTER;
	private static TimeUnit UUID_CACHE_EXPIRE_AFTER_TU;

	public static void load(FileConfiguration config) {
		if (loaded)
			return;
		loaded = true;
		DEFAULT_LANGUAGE = config.getString("defaultLanguage").toLowerCase();
		if (config.isString("uuidcache.maxSize")) {
			UUID_CACHE_MAX_SIZE = (long) Math.ceil(Bukkit.getMaxPlayers() * 0.75d);
		} else {
			UUID_CACHE_MAX_SIZE = config.getLong("uuidcache.maxSize");
		}
		UUID_CACHE_EXPIRE_AFTER = config.getLong("uuidcache.expiresAfter");
		UUID_CACHE_EXPIRE_AFTER_TU = TimeUnit.valueOf(config.getString("uuidcache.expiresAfterTimeUnit"));
	}

	public static String getDefaultLanguage() {
		return DEFAULT_LANGUAGE;
	}

	public static long getUuidCacheMaxSize() {
		return UUID_CACHE_MAX_SIZE;
	}

	public static long getUuidCacheExpireAfter() {
		return UUID_CACHE_EXPIRE_AFTER;
	}

	public static TimeUnit getUuidCacheExpireAfterTu() {
		return UUID_CACHE_EXPIRE_AFTER_TU;
	}
}
