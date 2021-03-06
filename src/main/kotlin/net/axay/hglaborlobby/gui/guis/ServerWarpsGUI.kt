package net.axay.hglaborlobby.gui.guis

import net.axay.hglaborlobby.data.database.ServerWarp
import net.axay.hglaborlobby.data.database.holder.ServerWarpsHolder
import net.axay.hglaborlobby.data.database.itemStack
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.pluginmessages.PluginMessageConnect
import net.axay.kspigot.runnables.*
import net.axay.kspigot.utils.hasMark
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object ServerWarpsGUI {

    private var menuCompound: GUIRectSpaceCompound<ForInventoryThreeByNine, GUICompoundElement<ForInventoryThreeByNine>>? =
        null

    private val serverWarpsGUI = kSpigotGUI(GUIType.THREE_BY_NINE, SharedGUICreator()) {

        title = "${KColors.BLACK}SERVER WARPS"

        page(1) {
            placeholder(Slots.Border, ItemStack(Material.WHITE_STAINED_GLASS_PANE))
            menuCompound = createSimpleRectCompound(Slots.RowTwoSlotTwo, Slots.RowTwoSlotEight)
        }
    }

    class ServerWarpGUICompoundElement(itemStack: ItemStack, serverWarp: ServerWarp) :
        GUICompoundElement<ForInventoryThreeByNine>(itemStack, onClick = listen@{
            it.bukkitEvent.isCancelled = true
            PluginMessageConnect(serverWarp.serverName).sendWithPlayer(it.player)
        })

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
        updateGUI()

        task(false, 0, 8 * 20, null) {
            ServerWarp.updatePlayerCounts()

            taskRunLater(2, false) {
                updateGUI()
            }
        }
    }

    private fun updateGUI() {
        val compoundElements = mutableListOf<GUICompoundElement<ForInventoryThreeByNine>>()
        ServerWarpsHolder.instance.serverWarps.forEach {
            compoundElements.add(ServerWarpGUICompoundElement(it.itemStack, it))
        }
        menuCompound?.setContent(compoundElements)
    }
}