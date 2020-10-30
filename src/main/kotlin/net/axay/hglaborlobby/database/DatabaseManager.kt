package net.axay.hglaborlobby.database

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.hglaborlobby.config.ConfigManager
import net.axay.hglaborlobby.data.PlayerSettings
import net.axay.hglaborlobby.data.Warp
import net.axay.hglaborlobby.main.PLUGIN_DATA_PREFIX

object DatabaseManager {

    private val mongoDB = MongoDB(ConfigManager.databaseLoginInformation, spigot = true)

    val playerSettings = mongoDB.getCollectionOrCreate<PlayerSettings>("${PLUGIN_DATA_PREFIX}player_settings")

    val warps = mongoDB.getCollectionOrCreate<Warp>("${PLUGIN_DATA_PREFIX}warps")

}