package net.axay.hglaborlobby.damager

import net.axay.hglaborlobby.functionality.LobbyItems.givePlayer
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.*
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.extensions.geometry.vec
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.task
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack

val world = Bukkit.getWorld("world")

val locationArea = LocationArea(vec(-490.5, 62.0, 449.5).toLocation(world!!), vec(-484.5, 65, 455.5).toLocation(world))

//val locationArea2 = LocationArea(vec(0.0, 60.0, 0.0).toLocation(world!!), vec(5.0, 70.0, 5.0).toLocation(world))
//val locationArea3 = LocationArea(vec(-10.0, 60.0, -10.0).toLocation(world!!), vec(-15.0, 70.0, -15.0).toLocation(world))
//TODO config zeug
//val damagers = de.royzer.utils.Config.config.readConfig()
val damagers = listOf<LocationArea>(locationArea)

val Player.isInDamager: Boolean
    get() {
        var res = false
        damagers.forEach {
            if (it.isInArea(this.location)) res = true
        }
        return res
    }

var Player.soupsEaten: Int
    get() {
        if (!Damager.playerSoupsEaten.containsKey(this)) return 0
        return Damager.playerSoupsEaten[this]!!
    }
    set(value) {
        if (!Damager.playerSoupsEaten.containsKey(this)) Damager.playerSoupsEaten[this] = 0
        Damager.playerSoupsEaten[this] = value
    }


object Damager {

    var playerSoupsEaten = hashMapOf<Player, Int>()
    var playersInDamager = mutableListOf<String>()
    var playerDamage = hashMapOf<String, Double>()
    var disabled = false

    fun enable() {

        DamagerDifficulty.enable()

        listen<EntityDamageEvent> {
            if (it.entity !is Player) return@listen
            val p = it.entity as Player
            if (!p.isInDamager) return@listen

            if (p.health - it.damage <= 0) {
                if (p.soupsEaten >= 92) {
                    p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F)
                    p.sendMessage("§7Du hast den Damager §2geschafft")
                    if (playerDamage[p.name]!! >= 9.0) broadcast("§6${p.name} hat den legendary Damager geschafft!")
                    giveItems(p)
                    p.soupsEaten = 0
                } else {
                    p.teleport(Location(Bukkit.getWorld("world"), -495.5, 63.0, 452.5)) // TODO add correct coordinates
                    p.sendMessage("§7Du hast den Damager §4nicht §7geschafft")
                    p.playSound(p.location, Sound.ENTITY_OCELOT_HURT, 1.0F, 1.0F)
                    givePlayer(p)
                    p.heal()
                    p.soupsEaten = 0
                }
                it.isCancelled = true
            }
        }

        listen<PlayerDropItemEvent> {
            if (it.isDamagerTrash())
                for (damager in damagers)
                    if (damager.isInArea(it.itemDrop.location))
                        it.itemDrop.remove()
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
                        if (player.name !in playerDamage.keys) playerDamage[player.name] =
                            5.0 // add player to playerDamage Map if he was not in the damager before
                    }
                    break
                } else {
                    if (player.name in playersInDamager && !player.isInDamager) {
                        playersInDamager.minusAssign(player.name) // remove player from damager
                        sync { player.gameMode = GameMode.SURVIVAL }
                        givePlayer(player)
                        player.heal()
                        player.feedSaturate()
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
        player.saturation = 0F
        player.inventory.clear()
        player.gameMode = GameMode.ADVENTURE

        player.inventory.addItem(ItemStack(Material.STONE_SWORD, 1))
        for (i in (1..12)) player.inventory.addItem(ItemStack(Material.MUSHROOM_STEW))
        player.inventory.addItem(ItemStack(Material.BOWL, 64))
        player.inventory.addItem(ItemStack(Material.BROWN_MUSHROOM, 64))
        player.inventory.addItem(ItemStack(Material.RED_MUSHROOM, 64))
        for (i in (1..21)) player.inventory.addItem(ItemStack(Material.MUSHROOM_STEW))

    }
}
