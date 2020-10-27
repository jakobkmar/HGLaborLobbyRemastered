package net.axay.hglaborlobby.protection

import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

object ServerProtection {

    fun enable() {

        listen<PlayerInteractEvent> {
            GeneralProtectionUtils.checkPlayerAction(it)
        }

        listen<PlayerInteractEntityEvent> {
            GeneralProtectionUtils.checkPlayerAction(it)
        }

        listen<EntityDamageEvent> {
            if (it is Player)
                it.isCancelled = true
        }

        listen<EntityDamageByEntityEvent> {

            val damager = it.damager

            if (damager is Player)
                GeneralProtectionUtils.checkPlayerAction(it, damager)

            else if (damager is Projectile) {
                val source = damager.shooter
                if (source is Player)
                    GeneralProtectionUtils.checkPlayerAction(it, source)
            }

        }

        listen<InventoryClickEvent> {

            val whoClicked = it.whoClicked

            if (whoClicked is Player)
                if (it.clickedInventory == whoClicked.inventory)
                    GeneralProtectionUtils.checkPlayerAction(it, whoClicked)

        }

        listen<InventoryMoveItemEvent> {

            val source = it.source
            val holder = source.holder

            if (holder is Player)
                if (holder.inventory == source)
                    GeneralProtectionUtils.checkPlayerAction(it, holder)

        }

        listen<PlayerDeathEvent> {
            it.drops.clear()
        }

        listen<PlayerDropItemEvent> {
            GeneralProtectionUtils.checkPlayerAction(it)
        }

    }

}