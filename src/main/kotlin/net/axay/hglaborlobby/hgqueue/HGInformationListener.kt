package net.axay.hglaborlobby.hgqueue

import net.axay.hglaborlobby.gui.guis.HGQueueGUI
import net.axay.hglaborlobby.main.gson
import net.axay.kspigot.gui.ForInventoryThreeByNine
import net.axay.kspigot.gui.elements.GUICompoundElement
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.messaging.PluginMessageListener
import java.nio.charset.StandardCharsets


object HGInformationListener : PluginMessageListener {
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        val hgInformationString = String(message, StandardCharsets.UTF_8)
        val hgInfos = gson.fromJson(hgInformationString, Array<HGInfo>::class.java)
        val items = mutableListOf<ItemStack>()
        val compoundElements = mutableListOf<GUICompoundElement<ForInventoryThreeByNine>>()

        hgInfos.sortedWith(compareBy<HGInfo> { it.gameState }.thenBy { it.onlinePlayers })
            .forEach { hgInfo ->
                hgInfo.item = hgInfo.getNewItem()
                items.add(hgInfo.item!!)
                HGInfo.infos[hgInfo.serverName!!] = hgInfo
            }
        items.forEach { compoundElements.add(HGQueueGUI.HGQueueGUICompoundElement(it)) }
        HGQueueGUI.setContent(compoundElements)
    }
}
