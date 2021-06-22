package net.axay.hglaborlobby.eventmanager.joinserver

import net.axay.hglaborlobby.data.database.holder.WarpsHolder
import net.axay.hglaborlobby.security.BadIPDetection
import net.axay.hglaborlobby.security.VPNCommand
import net.axay.kspigot.event.listen
import net.axay.kspigot.runnables.async
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

object OnJoinManager {

    fun enable() {

        listen<PlayerJoinEvent>(EventPriority.HIGHEST) {
            it.joinMessage = null

            val player = it.player

            JoinPlayerReset.resetPlayer(player)
            JoinTablist.setTablist(player)

            WarpsHolder.instance.spawn?.let { warp ->
                player.teleport(warp.location)
            }

            async {
                if (VPNCommand.isActive) {
                    if (!player.hasPermission("hglabor.bypassvpn")) {
                        BadIPDetection.checkPlayer(player)
                    }
                }
            }
        }

    }
}