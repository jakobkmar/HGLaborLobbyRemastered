package net.axay.hglaborlobby.damager

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

object DamageCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) return false
        val damage = args[0].toIntOrNull() ?: return false

        if (damage > 20) return false

        Damager.playerDamage[sender.name] = damage.toDouble()
        sender.sendMessage("§3Dein Damager Schaden ist nun §5${Damager.playerDamage[sender.name]}")

        return true
    }
}