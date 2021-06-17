package net.axay.hglaborlobby.minigames

import net.axay.hglaborlobby.damager.DamagerDifficulty
import net.axay.hglaborlobby.damager.DamagerDifficulty.crapDamagerEnabled
import net.axay.hglaborlobby.damager.DamagerDifficulty.getCrapDamagerItem
import net.axay.hglaborlobby.damager.DamagerDifficulty.inconsistencyEnabled
import net.axay.hglaborlobby.data.database.holder.WarpsHolder
import net.axay.hglaborlobby.data.database.location
import net.axay.hglaborlobby.functionality.LobbyItems
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.*
import net.axay.kspigot.extensions.geometry.LocationArea
import net.axay.kspigot.extensions.geometry.vec
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.task
import net.axay.kspigot.utils.addEffect
import net.axay.kspigot.utils.editMeta
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemFlag

val world = Bukkit.getWorld("hub")

val locationArea = LocationArea(vec(-27.5, 100.5, 130.5).toLocation(world!!), vec(-44.5, 110.0, 155.5).toLocation(world))

//TODO config zeu

val Player.isInWaterfightRegion: Boolean
    get() = locationArea.isInArea(this.location)


object Waterfight {

    var playersInGame = mutableListOf<String>()

    fun enable() {
        listen<EntityDamageEvent> {
            if (it.entity !is Player) return@listen
            val p = it.entity as Player
            if (!p.isInWaterfightRegion) return@listen
            if (p.health - it.damage <= 0) {
                    LobbyItems.givePlayer(p)
                    WarpsHolder.instance.spawn?.let { warp ->
                        p.teleport(warp.location())
                    }
                    p.heal()
                    p.sendMessage("${KColors.GRAY}Du wurdest getötet!")
                    p.playSound(p.location, Sound.ENTITY_ELDER_GUARDIAN_DEATH, 1.0F, 1.0F)
                it.isCancelled = true
            }
        }

        task(
            sync = false,
            period = 12L
        ) {
            checkPlayerPositions()
        }
    }


    private fun checkPlayerPositions() {
        for (player in onlinePlayers) {
            if (locationArea.isInArea(player.location)) {
                if (player.name !in playersInGame) {
                    sync { giveItems(player) }
                    playersInGame.plusAssign(player.name)
                }
            } else {
                if (player.name in playersInGame && !player.isInWater && !player.isInWaterfightRegion) {
                    LobbyItems.givePlayer(player)
                    player.heal()
                    player.feedSaturate()
                    playersInGame.minusAssign(player.name) // remove player from damager
                    break
                }
            }
        }

    }

    private fun giveItems(player: Player) {
        player.health = 20.0
        player.feed()
        player.saturation = 0F
        player.inventory.clear()
        player.inventory.addItem(itemStack(Material.TRIDENT) {
            meta {
                name = "${KColors.SKYBLUE}Dreizack"
                isUnbreakable = true
                addEnchant(Enchantment.LOYALTY, 1, true)
                addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
                addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier("generic.attackDamage", -4.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1))
            }
        })
    }
}