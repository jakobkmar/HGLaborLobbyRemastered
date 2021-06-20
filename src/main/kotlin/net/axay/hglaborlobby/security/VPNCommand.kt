package net.axay.hglaborlobby.security

import net.axay.kspigot.chat.KColors
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object VPNCommand : CommandExecutor {

    var isActive = true

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if(p0.hasPermission("hglabor.togglevpn")) {
            isActive = !isActive
            p0.sendMessage("${KColors.CORNSILK}VPN Protection: ${KColors.SANDYBROWN}${isActive}")
        } else {
            p0.sendMessage("${KColors.TOMATO}Du hast keine Rechte, um die VPN Protection einzuestellen.")
        }
        return false
    }
}