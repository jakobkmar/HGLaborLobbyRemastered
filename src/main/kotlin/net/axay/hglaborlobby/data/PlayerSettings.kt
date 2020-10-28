@file:UseSerializers(LocationSerializer::class)

package net.axay.hglaborlobby.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.axay.kspigot.serialization.LocationSerializer
import org.bukkit.Location
import java.util.*

@Serializable
data class PlayerSettings(
    @Contextual @SerialName("_id") val uuid: UUID,
    val customSpawnLoc: Location?,
    val privacySettings: PlayerPrivacySettings,
    val ifSeeJoinMessages: Boolean
) {

    companion object {
        fun createDefault(uuid: UUID) = PlayerSettings(
            uuid,
            null,
            PlayerPrivacySettings(ifCountry = false, ifState = false, ifCity = false),
            true
        )
    }

    @Serializable
    data class PlayerPrivacySettings(
        val ifCountry: Boolean,
        val ifState: Boolean,
        val ifCity: Boolean
    ) {
        val ifLoadAny get() = ifCountry || ifState || ifCity
    }

}