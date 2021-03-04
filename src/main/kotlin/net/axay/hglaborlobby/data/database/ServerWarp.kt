package net.axay.hglaborlobby.data.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.Material

@Serializable
data class ServerWarp(
    @SerialName("_id") val name: String,
    val serverName: String,
    val icon: Material
)