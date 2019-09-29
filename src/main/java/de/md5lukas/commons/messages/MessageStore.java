package de.md5lukas.commons.messages;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import de.md5lukas.commons.UUIDUtils;
import de.md5lukas.commons.internal.CommonsMain;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class MessageStore {

	private Map<String, Map<String, String>> messages;

	public MessageStore(File messageFolder, String fileFormat, Class<? extends Enum<?>> messageEnum) throws IOException {
		List<String> requiredMessages = Arrays.stream(messageEnum.getEnumConstants()).map(Enum::toString).collect(Collectors.toList());
		this.messages = new HashMap<>();

		MessageParser.ParseResult result = MessageParser.getDefaultParser().parse(new File(messageFolder, String.format(fileFormat, Languages.getDefaultLanguage())));
		Map<String, String> defaultMessages = result.getMessages();
		if (defaultMessages == null) {
			throw new IllegalStateException("Couldn't load default messages, see console output for more details");
		}
		this.messages.put(Languages.getDefaultLanguage(), defaultMessages);
		Languages.registerLanguage(Languages.getDefaultLanguage());

		for (String language : Locale.getISOLanguages()) {
			if (language.equals(Languages.getDefaultLanguage()))
				continue;
			File file = new File(messageFolder, String.format(fileFormat, language));
			if (!file.exists())
				continue;
			Map<String, String> messages = MessageParser.getDefaultParser().parse(file).getMessages();
			if (messages != null) {
				this.messages.put(language, messages);
				Languages.registerLanguage(language);
			}
		}
		Map<String, List<String>> missing = new HashMap<>();
		for (Map.Entry<String, Map<String, String>> en : messages.entrySet()) {
			for (String required : requiredMessages) {
				if (!en.getValue().containsKey(required)) {
					missing.compute(en.getKey(), (k, v) -> {
						if (v == null) {
							return Lists.newArrayList(required);
						}
						v.add(required);
						return v;
					});
				}
			}
		}
		if (!missing.isEmpty()) {
			CommonsMain.logger().log(Level.SEVERE, "The following messages are missing: ");
			missing.forEach((k, v) -> {
				CommonsMain.logger().log(Level.SEVERE, "Language: " + k);
				CommonsMain.logger().log(Level.SEVERE, Joiner.on('\n').join(v));
			});
			throw new IllegalArgumentException("Some translations are missing messages. Check the console for more details");
		}
	}

	public String getMessage(Enum<?> message) {
		return messages.get(Languages.getDefaultLanguage()).get(message.toString());
	}

	public String getMessage(Enum<?> message, String language) {
		if (language == null)
			return getMessage(message);
		return messages.get(language).get(message.toString());
	}

	public String getMessage(Enum<?> message, CommandSender sender) {
		if (sender == null)
			return getMessage(message);
		if(sender instanceof Player) {
			return getMessage(message, Languages.getLanguage(((Player) sender).getUniqueId()));
		} else {
			return getMessage(message, Languages.getLanguage(UUIDUtils.ZERO_UUID));
		}
	}
}
