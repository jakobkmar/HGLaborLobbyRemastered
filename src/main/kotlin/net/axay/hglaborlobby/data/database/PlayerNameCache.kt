package net.axay.hglaborlobby.data.database

import com.github.jershell.kbson.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class PlayerNameCache(
    @SerialName("_id") @Serializable(with = UUIDSerializer::class) val uuid: UUID,
    val name: String?
)
