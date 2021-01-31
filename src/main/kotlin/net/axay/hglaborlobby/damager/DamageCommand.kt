package net.axay.hglaborlobby.damager

import net.axay.hglaborlobby.main.LOBBY_PREFIX
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object DamageCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) return false
        val damage = args[0].toIntOrNull() ?: return false
        if (damage > 19 || damage < 1) return false
        if ((sender as Player).isInDamager) return false

        Damager.playerDamage[sender.name] = damage.toDouble()
        sender.sendMessage("$LOBBY_PREFIX Der Damager Schaden beträgt nun ${Damager.playerDamage[sender.name]}")

        return true
    }
}