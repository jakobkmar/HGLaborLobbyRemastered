package net.axay.hglaborlobby.data.database.holder

import net.axay.hglaborlobby.database.DatabaseManager

object ServerWarpsHolder {

    var instance = ServerWarpsHolderInstance()

    fun reloadWarps() {
        instance = ServerWarpsHolderInstance()
    }

    class ServerWarpsHolderInstance {

        val serverWarps by lazy { DatabaseManager.serverWarps.find().toList() }

    }
}