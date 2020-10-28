@file:UseSerializers(LocationSerializer::class)

package net.axay.hglaborlobby.data

import com.github.jershell.kbson.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.axay.kspigot.serialization.LocationSerializer
import org.bukkit.Location
import java.util.*

@Serializable
data class PlayerSettings(

    @Serializable(with = UUIDSerializer::class) @SerialName("_id") val uuid: UUID,

    val customSpawnLoc: Location? = null,
    val privacySettings: PlayerPrivacySettings = PlayerPrivacySettings(
        ifCountry = false,
        ifState = false,
        ifCity = false
    ),
    val ifSeeJoinMessages: Boolean = true

) {

    @Serializable
    data class PlayerPrivacySettings(
        val ifCountry: Boolean,
        val ifState: Boolean,
        val ifCity: Boolean
    ) {
        val ifLoadAny get() = ifCountry || ifState || ifCity
    }

}