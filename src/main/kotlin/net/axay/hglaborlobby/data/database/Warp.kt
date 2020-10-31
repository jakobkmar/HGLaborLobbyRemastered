@file:UseSerializers(LocationSerializer::class)

package net.axay.hglaborlobby.data.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.axay.kspigot.serialization.LocationSerializer
import org.bukkit.Location
import org.bukkit.Material

@Serializable
data class Warp(
    @SerialName("_id") val name: String,
    val location: Location,
    val description: String?,
    val icon: Material
)