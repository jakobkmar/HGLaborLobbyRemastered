package net.axay.hglaborlobby.eventmanager.joinserver

import net.axay.hglaborlobby.functionality.PlayerSettingsHolder
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.ipaddress.ipAddressData
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent

object JoinMessage {

    fun joinMessage(player: Player): String {

        // load geolocation

        var locationString = ""

        val settings = PlayerSettingsHolder[player].privacySettings
        if (settings.ifLoadAny) {

            val ipData = player.ipAddressData
            if (ipData != null) {

                val locationBuilder = StringBuilder()

                fun appendToLocation(string: String?) {
                    if (string != null) locationBuilder.append(string).append(" / ")
                }

                appendToLocation(ipData.continentCode)
                appendToLocation(ipData.country)
                appendToLocation(ipData.region)
                appendToLocation(ipData.city)

                if (locationBuilder.isNotEmpty())
                    locationBuilder.removeSuffix(" / ")

                locationString = " ${KColors.DIMGRAY}(${locationBuilder})"

            }

        }

        return "${KColors.CHARTREUSE}→ ${KColors.POWDERBLUE}${player.name}$locationString"

    }

    fun enable() {

        listen<PlayerJoinEvent> {
            // disable original join message
            it.joinMessage = null
        }

    }

}