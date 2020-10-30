package net.axay.hglaborlobby.eventmanager.leaveserver

import net.axay.kspigot.event.listen
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerKickEvent

object KickMessageListener {

    fun enable() {

        listen<PlayerKickEvent>(priority = EventPriority.LOWEST) {
            OnLeaveManager.registeredKickReasons[it.player] = it.reason
        }

    }

}