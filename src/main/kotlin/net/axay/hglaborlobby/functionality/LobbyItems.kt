package net.axay.hglaborlobby.functionality

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.toLoreList
import net.axay.kspigot.utils.hasMark
import net.axay.kspigot.utils.mark
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack

object LobbyItems {

    fun givePlayer(player: Player) {

        player.inventory.clear()

        val contents = HashMap<Int, ItemStack>()

        contents[8] = itemStack(Material.LODESTONE) {
            meta {
                name = "${KColors.CORAL}MENÜ"
                lore = "Rechtsklicke dieses Item, um das Hauptmenü zu öffnen."
                    .toLoreList(KColors.CHARTREUSE)
            }
            mark("maingui")
        }

        val playerInv = player.inventory
        contents.forEach { (index, item) ->
            item.mark("lobbyitem")
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

val ItemStack.isLobbyItem get() = hasMark("lobbyitem")