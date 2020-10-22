package net.axay.hglaborlobby.eventmanager.joinserver

import net.axay.hglaborlobby.security.BadIPDetection
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.async
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

object OnJoinManager {

    fun enable() {

        listen<PlayerJoinEvent>(EventPriority.HIGHEST) {

            async {

                JoinMessage.joinMessage(it.player)
                BadIPDetection.checkPlayer(it.player)

            }

        }

    }

}