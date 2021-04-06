package net.axay.hglaborlobby.gui.guis

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.gui.*
import net.axay.kspigot.utils.hasMark
import org.bukkit.event.player.PlayerInteractEvent

object HGQueueGUI {
    fun enable() {
        listen<PlayerInteractEvent> {
            if (!it.action.isRightClick) return@listen
            if (it.item?.hasMark("hgqueue") == true) {
                it.player.performCommand("hgqueue")
                it.isCancelled = true
            }
        }
    }
}
