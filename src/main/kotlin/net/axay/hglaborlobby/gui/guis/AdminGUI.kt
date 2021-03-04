package net.axay.hglaborlobby.gui.guis

import com.google.common.base.Enums
import net.axay.hglaborlobby.data.database.ServerWarp
import net.axay.hglaborlobby.data.database.Warp
import net.axay.hglaborlobby.database.DatabaseManager
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.input.awaitAnvilInput
import net.axay.kspigot.chat.input.awaitChatInput
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.toLoreList
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.litote.kmongo.save

object AdminGUI : CommandExecutor {

    private val gui = kSpigotGUI(GUIType.SIX_BY_NINE) {

        title = "${KColors.FIREBRICK}ADMIN GUI"

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            button(Slots.RowFiveSlotTwo, itemStack(Material.END_ROD) {
                meta {
                    name = "${KColors.SPRINGGREEN}Warp erstellen"
                    lore = "Erstelle einen neuen Warp an deiner Position.".toLoreList(KColors.DARKSEAGREEN)
                }
            }) { clickEvent ->

                (clickEvent.bukkitEvent.whoClicked as? Player)?.let { player ->

                    player.awaitAnvilInput("Gib den Namen des Warps ein!") { name ->

                        val warpName = name.input ?: kotlin.run {
                            player.sendMessage("${KColors.RED}ABBRUCH. ${KColors.INDIANRED}Du musst einen validen Namen für den Warp eingeben!")
                            return@awaitAnvilInput
                        }

                        player.sendMessage("${KColors.BISQUE}Du hast ${KColors.YELLOWGREEN}$warpName ${KColors.BISQUE}als Warpname festgelegt.")

                        player.awaitChatInput("Gib die Beschreibung des Warps ein!") { description ->

                            val warpDescription = description.input ?: kotlin.run {
                                player.sendMessage("${KColors.RED}ABBRUCH. ${KColors.INDIANRED}Du musst eine valide Beschreibung für den Warp eingeben!")
                                return@awaitChatInput
                            }

                            val takeElements = 20
                            val dots = if (warpDescription.length > takeElements) "..." else ""
                            player.sendMessage(
                                "${KColors.BISQUE}Du hast ${KColors.YELLOWGREEN}${
                                    warpDescription.take(
                                        takeElements
                                    )
                                }$dots ${KColors.BISQUE}als Warpbeschreibung festgelegt."
                            )

                            player.awaitAnvilInput("Gib das Iconmaterial des Warps ein!") { material ->

                                val warpMaterial =
                                    Enums.getIfPresent(Material::class.java, material.input?.toUpperCase() ?: "")
                                        .orNull() ?: kotlin.run {
                                        player.sendMessage("${KColors.RED}ABBRUCH. ${KColors.INDIANRED}Du musst ein valides Iconmaterial für den Warp eingeben!")
                                        @Suppress("LABEL_NAME_CLASH")
                                        return@awaitAnvilInput
                                    }

                                player.sendMessage("${KColors.BISQUE}Du hast ${KColors.YELLOWGREEN}$warpMaterial ${KColors.BISQUE}als Iconmaterial des Warps festgelegt.")

                                // save the warp
                                DatabaseManager.warps.save(
                                    Warp(
                                        warpName,
                                        player.location,
                                        warpDescription,
                                        warpMaterial
                                    )
                                )

                                player.sendMessage("${KColors.GRAY}Du hast den Warp ${KColors.LIGHTGRAY}$warpName ${KColors.SPRINGGREEN}erfolgreich ${KColors.GRAY}erstellt.")

                            }

                        }
                    }
                }
            }

            button(Slots.RowFiveSlotThree, itemStack(Material.POPPED_CHORUS_FRUIT) {
                meta {
                    name = "${KColors.SPRINGGREEN}Server Warp erstellen"
                    lore = "Erstelle einen neuen Server Warp.".toLoreList(KColors.DARKSEAGREEN)
                }
            }) { clickEvent ->

                (clickEvent.bukkitEvent.whoClicked as? Player)?.let { player ->

                    player.awaitAnvilInput("Gib den Namen des Serverwarps ein!") { name ->

                        val serverWarpName = name.input ?: kotlin.run {
                            player.sendMessage("${KColors.RED}ABBRUCH. ${KColors.INDIANRED}Du musst einen validen Namen für den Warp eingeben!")
                            return@awaitAnvilInput
                        }

                        player.sendMessage("${KColors.BISQUE}Du hast ${KColors.YELLOWGREEN}$serverWarpName ${KColors.BISQUE}als Serverwarpname festgelegt.")

                        player.awaitAnvilInput("Gib den Servernamen ein!") { servername ->

                            val serverName = servername.input ?: kotlin.run {
                                player.sendMessage("${KColors.RED}ABBRUCH. ${KColors.INDIANRED}Du musst eine valide Beschreibung für den Warp eingeben!")
                                return@awaitAnvilInput
                            }

                            player.sendMessage(
                                "${KColors.BISQUE}Du hast ${KColors.YELLOWGREEN}$serverName ${KColors.BISQUE}als Servernamen festgelegt.")

                            player.awaitAnvilInput("Gib das Iconmaterial des Serverwarps ein!") { material ->

                                val serverWarpMaterial =
                                    Enums.getIfPresent(Material::class.java, material.input?.toUpperCase() ?: "")
                                        .orNull() ?: kotlin.run {
                                        player.sendMessage("${KColors.RED}ABBRUCH. ${KColors.INDIANRED}Du musst ein valides Iconmaterial für den Warp eingeben!")
                                        @Suppress("LABEL_NAME_CLASH")
                                        return@awaitAnvilInput
                                    }

                                player.sendMessage("${KColors.BISQUE}Du hast ${KColors.YELLOWGREEN}$serverWarpMaterial ${KColors.BISQUE}als Iconmaterial des Serverwarps festgelegt.")

                                // save the serverwarp
                                DatabaseManager.serverWarps.save(
                                    ServerWarp(serverWarpName, serverName, serverWarpMaterial))

                                player.sendMessage("${KColors.GRAY}Du hast den Warp ${KColors.LIGHTGRAY}$serverName ${KColors.SPRINGGREEN}erfolgreich ${KColors.GRAY}erstellt.")

                            }

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