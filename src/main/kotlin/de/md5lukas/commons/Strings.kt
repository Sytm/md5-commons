package de.md5lukas.commons

private val minecraftUsernamePattern = Regex("^\\w{3,16}$")
private val uuidPattern =
    Regex("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b")
private val nonWordCharacterRegex = Regex("\\W")

/** Checks if a string is a valid Minecraft username */
val String.isMinecraftUsername
  get() = matches(minecraftUsernamePattern)

/** Checks if a string is a UUID */
val String.isUuid
  get() = matches(uuidPattern)

val String.containsNonWordCharacter
  get() = nonWordCharacterRegex in this
