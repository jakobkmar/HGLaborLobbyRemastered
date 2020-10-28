@file:UseSerializers(LocationSerializer::class)

package net.axay.hglaborlobby.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.axay.kspigot.serialization.LocationSerializer
import org.bukkit.Location
import org.bukkit.Material

@Serializable
data class Warp(
    val name: String,
    val location: Location,
    val description: String?,
    val icon: Material
)