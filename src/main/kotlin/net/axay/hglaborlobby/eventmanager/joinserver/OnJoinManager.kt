package net.axay.hglaborlobby.eventmanager.joinserver

import net.axay.hglaborlobby.functionality.PlayerSettingsHolder
import net.axay.hglaborlobby.security.BadIPDetection
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.async
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

object OnJoinManager {

    fun enable() {

        listen<PlayerJoinEvent>(EventPriority.HIGHEST) {

            val player = it.player

            async {

                val mainJoinMessage = JoinMessage.joinMessage(player)
                val ipCheckMessage = BadIPDetection.checkPlayer(player)

                val joinMessage = StringBuilder().apply {
                    append(mainJoinMessage)
                    if (ipCheckMessage != null)
                        append(" $ipCheckMessage")
                }.toString()

                onlinePlayers.forEach { messageReceiver ->
                    if (PlayerSettingsHolder[messageReceiver].ifSeeJoinMessages)
                        messageReceiver.sendMessage(joinMessage)
                }

            }

        }

    }

}