package net.axay.hglaborlobby.gui.guis

import net.axay.hglaborlobby.data.database.ServerWarp
import net.axay.hglaborlobby.data.database.holder.ServerWarpsHolder
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setMeta
import net.axay.kspigot.pluginmessages.PluginMessageConnect
import net.axay.kspigot.pluginmessages.PluginMessagePlayerCount
import net.axay.kspigot.pluginmessages.sendPluginMessageToBungeeCord
import net.axay.kspigot.runnables.firstAsync
import net.axay.kspigot.runnables.thenSync
import net.axay.kspigot.utils.hasMark
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object ServerWarpsGUI {

    private val serverWarpsGUI = kSpigotGUI(GUIType.THREE_BY_NINE, SharedGUICreator()) {

        title = "${KColors.BLACK}SERVER WARPS"

        page(1) {

            placeholder(Slots.Border, ItemStack(Material.WHITE_STAINED_GLASS_PANE))

            val serverWarpsCompound = createRectCompound<ServerWarp>(Slots.RowTwoSlotTwo, Slots.RowTwoSlotEight, {
                itemStack(it.icon) {
                    setMeta {
                        name = "${KColors.CORAL}${it.name.capitalize()}"
                        lore = arrayListOf<String>().apply {
                            this += "${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                      "
                            PluginMessagePlayerCount(it.serverName) { playerCount ->
                                this += "${KColors.GRAY}Spieler ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}$playerCount"
                            }
                        }
                    }
                }
            },
                onClick = { clickEvent, element ->
                    clickEvent.bukkitEvent.isCancelled = true
                    PluginMessageConnect(element.serverName).sendWithPlayer(clickEvent.player)
                }
            )

            firstAsync {
                ServerWarpsHolder.instance.serverWarps
            }.thenSync {
                serverWarpsCompound.addContent(it)
            }.execute()

        }

    }

    fun enable() {

        MainGUI.addContent(MainGUI.MainGUICompoundElement(
            Material.NETHER_STAR,
            "Server Warps",
            "Sehe alle Server Warps, die existieren.",
            onClick = { it.player.openGUI(serverWarpsGUI) }
        ))

        listen<PlayerInteractEvent> {
            if (!it.action.isRightClick) return@listen
            if (it.item?.hasMark("serverwarps") == true) {
                it.isCancelled = true
                it.player.openGUI(serverWarpsGUI)
            }
        }
    }
}