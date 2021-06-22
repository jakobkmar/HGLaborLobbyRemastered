package net.axay.hglaborlobby.security

import net.axay.kspigot.chat.KColors
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object VPNCommand : CommandExecutor {

    var isActive = true

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender.hasPermission("hglabor.togglevpn")) {
            isActive = !isActive
            sender.sendMessage("${KColors.CORNSILK}VPN Protection: ${KColors.SANDYBROWN}${isActive}")
        } else {
            sender.sendMessage("${KColors.TOMATO}Du hast keine Rechte, um die VPN Protection einzuestellen.")
        }
        return false
    }
}