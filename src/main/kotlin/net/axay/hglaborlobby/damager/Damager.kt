package net.axay.hglaborlobby.damager

import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.feed
import net.axay.kspigot.extensions.bukkit.heal
import net.axay.kspigot.extensions.bukkit.saturate
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.extensions.geometry.vec
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.task
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack

val world = Bukkit.getWorld("world")

val locationArea = LocationArea(vec(95.0, 60.0, 95.0).toLocation(world!!), vec(105.0, 70.0, 105.0).toLocation(world))
val locationArea2 = LocationArea(vec(0.0, 60.0, 0.0).toLocation(world!!), vec(5.0, 70.0, 5.0).toLocation(world))
val locationArea3 = LocationArea(vec(-10.0, 60.0, -10.0).toLocation(world!!), vec(-15.0, 70.0, -15.0).toLocation(world))
//TODO config zeug
//val damagers = de.royzer.utils.Config.config.readConfig()
val damagers = listOf<LocationArea>(locationArea, locationArea2, locationArea3)

val Player.isInDamager: Boolean
    get() {
        var res = false
        damagers.forEach {
            if (it.isInArea(this.location)) res = true
        }
        return res
    }

object Damager {

    var playersInDamager = mutableListOf<String>()
    var playerDamage = hashMapOf<String, Double>()
    var disabled = false

    fun enable() {

        listen<PlayerDropItemEvent> {
            if (it.isDamagerTrash()) {
                for (damager in damagers) {
                    if (damager.isInArea(it.itemDrop.location)) {
                        it.itemDrop.remove()
                    }
                }
            }
        }

        listen<PlayerDeathEvent> {
            if (it.entity.isInDamager) it.drops.clear()
        }

        task(
            sync = false,
            period = 12L
        ) {
            if (disabled) return@task
            checkPlayerPositions()

            for (playerName in playersInDamager) {
                val player = Bukkit.getPlayerExact(playerName)
                val damage = playerDamage[player?.name]
                sync { if (damage != null) player?.damage(damage) }
            }
        }
    }


    private fun checkPlayerPositions() {
        for (player in onlinePlayers) {
            for (damager in damagers) {
                if (damager.isInArea(player.location)) {
                    if (player.name !in playersInDamager) {
                        sync { giveItems(player) }
                        playersInDamager.plusAssign(player.name)
                        if (player.name !in playerDamage.keys) playerDamage[player.name] = 5.0 // add player to playerDamage Map if he was not in the damager before
                    }
                    break
                } else {
                    if (player.name in playersInDamager && !player.isInDamager) {
                        playersInDamager.minusAssign(player.name) // remove player from damager
                        player.sendMessage("Du hast den Damager verlassen")
                        player.inventory.clear()
                        player.heal()
                        player.feed()
                        break
                    }
                }
            }
        }

    }


    private fun PlayerDropItemEvent.isDamagerTrash() = when (itemDrop.itemStack.type) {
        Material.BROWN_MUSHROOM, Material.BOWL, Material.RED_MUSHROOM, Material.MUSHROOM_STEW -> true
        else -> false
    }


    private fun giveItems(player: Player) {
        player.health = 20.0
        player.feed()
        player.saturate()
        player.inventory.clear()
        player.gameMode = GameMode.SURVIVAL

        for (i in (1..13)) player.inventory.addItem(ItemStack(Material.MUSHROOM_STEW))

        player.inventory.addItem(ItemStack(Material.BOWL, 64))
        player.inventory.addItem(ItemStack(Material.BROWN_MUSHROOM, 64))
        player.inventory.addItem(ItemStack(Material.RED_MUSHROOM, 64))
        for (i in (1..21)) player.inventory.addItem(ItemStack(Material.MUSHROOM_STEW))

    }
}