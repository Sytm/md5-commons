package de.md5lukas.commons.paper

import java.time.temporal.TemporalAccessor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

infix fun String.placeholder(component: Component) = Placeholder.component(this, component)

infix fun String.placeholderIgnoringArguments(component: Component) =
    TagResolver.resolver(this) { _, _ -> Tag.inserting(component) }

infix fun String.placeholder(text: String) = Placeholder.unparsed(this, text)

infix fun String.placeholder(number: Number) = Formatter.number(this, number)

infix fun String.placeholder(temporal: TemporalAccessor) = Formatter.date(this, temporal)

inline fun textComponent(builder: TextComponent.Builder.() -> Unit): TextComponent =
    Component.text().apply(builder).build()
