package de.md5lukas.commons.messages;

import de.md5lukas.commons.StringHelper;
import de.md5lukas.commons.collections.ReplaceableList;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

public class Message {

	private final TextComponent[] components;
	private final String legacy;
	private final ReplaceableList asList;

	public Message(String legacy) {
		this.legacy = legacy;
		this.asList = new ReplaceableList(StringHelper.split(legacy, '\n'));
		this.asList.trimToSize();
		this.components = (TextComponent[]) TextComponent.fromLegacyText(legacy);
	}

	public String getLegacy() {
		return legacy;
	}

	public void send(CommandSender commandSender) {
		commandSender.spigot().sendMessage(components);
	}

	public void send(CommandSender commandSender, String target, String replacement, String... replacements) {
		TextComponent[] copy = cloneBaseComponents(components);

		replaceInComponents(target, replacement, copy);

		for (int i = 0; i < replacements.length; i += 2) {
			replaceInComponents(replacements[i], replacements[i + 1], copy);
		}

		commandSender.spigot().sendMessage(copy);
	}

	public BaseComponent[] getComponents() {
		return components;
	}

	public BaseComponent[] getComponentsModifiable() {
		return cloneBaseComponents(components);
	}

	public ReplaceableList getAsList() {
		return asList;
	}

	private static TextComponent[] cloneBaseComponents(TextComponent[] components) {
		TextComponent[] copy = new TextComponent[components.length];
		for (int i = 0; i < components.length; i++) {
			copy[i] = new TextComponent(components[i]);
		}
		return copy;
	}

	private static void replaceInComponents(String target, String replacement, TextComponent[] components) {
		for (TextComponent component : components) {
			component.setText(component.getText().replace(target, replacement));
		}
	}

	@Override
	public String toString() {
		return "Message{" +
			"legacy='" + legacy + '\'' +
			'}';
	}
}
