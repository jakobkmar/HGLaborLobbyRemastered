package net.axay.hglaborlobby.gui

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.input.awaitAnvilInput
import net.axay.kspigot.inventory.InventoryType
import net.axay.kspigot.inventory.Slots
import net.axay.kspigot.inventory.kSpigotGUI
import net.axay.kspigot.inventory.openGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object AdminGUI : CommandExecutor {

    private val gui = kSpigotGUI(InventoryType.SIX_BY_NINE) {

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS) { meta { name = " " } })

            button(Slots.RowFiveSlotTwo, itemStack(Material.END_ROD) {
                meta {
                    name = "${KColors.SPRINGGREEN}Warp erstellen"
                    setLore {
                        +"${KColors.DARKSEAGREEN}Erstelle einen neuen Warp"
                        +"${KColors.DARKSEAGREEN}an deiner Position."
                    }
                }
            }) { clickEvent ->
                (clickEvent.bukkitEvent.whoClicked as? Player)?.let { player ->
                    player.awaitAnvilInput(
                        invTitle = "Gib den Namen des Warps ein!"
                    ) {
                        val warpName = it ?: kotlin.run {
                            player.sendMessage("${KColors.RED}ABBRUCH. ${KColors.INDIANRED}Du musst einen validen Namen für den Warp eingeben!")
                            return@awaitAnvilInput
                        }
                        player.sendMessage("${KColors.BISQUE}Du hast ${KColors.YELLOWGREEN}$warpName ${KColors.BISQUE}als Warpname festgelegt.")
                        player.awaitAnvilInput( // TODO book input
                            invTitle = "Gib "
                        ) {

                        }
                    }
                }
            }

        }

    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {

        if (sender.hasPermission("lobby.admingui"))
            if (sender is Player)
                sender.openGUI(gui)

        return true

    }

}