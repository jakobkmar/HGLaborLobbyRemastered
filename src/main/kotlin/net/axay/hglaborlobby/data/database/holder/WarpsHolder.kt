package net.axay.hglaborlobby.data.database.holder

import net.axay.hglaborlobby.config.ConfigManager
import net.axay.hglaborlobby.damager.locationArea
import net.axay.hglaborlobby.database.DatabaseManager

object WarpsHolder {

    var instance = WarpsHolderInstance()

    fun reloadWarps() {
       instance = WarpsHolderInstance()
    }

}

class WarpsHolderInstance {

    val warps by lazy { ConfigManager.warpConfig.warps }

    val spawn by lazy { warps[0] }

}