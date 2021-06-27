package net.axay.hglaborlobby.hg

import net.axay.hglaborlobby.gui.guis.PrivateHGServerGUI
import net.axay.kspigot.gui.openGUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object HostCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender.hasPermission("group.contentcreator")) {
            if (sender is Player) {
                sender.openGUI(PrivateHGServerGUI.createMainGUI(sender), 0)
            }
        }
        return true
    }
}