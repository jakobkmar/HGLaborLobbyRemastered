package net.axay.hglaborlobby.functionality

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.getHandItem
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object SoupHealing {

    fun enable() {

        listen<PlayerInteractEvent> { event ->

            if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return@listen

            val p = event.player

            val interactItem = p.getHandItem(event.hand)
            if (interactItem != null && interactItem.type.isStew) {

                var ifConsume = false

                if (p.health < p.healthScale) {
                    (p.health + 7).let { if (it >= p.healthScale) p.health = p.healthScale else p.health = it }
                    ifConsume = true
                }

                else if (p.foodLevel < 20) {
                    (p.foodLevel + interactItem.type.restoredFood).let {
                        if (it >= 20) p.foodLevel = 20 else p.foodLevel = it
                    }
                    (p.saturation + interactItem.type.restoredSaturation).let {
                        if (it >= p.foodLevel) p.saturation = p.foodLevel.toFloat() else p.saturation = it
                    }
                    p.playSound(p.location, Sound.ENTITY_PLAYER_BURP, 1f, 1f)
                    ifConsume = true
                }

                if (ifConsume) interactItem.type = Material.BOWL

            }

        }

    }

    private val Material.isStew: Boolean
        get() = when (this) {
            Material.MUSHROOM_STEW -> true
            Material.BEETROOT_SOUP -> true
            Material.RABBIT_STEW -> true
            else -> false
        }

    private val Material.restoredFood: Int
        get() = when (this) {
            Material.MUSHROOM_STEW, Material.BEETROOT_SOUP -> 6
            Material.RABBIT_STEW -> 10
            else -> 0
        }

    private val Material.restoredSaturation: Float
        get() = when (this) {
            Material.MUSHROOM_STEW, Material.BEETROOT_SOUP -> 7.2f
            Material.RABBIT_STEW -> 12f
            else -> 0f
        }

}
