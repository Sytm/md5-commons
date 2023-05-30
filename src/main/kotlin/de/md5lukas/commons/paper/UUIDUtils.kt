package de.md5lukas.commons.paper

import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import org.bukkit.Bukkit

/**
 * A helper class providing access to Mojang APIs to access player name and uuid conversion, even if
 * the player itself is not online on the server
 *
 * @constructor Creates a new instance of UUIDUtils using the executor to perform the blocking
 *   network tasks
 * @property executor The executor to perform async operations on
 */
class UUIDUtils(private val executor: Executor) {

  /**
   * Looks up a players uuid in an async thread.
   *
   * @param name The name of the player to look up
   */
  fun getUUIDAsync(name: String): CompletableFuture<UUID?> {
    return CompletableFuture.supplyAsync(
        {
          val profile = Bukkit.createProfile(name)
          if (!profile.isComplete) {
            profile.complete(false)
          }
          profile.id
        },
        executor)
  }

  /**
   * Looks up a players name in an async thread
   *
   * @param uuid The uuid of the player to look up
   */
  fun getNameAsync(uuid: UUID): CompletableFuture<String?> {
    return CompletableFuture.supplyAsync(
        {
          val profile = Bukkit.createProfile(uuid)
          if (!profile.isComplete) {
            profile.complete(false)
          }
          profile.name
        },
        executor)
  }
}
