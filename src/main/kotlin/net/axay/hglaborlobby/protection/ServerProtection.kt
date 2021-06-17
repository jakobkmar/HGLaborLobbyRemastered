package net.axay.hglaborlobby.protection

import net.axay.hglaborlobby.damager.isInDamager
import net.axay.hglaborlobby.functionality.isLobbyItem
import net.axay.hglaborlobby.minigames.isInWaterfightRegion
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.isSimple
import org.bukkit.Material
import org.bukkit.block.data.type.Door
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.entity.Trident
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.CraftingInventory

object ServerProtection {
    fun enable() {
        listen<PlayerInteractEvent> {
            if (it.action == Action.RIGHT_CLICK_BLOCK || it.action == Action.RIGHT_CLICK_AIR) {
                val shouldAllow = when {
                    it.item?.type == Material.FIREWORK_ROCKET && it.clickedBlock?.type?.isInteractable != true -> true
                    it.clickedBlock?.type == Material.NOTE_BLOCK || it.clickedBlock?.type == Material.BELL -> true
                    it.clickedBlock?.blockData is Door -> true
                    it.item?.type == Material.TRIDENT -> true
                    else -> false
                }
                if (shouldAllow)
                    return@listen
            }
            GeneralProtectionUtils.checkPlayerAction(it)
        }

        listen<PlayerInteractEntityEvent> {
            GeneralProtectionUtils.checkPlayerAction(it)
        }

        listen<PlayerArmorStandManipulateEvent> {
            GeneralProtectionUtils.checkPlayerAction(it)
        }

        listen<EntityDamageEvent> {
            if (it.entity is Player)
                if (!(it.entity as Player).isInDamager && !(it.entity as Player).isInWaterfightRegion)
                    it.isCancelled = true
        }

        listen<FoodLevelChangeEvent> {
            it.isCancelled = true
        }

        listen<EntityDamageByEntityEvent> {
            val damager = it.damager

            if (damager is Player) {
                if(it.entity is Player && (it.damager as Player).isInWaterfightRegion) {
                    return@listen
                }
                GeneralProtectionUtils.checkPlayerAction(it, damager)
            }
            else if (damager is Projectile) {
                if (damager is Firework) {
                    it.isCancelled = true
                } else {
                    val source = damager.shooter
                    if (source is Player) {
                        if(damager !is Trident || it.entity !is Player) {
                            GeneralProtectionUtils.checkPlayerAction(it, source)
                        }
                    }
                }
            }
        }

        listen<InventoryClickEvent> {
            val whoClicked = it.whoClicked
            val clickedInv = it.clickedInventory ?: return@listen

            if ((it.whoClicked as Player).isInDamager) return@listen

            fun belongsToPlayer(player: Player): Boolean {
                return when {
                    clickedInv == player.inventory -> true
                    clickedInv is CraftingInventory && clickedInv.holder as? Player == player -> true
                    else -> false
                }
            }

            if (whoClicked is Player)
                if (belongsToPlayer(whoClicked)) {
                    if (it.currentItem?.isLobbyItem == true || !it.action.isSimple)
                        GeneralProtectionUtils.checkPlayerAction(it, whoClicked)
                }
        }

        listen<PlayerSwapHandItemsEvent> {
            if (it.mainHandItem?.isLobbyItem == true || it.offHandItem?.isLobbyItem == true)
                GeneralProtectionUtils.checkPlayerAction(it)
        }

        listen<PlayerDeathEvent> {
            it.drops.clear()
            if (it.entity.isInDamager) it.deathMessage = null
        }

        listen<PlayerDropItemEvent> {
            if (it.player.isInDamager)
                it.itemDrop.remove()
            else if (it.itemDrop.itemStack.isLobbyItem)
                GeneralProtectionUtils.checkPlayerAction(it)
        }

        listen<PlayerTakeLecternBookEvent> {
            GeneralProtectionUtils.checkPlayerAction(it)
        }
    }
}
