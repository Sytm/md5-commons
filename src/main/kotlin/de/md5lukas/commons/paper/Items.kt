package de.md5lukas.commons.paper

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

var ItemStack.plainDisplayName: String
  get() =
      if (hasItemMeta()) {
        val meta = itemMeta
        if (meta.hasDisplayName()) {
          meta.displayName()?.let { PlainTextComponentSerializer.plainText().serialize(it) } ?: ""
        } else ""
      } else ""
  set(value) {
    editMeta { it.displayName(Component.text(value)) }
  }

fun ItemStack.appendLore(lore: List<Component>) {
  val currentLore = lore()
  lore(
      if (currentLore === null) {
        lore
      } else {
        currentLore + lore
      })
}

inline fun <reified T : ItemMeta> ItemStack.editMeta(noinline block: T.() -> Unit) {
  editMeta(T::class.java, block)
}
