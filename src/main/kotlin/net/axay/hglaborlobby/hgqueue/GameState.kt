package net.axay.hglaborlobby.hgqueue

import net.axay.kspigot.chat.KColors
import net.md_5.bungee.api.ChatColor
import org.bukkit.Material

enum class GameState(val color: ChatColor, val material: Material) {
    LOBBY(KColors.LIME, Material.LIME_STAINED_GLASS_PANE),
    INVINCIBILITY(KColors.YELLOW, Material.YELLOW_STAINED_GLASS_PANE),
    INGAME(KColors.ORANGE, Material.ORANGE_STAINED_GLASS_PANE),
    END(KColors.DARKRED, Material.RED_STAINED_GLASS_PANE)
}

fun GameState.name() = toString().toLowerCase().capitalize()