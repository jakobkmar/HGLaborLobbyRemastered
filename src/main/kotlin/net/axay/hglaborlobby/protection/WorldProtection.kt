package net.axay.hglaborlobby.protection

import net.axay.kspigot.event.listen
import org.bukkit.GameMode
import org.bukkit.event.player.PlayerInteractEvent

object WorldProtection {

    fun enable() {

        listen<PlayerInteractEvent> {

            val player = it.player

            if (player.gameMode.isRestricted) {
                it.isCancelled = true
            }

        }

    }

}

private val GameMode.isRestricted get() = when(this) {
    GameMode.ADVENTURE, GameMode.SURVIVAL -> true
    else -> false
}