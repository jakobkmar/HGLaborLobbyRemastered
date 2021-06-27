package net.axay.hglaborlobby.gui.guis

import de.dytanic.cloudnet.common.document.gson.JsonDocument
import de.dytanic.cloudnet.driver.CloudNetDriver
import de.dytanic.cloudnet.driver.service.ProcessConfiguration
import de.dytanic.cloudnet.driver.service.ServiceEnvironmentType
import de.dytanic.cloudnet.driver.service.ServiceTemplate
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.entity.Player

object PrivateHGServerGUI {
    val DRIVER = CloudNetDriver.getInstance()

    fun createMainGUI(player: Player): GUI<ForInventoryThreeByNine> {
        return kSpigotGUI(GUIType.THREE_BY_NINE) {
            title = "HG RUNDE ERSTELLEN ;)"
            page(0) {
                placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

                button(Slots.RowTwoSlotFive, itemStack(Material.LIME_DYE) { meta { name = "starten!" }}) {
                    val privateRounds = DRIVER.cloudServiceProvider.getCloudServices("PrivateHG").size
                    if (!player.hasPermission("group.media")) {
                        if (privateRounds >= 1) {
                            player.sendMessage("${KColors.RED}es sind schon $privateRounds private runden an und als CC geht das nicht L")
                            return@button
                        }
                    }
                    val serviceInfoSnapshot  = DRIVER.cloudServiceFactory.createCloudService(
                        "PrivateHG",
                        "jvm",
                        true,
                        false,
                        arrayListOf(),
                        listOf(
                            ServiceTemplate(
                                "HG",
                                "private",
                                "local"
                            ),
                        ),
                        arrayListOf(),
                        arrayListOf(),
                        ProcessConfiguration(
                            ServiceEnvironmentType.MINECRAFT_SERVER,
                            2048,
                            arrayListOf()
                        ),
                        JsonDocument.newDocument().append("host", player.uniqueId), // PROPERTIES (host uuid)
                        null
                    )

                    serviceInfoSnapshot?.provider()?.start()
                    player.sendMessage("${KColors.LIMEGREEN}Der Server ${serviceInfoSnapshot?.name} wurde gestartet\nSobald dieser gestartet ist wirst du automatisch auf den Server geschoben")

                    player.closeInventory()
                }
            }
        }
    }
}