package net.axay.hglaborlobby.eventmanager.joinserver

import net.axay.kspigot.extensions.bukkit.feedSaturate
import net.axay.kspigot.extensions.bukkit.heal
import org.bukkit.entity.Player

object JoinPlayerReset {

    fun resetPlayer(player: Player) {

        player.feedSaturate()
        player.heal()

    }

}