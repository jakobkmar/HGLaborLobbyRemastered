package net.axay.hglaborlobby.pads

import net.axay.kspigot.event.listen
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

object ElytraLauncher {

    fun enable() {

        listen<PlayerInteractEvent>() {
            if (it.clickedBlock?.type == Material.BIRCH_PRESSURE_PLATE &&
                it.clickedBlock?.location?.subtract(0.0, 1.0, 0.0)?.block?.type == Material.STRIPPED_JUNGLE_WOOD
            ) {
                it.player.velocity = Vector(0, 4, 0)
                it.player.inventory.chestplate = ItemStack(Material.ELYTRA)
                it.player.inventory.addItem(ItemStack(Material.FEATHER))
            }

            if (it.player.inventory.itemInMainHand.type == Material.FEATHER &&
                it.player.isGliding &&
                it.action == Action.RIGHT_CLICK_AIR
            ) {
                it.player.velocity = it.player.location.direction.multiply(1.5)
            }

        }



    }
}


