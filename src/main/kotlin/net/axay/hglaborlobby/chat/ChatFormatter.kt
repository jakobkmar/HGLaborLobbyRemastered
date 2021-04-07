package net.axay.hglaborlobby.chat

import net.axay.hglaborlobby.main.lobbyPermission
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.clickEvent
import net.axay.kspigot.chat.hoverEventText
import net.axay.kspigot.chat.sendMessage
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.md_5.bungee.api.chat.ClickEvent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.player.AsyncPlayerChatEvent

object ChatFormatter : CommandExecutor {
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
        ":right:" to "→",
        ":copyright:" to "©",
        ":swords:" to "⚔",
        ":sword:" to "🗡",
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

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage {
            emojis.values.forEach {
                text("$it ") {
                    hoverEventText {
                        text("Click to copy") {
                            color = KColors.CORAL
                        }
                    }
                    clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, it)
                }
            }
        }

        return true
    }
}
