package de.md5lukas.commons;

import net.md_5.bungee.api.ChatColor;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

public class StringHelper {

	public static final java.util.regex.Pattern MC_USERNAME_PATTERN = java.util.regex.Pattern.compile("^\\w{3,16}$");

	
	public static String repeatString( String s, int amount) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < amount; ++i)
			sb.append(s);
		return sb.toString();
	}

	
	public static String multiReplace( String string,  String target,  String replacement,  String... targetsAndReplacements) {
		checkArgument(targetsAndReplacements.length % 2 == 0, "The amount targets and replacements need to be even");

		string = string.replace(target, replacement);
		for (int i = 0; i < targetsAndReplacements.length; i += 2) {
			string = string.replace(targetsAndReplacements[i], targetsAndReplacements[i + 1]);
		}
		return string;
	}

	
	public static String buildStringFromArray( String[] array, int offset) {
		StringJoiner sj = new StringJoiner(" ");
		for (int i = offset; i < array.length; i++) {
			sj.add(array[i]);
		}
		return sj.toString();
	}
	
	public static List<String> wrap( String input, int maxLineLength) {
		StringTokenizer tok = new StringTokenizer(input, " ");
		List<String> output = new ArrayList<String>();
		StringBuilder currentLine = new StringBuilder();
		while (tok.hasMoreTokens()) {
			String word = tok.nextToken();

			if (currentLine.length() + word.length() > maxLineLength) {
				output.add(currentLine.toString());
				currentLine.setLength(0);
			}
			currentLine.append(word).append(" ");
		}
		if (currentLine.length() != 0)
			output.add(currentLine.toString());
		return output;
	}

	// https://codereview.stackexchange.com/a/59157
	public static List<String> split(String strToSplit, char delimiter) {
		List<String> arr = new ArrayList<>();
		int foundPosition;
		int startIndex = 0;
		while ((foundPosition = strToSplit.indexOf(delimiter, startIndex)) > -1) {
			arr.add(strToSplit.substring(startIndex, foundPosition));
			startIndex = foundPosition + 1;
		}
		arr.add(strToSplit.substring(startIndex));
		return arr;
	}

	public static String getLastChatColor(String input) {
		Set<Character> lastEffects = new HashSet<>(5);
		char lastColor = Character.MIN_VALUE;

		boolean isCode = false;
		for (char c : input.toCharArray()) {
			if (ChatColor.COLOR_CHAR == c) {
				isCode = true;
				continue;
			}
			if (isCode) {
				if (isEffect(c)) {
					lastEffects.add(c);
				} else if (isColor(c)) {
					lastColor = c;
					lastEffects.clear();
				} else if (isReset(c)) {
					lastEffects.clear();
					lastColor = Character.MIN_VALUE;
				}
				isCode = false;
			}
		}
		StringBuilder sb = new StringBuilder(((lastColor == Character.MIN_VALUE ? 0 : 1) + lastEffects.size()) * 2);
		sb.append(ChatColor.COLOR_CHAR).append(lastColor);
		for (char c : lastEffects)
			sb.append(ChatColor.COLOR_CHAR).append(c);
		return sb.toString();
	}

	private static boolean isColor(char color) {
		switch (ChatColor.getByChar(color)) {
			case BOLD:
			case ITALIC:
			case MAGIC:
			case RESET:
			case STRIKETHROUGH:
			case UNDERLINE:
				return false;
			default:
				return true;
		}
	}

	private static boolean isEffect(char color) {
		switch (ChatColor.getByChar(color)) {
			case BOLD:
			case ITALIC:
			case MAGIC:
			case RESET:
			case UNDERLINE:
				return true;
			default:
				return false;
		}
	}

	private static boolean isReset(char color) {
		return ChatColor.RESET.toString().charAt(1) == color;
	}
}