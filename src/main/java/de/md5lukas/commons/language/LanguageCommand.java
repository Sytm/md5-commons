package de.md5lukas.commons.language;

import com.google.common.base.Joiner;
import de.md5lukas.commons.UUIDUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			Joiner j = Joiner.on(" ,");
			StringBuilder sb = new StringBuilder("§eSupported Languages: ").append(j.join(Languages.getFullySupportedLanguages()));
			if (!Languages.getPartiallySupportedLanguages().isEmpty())
				sb.append("\nPartially supported languages: ").append(j.join(Languages.getPartiallySupportedLanguages()));
			sb.append("\nSelect your language with §a/language <Your Language>");
			sender.sendMessage(sb.toString());
		} else {
			String language = args[0].toLowerCase();
			if (Languages.isSupportedLanguage(language)) {
				Languages.setLanguage(sender instanceof Player ? ((Player) sender).getUniqueId() : UUIDUtils.ZERO_UUID, language);
				sender.sendMessage("§eYour new language has been set successfully.");
			} else {
				sender.sendMessage("§eSorry, but that language is not supported.");
			}
		}
		return true;
	}
}
