package de.md5lukas.commons.internal;

import de.md5lukas.commons.data.PlayerStore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class CommonsListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(AsyncPlayerPreLoginEvent e) {
		// Preload here, so the I/O is async
		PlayerStore.getPlayerStore(e.getUniqueId());
	}
}
