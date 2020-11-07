package net.axay.hglaborlobby.eventmanager.joinserver

import net.axay.hglaborlobby.data.database.holder.PlayerSettingsHolder
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

                if (settings.ifContinent) appendToLocation(ipData.continentCode)
                if (settings.ifCountry) appendToLocation(ipData.country)
                if (settings.ifState) appendToLocation(ipData.region)
                if (settings.ifCity) appendToLocation(ipData.city)

                locationString = " ${KColors.DIMGRAY}(${locationBuilder.removeSuffix(" / ")})"

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