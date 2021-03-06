package net.axay.hglaborlobby.data.database

import com.google.common.io.ByteStreams
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.axay.hglaborlobby.data.database.ServerWarp.Companion.playerCount
import net.axay.hglaborlobby.data.database.holder.ServerWarpsHolder
import net.axay.hglaborlobby.main.InternalMainClass
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setMeta
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

@Serializable
data class ServerWarp(
    @SerialName("_id") val name: String,
    val serverName: String,
    val icon: Material) {

    companion object {
        val playerCounts = hashMapOf<String, Int>()

        val ServerWarp.playerCount get() = playerCounts[serverName] ?: -1

        fun updatePlayerCounts() {
            ServerWarpsHolder.instance.serverWarps.forEach {
                val b = ByteArrayOutputStream()
                val out = DataOutputStream(b)
                out.writeUTF("PlayerCount")
                out.writeUTF(it.serverName)
                Bukkit.getServer().sendPluginMessage(InternalMainClass.INSTANCE, "BungeeCord", b.toByteArray())
            }
        }
    }
}

val ServerWarp.itemStack
    get() = itemStack(icon) {
        setMeta {
            name = "${KColors.CORAL}${name?.capitalize()}"
            lore = arrayListOf<String>().apply {
                this += "${KColors.DARKGRAY}${KColors.STRIKETHROUGH}                      "
                this += "${KColors.GRAY}Spieler ${KColors.DARKGRAY}» ${KColors.DODGERBLUE}$playerCount"
            }
        }
    }

object ServerWarpPluginMessageListener : PluginMessageListener {
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != "BungeeCord") { return }
        val `in` = ByteStreams.newDataInput(message)
        val subchannel = `in`.readUTF()
        if (subchannel == "PlayerCount") {
            val server = `in`.readUTF()
            val int = `in`.readInt()
            ServerWarp.playerCounts[server] = int
            broadcast("${KColors.PLUM}$server $int")
        }
    }
}