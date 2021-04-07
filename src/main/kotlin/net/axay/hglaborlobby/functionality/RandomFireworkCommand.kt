package net.axay.hglaborlobby.functionality

import net.axay.kspigot.utils.addEffect
import net.axay.kspigot.utils.fireworkItemStack
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random.Default.nextBoolean

object RandomFireworkCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player || !sender.hasPermission("lobby.randomfirework"))
            return true

        val firework = fireworkItemStack {
            addEffect {
                flicker(nextBoolean())
                trail(nextBoolean())
                repeat((1..3).random()) {
                    withColor(Color.fromRGB((0x000000..0xFFFFFF).random()))
                }
                repeat((0..2).random()) {
                    withFade(Color.fromRGB((0x000000..0xFFFFFF).random()))
                }
                with(FireworkEffect.Type.values().random())
            }
            power = (1..3).random()
        }

        sender.inventory.addItem(firework)

        return true
    }
}
