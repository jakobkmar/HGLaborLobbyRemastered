package net.axay.hglaborlobby.functionality

import net.axay.hglaborlobby.functionality.makeLobbyItem
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.isGroundSolid
import net.axay.kspigot.runnables.taskRunLater
import net.axay.kspigot.sound.sound
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object ElytraLauncher {

    fun enable() {

        listen<PlayerInteractEvent>() {

            if (
                it.action == Action.PHYSICAL &&
                it.clickedBlock?.type == Material.BIRCH_PRESSURE_PLATE &&
                it.clickedBlock?.location?.subtract(0.0, 1.0, 0.0)?.block?.type == Material.STRIPPED_JUNGLE_WOOD
            ) {
                it.player.velocity = it.player.velocity.setY(10)

                it.player.inventory.chestplate = ItemStack(Material.ELYTRA).makeLobbyItem()
                if (!it.player.inventory.contains(Material.FEATHER))
                    it.player.inventory.addItem(ItemStack(Material.FEATHER).makeLobbyItem())

                it.player.sound(Sound.ITEM_ARMOR_EQUIP_ELYTRA)

                taskRunLater(20, sync = false) {
                    it.player.isGliding = true
                }
            } else if (
                it.material == Material.FEATHER &&
                it.player.isGliding &&
                !it.player.isGroundSolid
            ) {
                it.player.velocity = it.player.location.direction.multiply(1.5)
            }

        }

    }
}
