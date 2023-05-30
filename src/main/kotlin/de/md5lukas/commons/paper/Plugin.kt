package de.md5lukas.commons.paper

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerEvents(listener: Listener) {
  server.pluginManager.registerEvents(listener, this)
}
