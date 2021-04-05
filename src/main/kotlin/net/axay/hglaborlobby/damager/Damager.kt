package net.axay.hglaborlobby.damager

import net.axay.hglaborlobby.damager.DamagerDifficulty.crapDamagerEnabled
import net.axay.hglaborlobby.damager.DamagerDifficulty.getCrapDamagerItem
import net.axay.hglaborlobby.damager.DamagerDifficulty.inconsistencyEnabled
import net.axay.hglaborlobby.data.database.holder.WarpsHolder
import net.axay.hglaborlobby.functionality.LobbyItems
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.*
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.extensions.geometry.vec
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.task
import net.axay.kspigot.utils.addEffect
import net.axay.kspigot.utils.editMeta
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.random.Random

val world = Bukkit.getWorld("world")

val locationArea = LocationArea(vec(11.5, 54.0, 32.5).toLocation(world!!), vec(18.5, 58.0, 38.5).toLocation(world))

//TODO config zeug
val damagers = listOf(locationArea)

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
    private val damagerSpawn by lazy { WarpsHolder.instance.warps.find { it.name == "Damager" }?.location }

    fun enable() {
        DamagerDifficulty.enable()

        listen<EntityDamageEvent> {
            if (it.entity !is Player) return@listen
            val p = it.entity as Player
            if (!p.isInDamager) return@listen

            if (p.health - it.damage <= 0) {
                if (p.soupsEaten >= 90) {
                    p.playSound(p.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F)
                    p.sendMessage("${KColors.GRAY}Du hast den Damager${KColors.GREEN} geschafft")
                    createFirework(p.location, playerDamage[p.name]!!)
                    if (playerDamage[p.name]!! >= 8.9 && !p.inconsistencyEnabled) broadcast("${KColors.GOLD}${p.name} hat den legendary Damager geschafft!")
                    giveItems(p)
                    playerSoupsEaten.remove(p)
                } else {
                    LobbyItems.givePlayer(p)
                    p.teleport(damagerSpawn!!)
                    p.heal()
                    p.sendMessage("${KColors.GRAY}Du hast den Damager ${KColors.RED}nicht${KColors.GRAY} geschafft")
                    p.playSound(p.location, Sound.ENTITY_OCELOT_HURT, 1.0F, 1.0F)
                    playerSoupsEaten.remove(p)
                }
                it.isCancelled = true
            }
        }

        task(
            sync = false,
            period = 12L
        ) {
            checkPlayerPositions()

            for (playerName in playersInDamager) {
                val player = Bukkit.getPlayerExact(playerName) ?: continue
                var damage = playerDamage[player.name]

                if (player.inconsistencyEnabled) {
                    val damagePair = DamagerDifficulty.inconsistencyRanges[player.name] ?: continue
                    damage = Random.nextInt(damagePair.first, damagePair.second + 1).toDouble()
                }
                if (player.crapDamagerEnabled)
                    if (Random.nextInt(4) == 3) player.inventory.addItem(
                        getCrapDamagerItem()
                    )

                sync { if (damage != null) player.damage(damage) }
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
                        LobbyItems.givePlayer(player)
                        player.heal()
                        player.feedSaturate()
                        playersInDamager.minusAssign(player.name) // remove player from damager
                        playerSoupsEaten.remove(player)
                        break
                    }
                }
            }
        }
    }

    private fun createFirework(location: Location, damage: Double) {
        (location.world?.spawnEntity(location, EntityType.FIREWORK) as Firework).editMeta {
            addEffect {
                val c = fireworkColor(damage)
                withColor(c)
                withFade(c)
            }
        }
    }

    private fun fireworkColor(damage: Double): Color {
        return when (damage) {
            1.0, 2.0, 3.0 -> Color.WHITE
            4.0 -> Color.GREEN
            5.0 -> Color.LIME
            6.0 -> Color.YELLOW
            7.0 -> Color.ORANGE
            8.0 -> Color.RED
            else -> Color.PURPLE
        }
    }

    val PlayerDropItemEvent.isDamagerTrash
        get() = when (itemDrop.itemStack.type) {
            Material.BROWN_MUSHROOM, Material.BOWL, Material.RED_MUSHROOM, Material.MUSHROOM_STEW, Material.STONE_SWORD -> true
            else -> false
        }

    private fun giveItems(player: Player) {
        player.health = 20.0
        player.feed()
        player.saturation = 0F
        player.inventory.clear()

        player.inventory.addItem(ItemStack(Material.STONE_SWORD, 1))
        for (i in (1..12)) player.inventory.addItem(ItemStack(Material.MUSHROOM_STEW))
        player.inventory.addItem(ItemStack(Material.BOWL, 64))
        player.inventory.addItem(ItemStack(Material.BROWN_MUSHROOM, 64))
        player.inventory.addItem(ItemStack(Material.RED_MUSHROOM, 64))
        for (i in (1..21)) player.inventory.addItem(ItemStack(Material.MUSHROOM_STEW))
    }
}
