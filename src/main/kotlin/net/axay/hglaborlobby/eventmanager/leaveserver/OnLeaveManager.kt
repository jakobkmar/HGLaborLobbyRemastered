package net.axay.hglaborlobby.eventmanager.leaveserver

import net.axay.hglaborlobby.damager.Damager
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerQuitEvent

object OnLeaveManager {

    val registeredKickReasons = HashMap<Player, String>()

    fun enable() {

        listen<PlayerQuitEvent>(EventPriority.HIGHEST) {

            val player = it.player
            val reason = registeredKickReasons.remove(player)

            it.quitMessage = null

            if (player in Damager.playerSoupsEaten) Damager.playerSoupsEaten.remove(player)

            if (reason != null) {
                broadcast(
                    StringBuilder().apply {
                        append("${KColors.PALEVIOLETRED}← ${KColors.GRAY}${player.name}")
                        append(" ${KColors.INDIANRED}${KColors.BOLD}$reason") }.toString())
            }
        }

    }

}