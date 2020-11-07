package net.axay.hglaborlobby.gui.guis

import net.axay.hglaborlobby.data.database.PlayerSettings
import net.axay.hglaborlobby.database.DatabaseManager
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.toLoreList
import org.bukkit.Material
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.projection
import org.litote.kmongo.setValue
import kotlin.reflect.KProperty

object PrivacySettingsGUI {

    private val privacyGUI = kSpigotGUI(GUIType.THREE_BY_NINE) {

        title = "${KColors.RED}PRIVACY SETTINGS"

        page(1) {

            placeholder(Slots.All, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
            placeholder(
                Slots.RowOneSlotTwo rectTo Slots.RowThreeSlotEight,
                itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { name = null } }
            )

            button(Slots.RowTwoSlotTwo, itemStack(Material.GREEN_GLAZED_TERRACOTTA) {
                meta {
                    name = "${KColors.SPRINGGREEN}CITY"
                    lore = "Lege fest, ob beim Betreten des Servers deine ${KColors.WHITESMOKE}Stadt ${KColors.CORNSILK}angezeigt werden soll."
                        .toLoreList(KColors.CORNSILK)
                }
            }) {
                it.tooglePrivacySetting(
                    PlayerSettings::privacySettings / PlayerSettings.PlayerPrivacySettings::ifCity)
            }

            button(Slots.RowTwoSlotFour, itemStack(Material.PURPLE_GLAZED_TERRACOTTA) {
                meta {
                    name = "${KColors.BLUEVIOLET}STATE"
                    lore = "Lege fest, ob beim Betreten des Servers dein ${KColors.WHITESMOKE}Bundesland ${KColors.CORNSILK}angezeigt werden soll."
                        .toLoreList(KColors.CORNSILK)
                }
            }) {
                it.tooglePrivacySetting(
                    PlayerSettings::privacySettings / PlayerSettings.PlayerPrivacySettings::ifState)
            }

            button(Slots.RowTwoSlotSix, itemStack(Material.RED_GLAZED_TERRACOTTA) {
                meta {
                    name = "${KColors.MAROON}COUNTRY"
                    lore = "Lege fest, ob beim Betreten des Servers dein ${KColors.WHITESMOKE}Land ${KColors.CORNSILK}angezeigt werden soll."
                        .toLoreList(KColors.CORNSILK)
                }
            }) {
                it.tooglePrivacySetting(
                    PlayerSettings::privacySettings / PlayerSettings.PlayerPrivacySettings::ifCountry)
            }

            button(Slots.RowTwoSlotEight, itemStack(Material.YELLOW_GLAZED_TERRACOTTA) {
                meta {
                    name = "${KColors.MAROON}CONTINENT"
                    lore = "Lege fest, ob beim Betreten des Servers dein ${KColors.WHITESMOKE}Kontinent ${KColors.CORNSILK}angezeigt werden soll."
                        .toLoreList(KColors.CORNSILK)
                }
            }) {
                it.tooglePrivacySetting(
                    PlayerSettings::privacySettings / PlayerSettings.PlayerPrivacySettings::ifContinent)
            }

        }

    }

    private fun GUIClickEvent<*>.tooglePrivacySetting(property: KProperty<*>) {

        @Suppress("UNCHECKED_CAST")
        property as KProperty<Boolean>

        val currentValue =
            DatabaseManager.playerSettings.projection(property, PlayerSettings::uuid eq player.uniqueId).first()
                ?: false
        DatabaseManager.playerSettings.updateOne(PlayerSettings::uuid eq player.uniqueId, setValue(property, !currentValue))

    }

    fun enable() {

        MainGUI.addContent(MainGUI.MainGUICompoundElement(
            Material.BEACON,
            "Privacy Settings",
            "Stelle ein, was angezeigt wird, wenn du den Server betrittst.",
            onClick = { it.player.openGUI(privacyGUI) }
        ))

    }

}