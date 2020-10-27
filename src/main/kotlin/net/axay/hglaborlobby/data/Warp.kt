package net.axay.hglaborlobby.data

import com.fasterxml.jackson.annotation.JsonInclude
import net.axay.kspigot.serialization.serializables.SerializableLocation
import org.bukkit.Material

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Warp(
    val name: String,
    val location: SerializableLocation,
    val description: String?,
    val icon: Material
)