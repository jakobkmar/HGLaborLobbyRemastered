package net.axay.hglaborlobby.gui.guis

import com.google.gson.Gson
import net.axay.hglaborlobby.hgqueue.GameState
import net.axay.hglaborlobby.hgqueue.HGInfo
import net.axay.hglaborlobby.hgqueue.HGQueueInfo
import net.axay.hglaborlobby.hgqueue.HG_QUEUE
import net.axay.hglaborlobby.main.Manager
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.events.isRightClick
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.items.name
import net.axay.kspigot.utils.hasMark
import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

object HGQueueGUI {
    private var menuCompound: GUIRectSpaceCompound<ForInventoryThreeByNine, GUICompoundElement<ForInventoryThreeByNine>>? =
        null

    val gui = kSpigotGUI(GUIType.THREE_BY_NINE, SharedGUICreator()) {

        title = "${KColors.DODGERBLUE}HG QUEUE"

        page(1) {
            placeholder(Slots.RowOneSlotOne rectTo Slots.RowThreeSlotNine, ItemStack(Material.WHITE_STAINED_GLASS_PANE))
            menuCompound = createSimpleRectCompound(Slots.RowTwoSlotTwo, Slots.RowTwoSlotEight)
        }
    }

    class HGQueueGUICompoundElement(itemStack: ItemStack) :
        GUICompoundElement<ForInventoryThreeByNine>(itemStack, onClick = listen@{
            val event = it.bukkitEvent
            event.isCancelled = true
            if (event.whoClicked is Player) {
                val player = event.whoClicked as Player
                val name = ChatColor.stripColor(event.currentItem!!.itemMeta!!.name)
                val hgInfo = HGInfo.infos[name]

                if (hgInfo!!.gameState() == GameState.INVINCIBILITY) {
                    sendPlayer(hgInfo, player)
                } else if (hgInfo.gameState() == GameState.INGAME) {
                    sendPlayer(hgInfo, player)
                } else if (hgInfo.gameState() == GameState.LOBBY) {
                    if (event.isRightClick) {
                        player.closeInventory()
                        queuePlayer(hgInfo, player)
                    } else if (event.isLeftClick) {
                        sendPlayer(hgInfo, player)
                    }
                }
            }
        })

    fun queuePlayer(hgInfo: HGInfo, player: Player) {
        val gson = Gson()
        val hgQueueInfo = HGQueueInfo(hgInfo.serverName!!)
        player.sendPluginMessage(Manager, HG_QUEUE, gson.toJson(hgQueueInfo, hgQueueInfo.javaClass).toByteArray())
    }

    fun sendPlayer(hgInfo: HGInfo, player: Player) {
        val bytearray = ByteArrayOutputStream()
        val out = DataOutputStream(bytearray)
        try {
            out.writeUTF("Connect")
            out.writeUTF(hgInfo.serverName!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        player.sendPluginMessage(Manager, "BungeeCord", bytearray.toByteArray())
    }

    fun setContent(elements: Iterable<GUICompoundElement<ForInventoryThreeByNine>>) {
        menuCompound?.setContent(elements)
    }

    fun enable() {
        listen<PlayerInteractEvent> {
            if (!it.action.isRightClick) return@listen
            if (it.item?.hasMark("hgqueue") == true) {
                it.isCancelled = true
                it.player.openGUI(gui)
            }
        }
    }
}
