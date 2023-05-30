package de.md5lukas.commons.paper

import org.bukkit.configuration.ConfigurationSection

fun ConfigurationSection.getStringNotNull(path: String): String =
    getString(path)
        ?: throw IllegalArgumentException(
            "The configuration key ${getFullPath(path)} is not present")

private fun ConfigurationSection.getFullPath(path: String): String =
    if (currentPath.isNullOrEmpty()) {
      path
    } else {
      "$currentPath.$path"
    }
