package net.axay.hglaborlobby.chat

import net.axay.hglaborlobby.main.lobbyPermission
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import org.bukkit.event.player.AsyncPlayerChatEvent

object ChatFormatter {

    private val emojis = mapOf(
        ":sad:" to "☹",
        ":happy:" to "☺",
        ":smile:" to "☻",
        ":toxic:" to "☣",
        ":peace:" to "☮",
        ":dead:" to "☠",
        ":sun:" to "☼",
        ":king:" to "♕",
        ":star:" to "★",
        ":heart:" to "❤",
        ":note:" to "♬",
        ":yep:" to "✔",
        ":nop:" to "✖",
        ":and:" to "&",
        ":left:" to "←",
        ":right:" to "→"
        ":copyright:" to "©"
        ":swords:" to "⚔"
        ":flame:" to "🔥"
    )

    fun enable() {

        listen<AsyncPlayerChatEvent> {

            if (it.isCancelled) return@listen

            it.isCancelled = true

            val player = it.player

            var finalMessage = it.message

            if (player.hasPermission(lobbyPermission("colorchat")))
                finalMessage = finalMessage.replace("&", "§")

            emojis.forEach { (inChat, emoji) ->
                finalMessage = finalMessage.replace(inChat, emoji)
            }

            broadcast("${KColors.GRAY}─ ${player.displayName} ${KColors.DARKGRAY}» ${KColors.WHITE}$finalMessage")

        }

    }

}
