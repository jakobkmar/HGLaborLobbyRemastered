package net.axay.hglaborlobby.functionality

import net.axay.kspigot.utils.addEffect
import net.axay.kspigot.utils.fireworkItemStack
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.random.Random


object RandomFireworkCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        val player: Player = sender as Player
        if (!player.hasPermission("lobby.randomfirework")) return true

        val firework: ItemStack = fireworkItemStack {
            addEffect {
                if (Random.nextBoolean()) withFlicker()
                with(FireworkEffect.Type.values().random())
                withColor(Color.fromRGB(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)))
                withFade(Color.fromRGB(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)))
            }
        }

        player.inventory.addItem(firework)

        return true
    }
}