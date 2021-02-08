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
    private var serverName: String? = null
    private var serverPort = 0
    private var maxPlayers = 0
    private var onlinePlayers = 0
    private var timeInSeconds = 0
    private var gameState: String? = null
    var item: ItemStack? = null

    fun getServerName(): String? {
        return serverName
    }

    fun setServerName(serverName: String?) {
        this.serverName = serverName
    }

    fun getServerPort(): Int {
        return serverPort
    }

    fun setServerPort(serverPort: Int) {
        this.serverPort = serverPort
    }

    fun getGameState(): String? {
        return gameState
    }

    fun setGameState(gameState: String?) {
        this.gameState = gameState
    }

    fun getMaxPlayers(): Int {
        return maxPlayers
    }

    fun setMaxPlayers(maxPlayers: Int) {
        this.maxPlayers = maxPlayers
    }

    fun getOnlinePlayers(): Int {
        return onlinePlayers
    }

    fun setOnlinePlayers(onlinePlayers: Int) {
        this.onlinePlayers = onlinePlayers
    }

    fun getTimeInSeconds(): Int {
        return timeInSeconds
    }

    fun setTimeInSeconds(timeInSeconds: Int) {
        this.timeInSeconds = timeInSeconds
    }

    fun gameState() = GameState.valueOf(getGameState()!!)

    fun formattedTime(): String {
        val min: Int = timeInSeconds / 60
        val sek = timeInSeconds % 60
        return String.format("${gameState().color}%02d${KColors.DARKGRAY}:${gameState().color}%02d", min, sek)
    }
}

fun HGInfo.getNewItem() = itemStack(gameState().material) {
    amount = 1
    meta {
        name = "${KColors.CORNSILK}${getServerName()}"
        addLore {
            +"${KColors.GRAY}State ${KColors.DARKGRAY}» ${gameState().name()}"
            +"${KColors.GRAY}Players ${KColors.DARKGRAY}» ${KColors.DEEPSKYBLUE}${getOnlinePlayers()}"
            if (gameState() == GameState.LOBBY) +"${KColors.GRAY}Starting in ${KColors.DARKGRAY}» ${formattedTime()}"
            else +"${KColors.GRAY}Time ${KColors.DARKGRAY}» ${formattedTime()}"

            +"${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                      "
            +"${KColors.LIGHTSKYBLUE}Left ${KColors.GRAY}click to join ${KColors.UNDERLINE}server"
            +"${KColors.MEDIUMPURPLE}Right ${KColors.GRAY}click to join ${KColors.UNDERLINE}queue"
        }
    }
}
