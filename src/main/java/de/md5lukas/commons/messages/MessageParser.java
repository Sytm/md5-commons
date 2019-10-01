package de.md5lukas.commons.messages;

import com.google.common.collect.ImmutableMap;
import net.md_5.bungee.api.ChatColor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageParser {

	private static final MessageParser defaultParser = new MessageParser();

	public static MessageParser getDefaultParser() {
		return defaultParser;
	}

	public static MessageParser get(String commentPrefix, String variablePrefix, String variableEnclosing) {
		return new MessageParser(commentPrefix, variablePrefix, variableEnclosing);
	}

	private final String commentPrefix;
	private final String variablePrefix;
	private final String variableEnclosing;

	private MessageParser() {
		commentPrefix = "#";
		variablePrefix = "variable.";
		variableEnclosing = "$";
	}

	private MessageParser(String commentPrefix, String variablePrefix, String variableEnclosing) {
		this.commentPrefix = commentPrefix;
		this.variablePrefix = variablePrefix;
		this.variableEnclosing = variableEnclosing;
	}

	public ParseResult parse(InputStream in) throws IOException {
		return parse(new InputStreamReader(in));
	}

	public ParseResult parse(Reader reader) throws IOException {
		BufferedReader br;
		if (reader instanceof BufferedReader)
			br = (BufferedReader) reader;
		else
			br = new BufferedReader(reader);
		List<String> lines = new ArrayList<>();
		while (true) {
			String line = br.readLine();
			if (line == null)
				break;
			lines.add(line);
		}
		return parse(lines);
	}

	public ParseResult parse(File file) throws IOException {
		return parse(file.toPath());
	}

	public ParseResult parse(Path path) throws IOException {
		return parse(Files.readAllLines(path, StandardCharsets.UTF_8));
	}

	public ParseResult parse(List<String> lines) {
		Map<String, String> original = new HashMap<>(), variables = new HashMap<>();

		{ // Parsing scope
			String key = null;
			StringBuilder value = new StringBuilder();
			boolean insertNewLine = true;

			for (String line : lines) {
				line = trimStart(line);
				if (line.startsWith(commentPrefix) || line.isEmpty())
					continue;
				int index;
				if ((index = line.indexOf("=")) > 0) {
					if (key != null) {
						if (key.startsWith(variablePrefix))
							variables.put(key.substring(variablePrefix.length()), value.toString());
						else
							original.put(key, value.toString());
						value.setLength(0);
					}
					key = line.substring(0, index);
					value.append(line.substring(index + 1));
					insertNewLine = true;
				} else if ((index = line.indexOf("|")) == 0) {
					if (insertNewLine) {
						value.append('\n');
					} else {
						insertNewLine = true;
					}
					value.append(line.substring(1));
				} else if (index > 0) {
					if (key != null) {
						if (key.startsWith(variablePrefix))
							variables.put(key.substring(variablePrefix.length()), value.toString());
						else
							original.put(key, value.toString());
						value.setLength(0);
					}
					key = line.substring(0, index);
					insertNewLine = false;
				}
			}
			if (key != null) {
				if (key.startsWith(variablePrefix))
					variables.put(key.substring(variablePrefix.length()), value.toString());
				else
					original.put(key, value.toString());
				value.setLength(0);
			}
		}
		original.forEach((key, str) -> original.put(key, ChatColor.translateAlternateColorCodes('&', str)));
		variables.forEach((key, str) -> variables.put(key, ChatColor.translateAlternateColorCodes('&', str)));

		Map<String, Message> messages = new HashMap<>();

		for (Map.Entry<String, String> e : original.entrySet()) {
			String messageKey = e.getKey();
			String message = e.getValue();

			for (Map.Entry<String, String> entry : variables.entrySet()) {
				String variableKey = entry.getKey();
				String variable = entry.getValue();
				message = message.replace(variableEnclosing + variableKey + variableEnclosing, variable);
			}

			messages.put(messageKey, new Message(message));
		}
		return new ParseResult(original, messages, variables);
	}

	private String trimStart(String string) {
		int length = string.length(), start = 0;
		while ((start < length) && (string.charAt(start) <= ' ')) {
			start++;
		}
		return string.substring(start);
	}

	public final static class ParseResult {

		private final ImmutableMap<String, String> original, variables;
		private final ImmutableMap<String, Message> messages;

		private ParseResult(Map<String, String> original, Map<String, Message> messages, Map<String, String> variables) {
			this.original = ImmutableMap.copyOf(original);
			this.messages = ImmutableMap.copyOf(messages);
			this.variables = ImmutableMap.copyOf(variables);
		}

		public ImmutableMap<String, String> getOriginal() {
			return original;
		}

		public ImmutableMap<String, Message> getMessages() {
			return messages;
		}

		public ImmutableMap<String, String> getVariables() {
			return variables;
		}

		@Override
		public String toString() {
			return "ParseResult{" +
				"original=" + original +
				", messages=" + messages +
				", variables=" + variables +
				'}';
		}
	}
}
