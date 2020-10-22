package net.axay.hglaborlobby.security

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.kick
import net.axay.kspigot.ipaddress.BadIPDetector
import net.axay.kspigot.ipaddress.badipdetectionservices.GetIPIntel
import net.axay.kspigot.ipaddress.badipdetectionservices.IPHub
import net.axay.kspigot.ipaddress.badipdetectionservices.VPNBlocker
import net.axay.kspigot.ipaddress.checkIP
import net.axay.kspigot.runnables.sync
import org.bukkit.entity.Player

object BadIPDetection {

    val detector = BadIPDetector(
        GetIPIntel(),
        IPHub(),
        VPNBlocker()
    )

    fun checkPlayer(player: Player) {

        player.checkIP(detector).filter { it.value.isBad }.values.firstOrNull()?.let {

            sync {

                player.kick(it.typeName.toUpperCase())

                broadcast("${KColors.PALEVIOLETRED}← ${KColors.GRAY}${player.name} ${KColors.INDIANRED}${KColors.BOLD}${it.typeName.toUpperCase()}")

            }

        }

    }

}