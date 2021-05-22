package net.axay.hglaborlobby.data.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.axay.kspigot.serialization.LocationSerializer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material

@Serializable
data class Warp(
    @SerialName("_id") val name: String,
    val world: String,
    val location: ArrayList<Float>,
    val description: String?,
    val icon: String
)

fun Warp.location(): Location {
    return Location(Bukkit.getWorld(world),
        location[0].toDouble(), location[1].toDouble(), location[2].toDouble(), location[3], location[4])
}