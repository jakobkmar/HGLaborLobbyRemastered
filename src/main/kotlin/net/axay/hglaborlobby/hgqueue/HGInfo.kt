package net.axay.hglaborlobby.hgqueue

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.inventory.ItemStack

class HGInfo {

    companion object {
        var infos = hashMapOf<String, HGInfo>()
    }

    var serverName: String? = null
    var serverPort = 0
    var maxPlayers = 0
    var onlinePlayers = 0
    var timeInSeconds = 0
    var gameState: String? = null

    var item: ItemStack? = null

    fun gameState() = GameState.valueOf(gameState!!)

    fun formattedTime(): String {
        val min: Int = timeInSeconds / 60
        val sek = timeInSeconds % 60
        return String.format("${gameState().color}%02d${KColors.DARKGRAY}:${gameState().color}%02d", min, sek)
    }
}

fun HGInfo.getNewItem() = itemStack(gameState().material) {
    amount = if (onlinePlayers > 1) onlinePlayers else 1
    meta {
        name = "${KColors.CORNSILK}${serverName}"
        addLore {
            +"${KColors.GRAY}State ${KColors.DARKGRAY}»${gameState().color} ${gameState().name()}"
            +"${KColors.GRAY}Players ${KColors.DARKGRAY}» ${gameState().color}${onlinePlayers}"
            if (gameState() == GameState.LOBBY) +"${KColors.GRAY}Starting in ${KColors.DARKGRAY}» ${formattedTime()}"
            else +"${KColors.GRAY}Time ${KColors.DARKGRAY}» ${formattedTime()}"
            +"${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                      "

            if (onlinePlayers < maxPlayers) {
                if (gameState() != GameState.INGAME && gameState() != GameState.END)
                    +"${KColors.LIGHTSKYBLUE}Left ${KColors.GRAY}click to join ${KColors.UNDERLINE}server"
                if (gameState() == GameState.END || gameState() == GameState.LOBBY)
                    +"${KColors.MEDIUMPURPLE}Right ${KColors.GRAY}click to join ${KColors.UNDERLINE}queue"
            }
        }
    }
}
