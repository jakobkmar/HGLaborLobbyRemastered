package net.axay.hglaborlobby.hgqueue

import com.google.gson.Gson
import net.axay.hglaborlobby.gui.guis.HGQueueGUI
import net.axay.kspigot.gui.ForInventoryThreeByNine
import net.axay.kspigot.gui.elements.GUICompoundElement
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.messaging.PluginMessageListener
import java.nio.charset.StandardCharsets


object HGInformationListener : PluginMessageListener {
    private val GSON = Gson()

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        val hgInformationString = String(message, StandardCharsets.UTF_8)
        val hgInfos = GSON.fromJson(hgInformationString, Array<HGInfo>::class.java)
        val items = mutableListOf<ItemStack>()
        val compoundElements = mutableListOf<GUICompoundElement<ForInventoryThreeByNine>>()

        hgInfos.sortedWith(compareBy<HGInfo> {it.gameState}.thenBy { it.onlinePlayers })

        hgInfos.forEach { hgInfo ->
            hgInfo.item = hgInfo.getNewItem()
            items.add(hgInfo.item!!)
            HGInfo.infos[hgInfo.serverName!!] = hgInfo
        }
        items.forEach { compoundElements.add(HGQueueGUI.HGQueueGUICompoundElement(it)) }
        HGQueueGUI.setContent(compoundElements)
    }
}
