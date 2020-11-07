package net.axay.hglaborlobby.security

import net.axay.hglaborlobby.config.ConfigManager
import net.axay.hglaborlobby.data.database.IPCheckData
import net.axay.hglaborlobby.database.DatabaseManager
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.bukkit.info
import net.axay.kspigot.extensions.bukkit.kick
import net.axay.kspigot.extensions.console
import net.axay.kspigot.ipaddress.BadIPDetectionResult
import net.axay.kspigot.ipaddress.BadIPDetector
import net.axay.kspigot.ipaddress.badipdetectionservices.GetIPIntel
import net.axay.kspigot.ipaddress.badipdetectionservices.IPHub
import net.axay.kspigot.ipaddress.checkIP
import net.axay.kspigot.ipaddress.ipAddressOrNull
import net.axay.kspigot.runnables.sync
import org.bukkit.entity.Player
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.save
import java.time.Instant

object BadIPDetection {

    private const val checkExpiresAfterSeconds = 60 * 60 * 24 * 30L

    private val detector = BadIPDetector(
        IPHub(ConfigManager.ipServiceConfig.ipHubApiKey)
    )

    fun checkPlayer(player: Player): String? {

        val ip = player.ipAddressOrNull
        if (ip != null) {

            // find out if the ip has already been checked and was not bad

            var mustCheck = true

            val checkData = DatabaseManager.ipAddresses.findOne(IPCheckData::ip eq player.ipAddressOrNull)
            if (checkData != null) {

                if (checkData.expiresAt > Instant.now()) {
                    if (!checkData.isBad)
                        mustCheck = false
                }

            }

            if (!mustCheck) return null

            // now check the players ip address

            val checkResult = player.checkIP(detector)

            console.info("IP check result for player ${player.name}: ${checkResult.values}")

            // no result or only error codes?
            if (!checkResult.all { it.value == BadIPDetectionResult.ERROR || it.value == BadIPDetectionResult.LIMIT }) {

                // is there a bad result?
                val badResult = checkResult.filter { it.value.isBad }.values.firstOrNull()?.typeName?.toUpperCase()

                // save result to database
                val expiresAfter = Instant.now().plusSeconds(checkExpiresAfterSeconds)
                DatabaseManager.ipAddresses.save(
                    if (badResult != null)
                        IPCheckData(ip, true, expiresAfter)
                    else
                        IPCheckData(ip, false, expiresAfter)
                )

                badResult?.let {
                    sync { player.kick(it) }
                    return "${KColors.INDIANRED}${KColors.BOLD}$it"
                }

                return null

            }

        }

        return "${KColors.LIGHTGRAY}(not checked)"

    }

}