package net.axay.hglaborlobby.eventmanager.joinserver

import net.axay.kspigot.chat.KColors
import org.bukkit.entity.Player

object JoinTablist {

    private val header = StringBuilder().apply {
        appendLine(" ")
        appendLine("   ${KColors.BOLD}${KColors.SPRINGGREEN}hg${KColors.AQUAMARINE}labor${KColors.WHITE}.${KColors.LIGHTGRAY}de   ")
    }.toString()

    private val footer = StringBuilder().apply {
        appendLine(" ")
        appendLine("${KColors.GREENYELLOW}${KColors.BOLD}#1 ${KColors.RESET}${KColors.INDIANRED}Minecraft Server")
    }.toString()

    fun setTablist(player: Player) {
        player.setPlayerListHeaderFooter(header, footer)
    }

}