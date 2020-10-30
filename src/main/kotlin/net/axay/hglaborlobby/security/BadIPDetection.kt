package net.axay.hglaborlobby.security

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.kick
import net.axay.kspigot.ipaddress.BadIPDetectionResult
import net.axay.kspigot.ipaddress.BadIPDetector
import net.axay.kspigot.ipaddress.badipdetectionservices.GetIPIntel
import net.axay.kspigot.ipaddress.badipdetectionservices.IPHub
import net.axay.kspigot.ipaddress.checkIP
import net.axay.kspigot.runnables.sync
import org.bukkit.entity.Player

object BadIPDetection {

    private val detector = BadIPDetector(
        GetIPIntel(),
        IPHub()
    )

    fun checkPlayer(player: Player): String? {

        val checkResult = player.checkIP(detector)

        if (checkResult.all { it.value == BadIPDetectionResult.ERROR || it.value == BadIPDetectionResult.LIMIT })
            return "${KColors.LIGHTGRAY}(not checked)"

        checkResult.filter { it.value.isBad }.values.firstOrNull()?.let {

            val result = it.typeName.toUpperCase()

            sync {
                player.kick(result)
            }

            return "${KColors.INDIANRED}${KColors.BOLD}$result"

        }

        return null

    }

}