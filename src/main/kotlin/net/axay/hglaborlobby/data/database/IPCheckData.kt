package net.axay.hglaborlobby.data.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.serialization.InstantSerializer
import java.time.Instant

@Serializable
data class IPCheckData(
    @SerialName("_id") val ip: String,
    val isBad: Boolean,
    @Serializable(with = InstantSerializer::class) val expiresAt: Instant
)