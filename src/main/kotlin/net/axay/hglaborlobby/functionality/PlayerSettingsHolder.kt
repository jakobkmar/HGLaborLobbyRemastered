@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.hglaborlobby.functionality

import net.axay.blueutils.database.mongodb.insertOneIfNotContains
import net.axay.hglaborlobby.database.DatabaseManager
import net.axay.hglaborlobby.database.data.PlayerSettings
import net.axay.kspigot.event.listen
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerQuitEvent
import org.litote.kmongo.eq
import org.litote.kmongo.findOne

object PlayerSettingsHolder {

    private val playerSettings = mutableMapOf<Player, PlayerSettings>()

    fun loadFromDatabase(player: Player)
        = (
            DatabaseManager.playerSettings.findOne(PlayerSettings::uuid eq player.uniqueId)
                ?: PlayerSettings(player.uniqueId).apply {
                    DatabaseManager.playerSettings.insertOneIfNotContains(
                        PlayerSettings::uuid eq player.uniqueId,
                        this@apply
                    )
                }
        ).apply { playerSettings[player] = this@apply }

    operator fun get(player: Player, loadIfNotPresent: Boolean = true)
        = playerSettings[player] ?:
            if (loadIfNotPresent)
                loadFromDatabase(player)
            else null

    fun enable() {

        listen<PlayerQuitEvent>(EventPriority.HIGHEST) {
            playerSettings -= it.player
        }

    }

}