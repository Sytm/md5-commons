package de.md5lukas.commons.internal;

import de.md5lukas.commons.data.PlayerStore;
import de.md5lukas.commons.internal.store.NBTLanguageStore;
import de.md5lukas.commons.language.LanguageCommand;
import de.md5lukas.commons.language.Languages;
import de.md5lukas.nbt.Tags;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class CommonsMain extends JavaPlugin {

	private static Logger logger;
	private static Plugin instance;

	public static Plugin instance() {
		return instance;
	}
	public static Logger logger() {
		return logger;
	}


	@SuppressWarnings("ConstantConditions")
	@Override
	public void onEnable() {
		Tags.registerExtendedTags();
		instance = this;
		logger = this.getLogger();
		this.saveDefaultConfig();
		Md5CommonsConfig.load(this.getConfig());
		Languages.setStore(new NBTLanguageStore());
		getCommand("language").setExecutor(new LanguageCommand());

		new File(getDataFolder(), "playerdata/").mkdirs();
		// Keep the player stores in the cache when the player is online
		getServer().getScheduler().runTaskTimer(this,
			() -> getServer().getOnlinePlayers().stream().map(Player::getUniqueId).forEach(PlayerStore::getPlayerStore), 20, 20 * 60 * 15);
	}

	@Override
	public void onDisable() {
		PlayerStore.saveAll();
	}
}
