package net.axay.hglaborlobby.data.database.holder

import net.axay.hglaborlobby.database.DatabaseManager

object WarpsHolder {

    var instance = WarpsHolderInstance()

    fun reloadWarps() {
       instance = WarpsHolderInstance()
    }

}

class WarpsHolderInstance {

    val warps by lazy { DatabaseManager.warps.find().toList() }

    val spawn by lazy { warps.find { it.name.equals(other = "Spawn", ignoreCase = true) } }

}