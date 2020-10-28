package net.axay.hglaborlobby.protection

import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.CraftingInventory

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
            val clickedInv = it.clickedInventory ?: return@listen

            fun belongsToPlayer(player: Player): Boolean {
                return when {
                    clickedInv == player.inventory -> true
                    clickedInv is CraftingInventory && clickedInv.holder as? Player == player -> true
                    else -> false
                }
            }

            if (whoClicked is Player)
                if (belongsToPlayer(whoClicked))
                    GeneralProtectionUtils.checkPlayerAction(it, whoClicked)

        }

        listen<PlayerSwapHandItemsEvent> {
            GeneralProtectionUtils.checkPlayerAction(it)
        }

        listen<PlayerDeathEvent> {
            it.drops.clear()
        }

        listen<PlayerDropItemEvent> {
            GeneralProtectionUtils.checkPlayerAction(it)
        }

        listen<PlayerTakeLecternBookEvent> {
            GeneralProtectionUtils.checkPlayerAction(it)
        }

    }

}