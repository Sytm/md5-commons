package de.md5lukas.commons.paper

import com.google.common.base.Preconditions
import com.google.common.math.DoubleMath
import de.md5lukas.commons.squared
import java.util.concurrent.CompletableFuture
import kotlin.math.roundToInt
import kotlin.math.sqrt
import org.bukkit.Location
import org.bukkit.entity.Player

operator fun Location.component1() = x

operator fun Location.component2() = y

operator fun Location.component3() = z

/**
 * Calculates the distance between the two locations, disregarding the Y axis. The result is squared
 *
 * @param other The second location
 * @return The distance between those two locations squared
 * @receiver The first location
 * @throws NullPointerException If either location1 or location 2 are null
 * @throws IllegalArgumentException If both locations are not in the same world
 */
fun Location.distance2DSquared(other: Location): Double {
  Preconditions.checkArgument(this.world == other.world)
  return (this.x - other.x).squared + (this.z - other.z).squared
}

/**
 * Calculates the distance between the two locations, disregarding the Y axis.
 *
 * @param other The second location
 * @return The distance between those two locations
 * @receiver The first location
 * @throws NullPointerException If either location1 or location 2 are null
 * @throws IllegalArgumentException If both locations are not in the same world
 * @see .distance2DSquared
 */
fun Location.distance2D(other: Location): Double {
  return sqrt(distance2DSquared(other))
}

fun Location.fuzzyEquals(other: Location, tolerance: Double) =
    world === other.world &&
        DoubleMath.fuzzyEquals(x, other.x, tolerance) &&
        DoubleMath.fuzzyEquals(y, other.y, tolerance) &&
        DoubleMath.fuzzyEquals(x, other.x, tolerance)

val Location.isOutOfBounds: Boolean
  get() {
    if (!world.worldBorder.isInside(this)) {
      return true
    }

    return y.roundToInt() !in world.minHeight..world.maxHeight
  }

fun Player.teleportKeepOrientation(location: Location): CompletableFuture<Boolean> {
  val target = location.clone()
  target.pitch = this.location.pitch
  target.yaw = this.location.yaw
  return this.teleportAsync(target)
}
