package net.axay.hglaborlobby.functionality

import net.axay.hglaborlobby.main.Manager
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.toLoreList
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object LobbyItems {

    private fun givePlayer(player: Player) {

        val contents = HashMap<Int, ItemStack>()

        contents[8] = itemStack(Material.LODESTONE) {
            meta {

                name = "${KColors.CORAL}MENÜ"
                lore = "Rechtsklicke dieses Item, um das Hauptmenü zu öffnen."
                    .toLoreList(lineColor = KColors.CHARTREUSE)

                persistentDataContainer[NamespacedKey(Manager, "lobby_items_maingui"), PersistentDataType.BYTE] =
                    1.toByte()

            }
        }

        val playerInv = player.inventory
        contents.forEach { (index, item) ->
            playerInv.setItem(index, item)
        }

    }

    fun enable() {

        listen<PlayerJoinEvent> {
            givePlayer(it.player)
        }

        listen<PlayerRespawnEvent> {
            givePlayer(it.player)
        }

    }

}