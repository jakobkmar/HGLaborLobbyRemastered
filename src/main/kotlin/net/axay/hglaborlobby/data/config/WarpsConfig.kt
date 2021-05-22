package net.axay.hglaborlobby.data.config

import net.axay.hglaborlobby.data.database.Warp

data class WarpsConfig(
    val warps: ArrayList<Warp> = arrayListOf(Warp("Spawn", "hub", arrayListOf(135.5f, 103f, 15f, 90f, 0f), "Der Punkt, an dem du spawnst, wenn du den Server betrittst.", "NETHER_STAR"))
)
