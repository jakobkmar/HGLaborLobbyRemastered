package net.axay.hglaborlobby.database.data

import com.fasterxml.jackson.annotation.JsonInclude
import net.axay.kspigot.serialization.serializables.SerializableLocation
import org.bukkit.Material

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DatabaseWarp(
    val name: String,
    val location: SerializableLocation,
    val description: String?,
    val icon: Material
)