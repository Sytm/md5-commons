package de.md5lukas.commons.messages;

import com.google.common.base.Preconditions;
import de.md5lukas.commons.UUIDUtils;
import de.md5lukas.commons.internal.CommonsMain;
import de.md5lukas.commons.internal.Md5CommonsConfig;
import de.md5lukas.commons.internal.store.LanguageStore;

import java.util.*;
import java.util.stream.Collectors;

public class Languages {

	private static Map<String, Integer> supportedLanguages = new HashMap<>();
	private static int maxSize = 0;

	public static void registerLanguage(String language) {
		supportedLanguages.compute(language.toLowerCase(), (k, v) -> {
			int newVal = v == null ? 1 : v + 1;
			if (newVal > maxSize)
				maxSize = newVal;
			return newVal;
		});
	}

	public static List<String> getFullySupportedLanguages() {
		return supportedLanguages.entrySet().stream().filter(en -> en.getValue() == maxSize).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	public static List<String> getPartiallySupportedLanguages() {
		return supportedLanguages.entrySet().stream().filter(en -> en.getValue() != maxSize).map(Map.Entry::getKey).collect(Collectors.toList());
	}

	public static String getDefaultLanguage() {
		return Md5CommonsConfig.getDefaultLanguage();
	}

	public static boolean isSupportedLanguage(String language) {
		return supportedLanguages.containsKey(language.toLowerCase());
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

	public static void setLanguage(UUID uuid, String language) {
		store.setLanguage(uuid, language);
	}
}
