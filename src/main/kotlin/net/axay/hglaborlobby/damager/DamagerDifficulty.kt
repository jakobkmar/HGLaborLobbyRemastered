package net.axay.hglaborlobby.damager

import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object DamagerDifficulty {

    private val damageGUI = kSpigotGUI(GUIType.ONE_BY_NINE) {
        title = "§6Damager Difficulty"

        page(0) {
            placeholder(Slots.RowOneSlotOne, ItemStack(Material.AIR))
            button(Slots.RowOneSlotTwo, itemStack(Material.WHITE_DYE) {
                meta { name = "${org.bukkit.ChatColor.WHITE}1.5 Herzen" }
            }) {
                Damager.playerDamage[it.player.name] = 3.0
                it.player.sendMessage("§6Dein Damager Schaden ist nun §2${Damager.playerDamage[it.player.name]}")
            }
            button(Slots.RowOneSlotThree, itemStack(Material.GREEN_DYE) {
                meta { name = "${org.bukkit.ChatColor.DARK_GREEN}2 Herzen" }
            }) {
                Damager.playerDamage[it.player.name] = 4.0
                it.player.sendMessage("§6Dein Damager Schaden ist nun §2${Damager.playerDamage[it.player.name]}")
            }
            button(Slots.RowOneSlotFour, itemStack(Material.LIME_DYE) {
                meta { name = "${org.bukkit.ChatColor.GREEN}2.5 Herzen" }
            }) {
                Damager.playerDamage[it.player.name] = 5.0
                it.player.sendMessage("§6Dein Damager Schaden ist nun §2${Damager.playerDamage[it.player.name]}")
            }
            button(Slots.RowOneSlotFive, itemStack(Material.YELLOW_DYE) {
                meta { name = "${org.bukkit.ChatColor.YELLOW}3 Herzen" }
            }) {
                Damager.playerDamage[it.player.name] = 6.0
                it.player.sendMessage("§6Dein Damager Schaden ist nun §2${Damager.playerDamage[it.player.name]}")
            }
            button(Slots.RowOneSlotSix, itemStack(Material.ORANGE_DYE) {
                meta { name = "${org.bukkit.ChatColor.GOLD}3.5 Herzen" }
            }) {
                Damager.playerDamage[it.player.name] = 7.0
                it.player.sendMessage("§6Dein Damager Schaden ist nun §2${Damager.playerDamage[it.player.name]}")
            }
            button(Slots.RowOneSlotSeven, itemStack(Material.RED_DYE) {
                meta { name = "${org.bukkit.ChatColor.RED}4 Herzen" }
            }) {
                Damager.playerDamage[it.player.name] = 8.0
                it.player.sendMessage("§6Dein Damager Schaden ist nun §2${Damager.playerDamage[it.player.name]}")
            }
            button(Slots.RowOneSlotEight, itemStack(Material.PURPLE_DYE) {
                meta { name = "${org.bukkit.ChatColor.DARK_PURPLE}4.5 Herzen" }
            }) {
                Damager.playerDamage[it.player.name] = 9.0
                it.player.sendMessage("§6Dein Damager Schaden ist nun §2${Damager.playerDamage[it.player.name]}")
            }
            placeholder(Slots.RowOneSlotNine, ItemStack(Material.AIR))
        }
    }


    fun enable() {
        listen<PlayerInteractEvent>(EventPriority.MONITOR) {
            if (it.clickedBlock?.type != Material.OAK_WALL_SIGN) return@listen
            val sign = it.clickedBlock!!.state as Sign
            if (sign.lines[1] != "Damager") return@listen
            it.player.openGUI(damageGUI, 0)
        }
    }

}