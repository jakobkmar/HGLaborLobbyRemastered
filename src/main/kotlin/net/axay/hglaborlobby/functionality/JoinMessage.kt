package net.axay.hglaborlobby.functionality

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.ipaddress.ipAddressData
import net.axay.kspigot.runnables.async
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

object JoinMessage {

    fun enable() {

        listen<PlayerJoinEvent>(EventPriority.HIGHEST) {

            // disable original join message
            it.joinMessage = null

            val player = it.player

            async {

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

                // send the joinmessage

                val joinMessage = "${KColors.CHARTREUSE}→ ${KColors.POWDERBLUE}${player.name}$locationString"

                onlinePlayers.forEach { messageReceiver ->
                    if (PlayerSettingsHolder[messageReceiver].ifSeeJoinMessages)
                        messageReceiver.sendMessage(joinMessage)
                }

            }

        }

    }

}