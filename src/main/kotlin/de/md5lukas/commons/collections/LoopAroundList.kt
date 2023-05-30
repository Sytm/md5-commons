package de.md5lukas.commons.collections

import com.google.common.base.Preconditions

/**
 * A simple class that will present a cutout based on the index and will, if required loop around
 * <br></br> Example:
 * ```
 * |1 2 3 4 5 6 7 8|
 * |      <----->  |
 * ```
 *
 * [next]
 *
 * ```
 * |1 2 3 4 5 6 7 8|
 * |        <----->|
 * ```
 *
 * [next]
 *
 * ```
 * |1 2 3 4 5 6 7 8|
 * |>         <----|
 * ```
 *
 * [next]
 *
 * ```
 * |1 2 3 4 5 6 7 8|
 * |-->         <--|
 * ```
 *
 * @param T The type of the list
 * @constructor Creates a new instance of this list with the specified cutout size
 * @property cutOutSize The cutout size to use
 * @throws IllegalArgumentException If cutout size is equal or lower than zero
 */
class LoopAroundList<T>(private val cutOutSize: Int) : ArrayList<T>() {

  private var index: Int = 0

  init {
    Preconditions.checkArgument(
        cutOutSize > 0, "The cutout size needs to be greater than zero, but was %d", cutOutSize)
  }

  /**
   * Moves the cutout one forward
   *
   * @return The current index
   */
  fun next(): Int = move(1)

  /**
   * Moves the cutout one backwards
   *
   * @return The current index
   */
  fun previous(): Int = move(-1)

  /**
   * Moves the cutout the specified amount. The value may be negative
   *
   * @param steps The amount of steps to move
   * @return The current index
   */
  fun move(steps: Int): Int = setIndex(index + steps)

  /**
   * Sets the index of the cutout start to the given value
   *
   * @param index The new index
   * @return The current index corrected for wrap-around
   */
  fun setIndex(index: Int): Int {
    this.index = Math.floorMod(index, size)
    return this.index
  }

  val cutOut: List<T>
    /** @return The values that are currently in the cutout */
    get() {
      val result = ArrayList<T>()
      for (i in 0 until cutOutSize) {
        var actualIndex = i - index
        actualIndex = if (actualIndex >= 0) actualIndex else actualIndex + size
        result.add(get(actualIndex))
      }
      return result
    }
}
