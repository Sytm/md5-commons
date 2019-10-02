package de.md5lukas.commons.language;

import com.google.common.base.Preconditions;
import de.md5lukas.commons.UUIDUtils;
import de.md5lukas.commons.internal.CommonsMain;
import de.md5lukas.commons.internal.Md5CommonsConfig;
import de.md5lukas.commons.internal.store.LanguageStore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public class Languages {

	private static Map<Plugin, PluginLanguageData> languages = new HashMap<>();
	private static boolean recalcSupport = false;
	private static Set<String> fullySupported = new HashSet<>();
	private static Set<String> partiallySupported = new HashSet<>();

	public static void registerLanguage(Plugin plugin, String language) {
		languages.compute(plugin, (k, v) -> {
			if (v == null)
				v = new PluginLanguageData();
			v.addLanguage(language);
			recalcSupport = true;
			return v;
		});
	}

	public static Set<String> getFullySupportedLanguages() {
		recalcSupport();
		return fullySupported;
	}

	public static Set<String> getPartiallySupportedLanguages() {
		recalcSupport();
		return partiallySupported;
	}

	private static void recalcSupport() {
		if (recalcSupport) {
			recalcSupport = false;
			fullySupported.clear();
			partiallySupported.clear();

			Iterator<PluginLanguageData> ite = languages.values().iterator();
			PluginLanguageData data = ite.next();
			fullySupported.addAll(data.supported);
			partiallySupported.addAll(data.supported);
			while (ite.hasNext()) {
				data = ite.next();
				List<String> supported = data.supported;
				fullySupported.removeIf(lang -> !supported.contains(lang));
				partiallySupported.addAll(data.supported);
			}
			partiallySupported.removeAll(fullySupported);
		}
	}

	public static String getDefaultLanguage() {
		return Md5CommonsConfig.getDefaultLanguage();
	}

	public static boolean isSupportedLanguage(String language) {
		return fullySupported.contains(language) || partiallySupported.contains(language);
	}

	private static LanguageStore store;

	public static void setStore(LanguageStore store) {
		if (Languages.store != null)
			throw new IllegalStateException("A language store has already been set");
		Languages.store = Preconditions.checkNotNull(store, "The language store cannot be null");
		store.init(CommonsMain.instance());
	}

	public static String getLanguage(UUID uuid) {
		if (uuid.equals(UUIDUtils.ZERO_UUID))
			return getDefaultLanguage();
		return store.getLanguage(uuid);
	}

	public static String getLanguage(CommandSender sender) {
		if (sender instanceof Player)
			return getLanguage(((Player) sender).getUniqueId());
		return getLanguage(UUIDUtils.ZERO_UUID);
	}

	public static void setLanguage(UUID uuid, String language) {
		store.setLanguage(uuid, language);
	}

	private static class PluginLanguageData {

		private List<String> supported;

		public PluginLanguageData() {
			supported = new ArrayList<>();
		}

		public void addLanguage(String language) {
			supported.add(language);
		}
	}
}
