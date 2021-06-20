package net.axay.hglaborlobby.functionality

import de.hglabor.utils.noriskutils.HologramUtils
import kotlinx.coroutines.launch
import net.axay.hglaborlobby.data.database.HGStats
import net.axay.hglaborlobby.data.database.PlayerNameCache
import net.axay.hglaborlobby.database.DatabaseManager
import net.axay.hglaborlobby.database.mongoScope
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.info
import net.axay.kspigot.extensions.console
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.runnables.sync
import net.axay.kspigot.runnables.task
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.meta.SkullMeta
import org.litote.kmongo.*

object StatsDisplay {

    val hologramList = arrayListOf<ArmorStand>()

    fun enable() {
        task(
            delay = 20,
            period = 5*60*20
        ) {
            updateDisplay(Location(Bukkit.getWorld("hub"), 124.5, 106.5, 15.5))
        }
    }

    private fun spawnPlayerHead(location: Location, playerName: String, isMini: Boolean = true): ArmorStand? {
        val newLocation = location.clone()
        newLocation.yaw = 90.0f
        newLocation.pitch = 0.0f
        val armorStand = location.world?.spawn(newLocation, ArmorStand::class.java)
        armorStand!!.isVisible = false
        armorStand.equipment?.setItemInMainHand(itemStack(Material.PLAYER_HEAD) {
            meta<SkullMeta> {
                owner = playerName
            }
        })
        armorStand.isSmall = isMini
        armorStand.setGravity(false)
        return armorStand
    }

    private fun updateDisplay(highestLocation: Location) {
        HologramUtils.spawnHologram(highestLocation.clone().add(0.0, 0.3,0.0), "${KColors.SLATEBLUE}HG Top 10 ${KColors.DARKGRAY}- ${KColors.MEDIUMVIOLETRED}Kills")
        mongoScope.launch {
            var location = highestLocation.clone()
            for (hologram in hologramList) {
                hologram.remove()
            }
            hologramList.clear()
            val data = DatabaseManager.hgStats.aggregate<HGStats>(
                sort(descending(HGStats::kills)),
                limit(10)
            ).toList()
            console.info(data.toString())
            for(hgStats in data) {
                val playerName = DatabaseManager.playerNameCache.findOne(PlayerNameCache::uuid eq hgStats.uuid)?.name
                playerName?.let {
                    val rankingPos = data.indexOf(hgStats)+1
                    sync {
                        val playerHead = spawnPlayerHead(location.clone().add(0.0, 0.08, 1.6)/*.clone().add(0.0, -0.7, 1.0)*/, it)
                        val hologram = HologramUtils.spawnHologram(
                            location,
                            "${getColor(rankingPos)}${KColors.UNDERLINE}#$rankingPos.${KColors.RESET} ${KColors.GRAY}$it ${KColors.DARKGRAY}- ${KColors.LIGHTGOLDENRODYELLOW}${hgStats.kills}"
                        )
                        hologramList.add(hologram)
                        if (playerHead != null) {
                            hologramList.add(playerHead)
                        }
                        location = highestLocation.subtract(0.0, 0.3, 0.0)
                    }
                }
            }
        }
    }

    private fun getColor(rank: Int): ChatColor {
        return if(rank == 1) {
            KColors.SANDYBROWN
        } else if(rank == 2) {
            KColors.SILVER
        } else if(rank == 3) {
            KColors.ROSYBROWN
        } else {
            KColors.DARKGRAY
        }
    }

}