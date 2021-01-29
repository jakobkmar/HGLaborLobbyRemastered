package net.axay.hglaborlobby.eventmanager.joinserver

import net.axay.hglaborlobby.data.database.holder.PlayerSettingsHolder
import net.axay.hglaborlobby.data.database.holder.WarpsHolder
import net.axay.hglaborlobby.security.BadIPDetection
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlineSenders
import net.axay.kspigot.runnables.async
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

object OnJoinManager {

    fun enable() {

        listen<PlayerJoinEvent>(EventPriority.HIGHEST) {

            val player = it.player


            JoinPlayerReset.resetPlayer(player)
            JoinTablist.setTablist(player)

            WarpsHolder.instance.spawn?.let { spawn -> player.teleport(spawn.location) }

            async {

                val mainJoinMessage = JoinMessage.joinMessage(player)
                val ipCheckMessage = BadIPDetection.checkPlayer(player)

                val joinMessage = StringBuilder().apply {
                    append(mainJoinMessage)
                    if (ipCheckMessage != null)
                        append(" $ipCheckMessage")
                }.toString()

                onlineSenders.forEach { messageReceiver ->
                    if (
                        (messageReceiver is Player && PlayerSettingsHolder[messageReceiver].ifSeeJoinMessages) ||
                        messageReceiver !is Player
                    )
                        messageReceiver.sendMessage(joinMessage)
                }

            }

        }

    }

}