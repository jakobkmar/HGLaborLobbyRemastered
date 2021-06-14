package net.axay.hglaborlobby.gui.guis

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.console
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.toLoreList
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.node.types.MetaNode
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player

object PrefixGUI {

    private fun generateGui(player: Player): GUI<ForInventoryThreeByNine> {
        return kSpigotGUI(GUIType.THREE_BY_NINE) {

            title = "${KColors.BLACK}PREFIX SELECTOR"

            page(1) {
                placeholder(Slots.All, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
                placeholder(
                    Slots.RowOneSlotTwo rectTo Slots.RowThreeSlotEight,
                    itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = null } }
                )
                button(Slots.RowTwoSlotTwo, itemStack(Material.GREEN_GLAZED_TERRACOTTA) {
                    meta {
                        name = "${KColors.SPRINGGREEN}Creator"
                        lore =
                            "Setze deinen Prefix auf ${KColors.PALEGOLDENROD}Creator${KColors.CORNSILK}."
                                .toLoreList(KColors.CORNSILK)
                    }
                }) {
                    setPrefix(it.player, "Creator${if(player.hasPermission("group.contentcreator+")) "+" else ""}")
                }
                button(Slots.RowTwoSlotFour, itemStack(Material.LIME_GLAZED_TERRACOTTA) {
                    meta {
                        name = "${KColors.MEDIUMSPRINGGREEN}Content"
                        lore =
                            "Setze deinen Prefix auf ${KColors.PALEGOLDENROD}Content${KColors.CORNSILK}."
                                .toLoreList(KColors.CORNSILK)
                    }
                }) {
                    setPrefix(it.player, "Content${if(player.hasPermission("group.contentcreator+")) "+" else ""}")
                }
                button(Slots.RowTwoSlotSix, itemStack(Material.YELLOW_GLAZED_TERRACOTTA) {
                    meta {
                        name = "${KColors.PALEGOLDENROD}ContentCreator"
                        lore =
                            "Setze deinen Prefix auf ${KColors.PALEGOLDENROD}ContentCreator${KColors.CORNSILK}."
                                .toLoreList(KColors.CORNSILK)
                    }
                }) {
                    setPrefix(it.player, "ContentCreator${if(player.hasPermission("group.contentcreator+")) "+" else ""}")
                }
                button(Slots.RowTwoSlotEight, itemStack(Material.ORANGE_GLAZED_TERRACOTTA) {
                    meta {
                        name = "${KColors.ORANGE}CC"
                        lore =
                            "Setze deinen Prefix auf ${KColors.PALEGOLDENROD}CC${KColors.CORNSILK}."
                                .toLoreList(KColors.CORNSILK)
                    }
                }) {
                    setPrefix(it.player, "CC${if(player.hasPermission("group.contentcreator+")) "+" else ""}")
                }
            }
        }
    }

    fun enable() {
        MainGUI.addContent(MainGUI.MainGUICompoundElement(
            Material.NAME_TAG,
            "Prefix Settings",
            "Stelle ein, welchen Prefix du benutzen möchtest.",
            onClick = {
                if(it.player.hasPermission("group.contentcreator")) {
                    it.player.openGUI(generateGui(it.player))
                } else {
                    it.bukkitEvent.isCancelled = true
                    it.player.closeInventory()
                    it.player.sendMessage("${KColors.TOMATO}Du benötigst mindestens den ContentCreator Rang um deinen Prefix ändern zu können.")
                }
            }
        ))
    }

    private fun setPrefix(player: Player, prefix: String) {
        Bukkit.dispatchCommand(console, "lp user ${player.name} meta set custom_prefix $prefix")
        player.kickPlayer("${KColors.CORNSILK}Dein Prefix wurde zu ${KColors.SLATEBLUE}${prefix}${KColors.CORNSILK} geändert, bitte verbinde dich neu damit die Änderungen in kraft treten.")
    }

}