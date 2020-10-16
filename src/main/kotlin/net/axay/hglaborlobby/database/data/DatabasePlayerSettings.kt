package net.axay.hglaborlobby.database.data

import com.fasterxml.jackson.annotation.JsonInclude
import net.axay.kspigot.serialization.serializables.SerializableLocation
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PlayerSettings(
    val uuid: UUID,
    val customSpawnLoc: SerializableLocation? = null,
    val privacySettings: PlayerPrivacySettings? = null
) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class PlayerPrivacySettings(
        val ifCountry: Boolean,
        val ifState: Boolean,
        val ifCity: Boolean
    )

}