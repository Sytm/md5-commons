package de.md5lukas.commons.messages;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import de.md5lukas.commons.internal.CommonsMain;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class MessageStore {

	private Map<String, Map<String, Message>> messages;

	public MessageStore(File messageFolder, String fileFormat, Class<? extends Enum<?>> messageEnum) throws IOException {
		List<String> requiredMessages = Arrays.stream(messageEnum.getEnumConstants()).map(Enum::toString).collect(Collectors.toList());
		this.messages = new HashMap<>();

		MessageParser.ParseResult result = MessageParser.getDefaultParser().parse(new File(messageFolder, String.format(fileFormat, Languages.getDefaultLanguage())));
		messages.put(Languages.getDefaultLanguage(), result.getMessages());
		Languages.registerLanguage(Languages.getDefaultLanguage());

		for (String language : Locale.getISOLanguages()) {
			if (language.equals(Languages.getDefaultLanguage()))
				continue;
			File file = new File(messageFolder, String.format(fileFormat, language));
			if (!file.exists())
				continue;

			this.messages.put(language, MessageParser.getDefaultParser().parse(file).getMessages());
			Languages.registerLanguage(language);
		}
		Map<String, List<String>> missing = new HashMap<>();
		for (Map.Entry<String, Map<String, Message>> en : messages.entrySet()) {
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

	public Message getMessage(Enum<?> message) {
		return messages.get(Languages.getDefaultLanguage()).get(message.toString());
	}

	public Message getMessage(Enum<?> message, String language) {
		if (language == null)
			return getMessage(message);
		return messages.get(language).get(message.toString());
	}

	public Message getMessage(Enum<?> message, CommandSender sender) {
		return getMessage(message, Languages.getLanguage(sender));
	}
}
