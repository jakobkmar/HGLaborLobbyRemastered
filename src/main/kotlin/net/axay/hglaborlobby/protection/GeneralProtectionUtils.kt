package net.axay.hglaborlobby.protection

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerEvent

object GeneralProtectionUtils {

    fun checkPlayerAction(event: PlayerEvent) {
        checkPlayerAction(event, event.player)
    }

    fun checkPlayerAction(event: Event, player: Player) {
        if (event is Cancellable)
            if (player.gameMode.isRestricted)
                event.isCancelled = true
    }

}

val GameMode.isRestricted get() = when(this) {
    GameMode.ADVENTURE, GameMode.SURVIVAL -> true
    else -> false
}