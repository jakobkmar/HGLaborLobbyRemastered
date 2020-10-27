package net.axay.hglaborlobby.protection

import net.axay.kspigot.event.listen
import org.bukkit.GameMode
import org.bukkit.event.Cancellable
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

object WorldProtection {

    fun enable() {

        listen<PlayerInteractEvent> {
            checkPlayerAction(it)
        }

        listen<PlayerInteractEntityEvent> {
            checkPlayerAction(it)
        }

    }

    private fun checkPlayerAction(event: PlayerEvent) {
        if (event is Cancellable)
            if (event.player.gameMode.isRestricted)
                event.isCancelled = true
    }

}

private val GameMode.isRestricted get() = when(this) {
    GameMode.ADVENTURE, GameMode.SURVIVAL -> true
    else -> false
}