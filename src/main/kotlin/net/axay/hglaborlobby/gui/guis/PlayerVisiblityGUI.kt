package net.axay.hglaborlobby.gui.guis

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.hideOnlinePlayers
import net.axay.kspigot.extensions.bukkit.showOnlinePlayers
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.toLoreList
import org.bukkit.Material

object PlayerVisiblityGUI {

    private val settingsGUI = kSpigotGUI(GUIType.THREE_BY_NINE) {

        title = "${KColors.RED}VISIBILITY SETTINGS"

        page(1) {

            placeholder(Slots.All, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(
                Slots.RowOneSlotTwo rectTo Slots.RowThreeSlotEight,
                itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = null } }
            )

            button(Slots.RowTwoSlotThree, itemStack(Material.GREEN_GLAZED_TERRACOTTA) {
                meta {
                    name = "${KColors.SPRINGGREEN}SHOW ALL PLAYERS"
                    lore = "Zeige alle Spieler, egal ob sie deine Freunde sind oder nicht."
                        .toLoreList(KColors.CORNSILK)
                }
            }) {

                it.player.closeInventory()

                it.player.showOnlinePlayers()
                it.player.sendMessage("${KColors.GRAY}Die Spielersichtbarkeit wurde auf ${KColors.SPRINGGREEN}SHOW ALL PLAYERS ${KColors.GRAY}gesetzt.")

            }

            button(Slots.RowTwoSlotFive, itemStack(Material.PURPLE_GLAZED_TERRACOTTA) {
                meta {
                    name = "${KColors.BLUEVIOLET}SHOW FRIENDS"
                    lore = "Zeige nur Spieler, die auch deine Freunde sind."
                        .toLoreList(KColors.CORNSILK)
                }
            }) {

                it.player.closeInventory()

                it.player.hideOnlinePlayers()
                it.player.sendMessage("${KColors.GRAY}Die Spielersichtbarkeit wurde auf ${KColors.BLUEVIOLET}SHOW FRIENDS ${KColors.GRAY}gesetzt. ${KColors.LIGHTGRAY}(es gibt aktuell noch kein Freundesystem)")

            }

            button(Slots.RowTwoSlotSeven, itemStack(Material.RED_GLAZED_TERRACOTTA) {
                meta {
                    name = "${KColors.MAROON}HIDE ALL PLAYERS"
                    lore = "Verstecke alle Spieler."
                        .toLoreList(KColors.CORNSILK)
                }
            }) {

                it.player.closeInventory()

                it.player.hideOnlinePlayers()
                it.player.sendMessage("${KColors.GRAY}Die Spielersichtbarkeit wurde auf ${KColors.MAROON}HIDE ALL PLAYERS ${KColors.GRAY}gesetzt.")

            }

        }

    }

    fun enable() {

        MainGUI.addContent(MainGUI.MainGUICompoundElement(
            Material.REDSTONE_LAMP,
            "Visiblity Settings",
            "Stelle ein, wen du alles sehen kannst.",
            onClick = { it.player.openGUI(settingsGUI) }
        ))

    }

}