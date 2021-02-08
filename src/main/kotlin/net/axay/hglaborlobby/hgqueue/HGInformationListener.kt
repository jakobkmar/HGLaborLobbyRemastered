package net.axay.hglaborlobby.hgqueue

import com.google.gson.Gson
import net.axay.hglaborlobby.gui.guis.HGQueueGUI
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.gui.ForInventoryThreeByNine
import net.axay.kspigot.gui.elements.GUICompoundElement
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import java.nio.charset.StandardCharsets


object HGInformationListener : PluginMessageListener {
    private val GSON = Gson()

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        val hgInformationString = String(message, StandardCharsets.UTF_8)
        val hgInfos = GSON.fromJson(hgInformationString, Array<HGInfo>::class.java)
        val items = mutableListOf<GUICompoundElement<ForInventoryThreeByNine>>()

        hgInfos.forEach { hgInfo ->
            hgInfo.item = hgInfo.getNewItem()
            items.add(HGQueueGUI.HGQueueGUICompoundElement(hgInfo.item!!))
            HGInfo.infos[hgInfo.getServerName()!!] = hgInfo
        }

        HGQueueGUI.setContent(items)
    }
}
