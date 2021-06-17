package net.axay.hglaborlobby.data.database

import com.github.jershell.kbson.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class HGStats(
    @SerialName("_id") @Serializable(with = UUIDSerializer::class) val uuid: UUID,
    val kills: Int = 0,
    val deaths: Int = 0,
    val wins: Int = 0,
    val gamesPlayed: Int = 0,
    val playtime: Int = 0,
    val killRecord: Int = 0,
    val kits: Map<String, Int> = mapOf(),
)
