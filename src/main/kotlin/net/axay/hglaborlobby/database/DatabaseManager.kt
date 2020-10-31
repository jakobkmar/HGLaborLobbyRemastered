package net.axay.hglaborlobby.database

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.hglaborlobby.config.ConfigManager
import net.axay.hglaborlobby.data.database.IPCheckData
import net.axay.hglaborlobby.data.database.PlayerSettings
import net.axay.hglaborlobby.data.database.Warp
import net.axay.hglaborlobby.main.PLUGIN_DATA_PREFIX

object DatabaseManager {

    private val mongoDB = MongoDB(ConfigManager.databaseLoginInformation, spigot = true)

    val playerSettings = mongoDB.getCollectionOrCreate<PlayerSettings>("${PLUGIN_DATA_PREFIX}player_settings")

    val warps = mongoDB.getCollectionOrCreate<Warp>("${PLUGIN_DATA_PREFIX}warps")

    val ipAddresses = mongoDB.getCollectionOrCreate<IPCheckData>("${PLUGIN_DATA_PREFIX}ipcheckdata")

}