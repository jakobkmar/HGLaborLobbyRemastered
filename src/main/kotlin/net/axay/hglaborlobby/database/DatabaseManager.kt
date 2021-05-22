package net.axay.hglaborlobby.database

import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.hglaborlobby.config.ConfigManager
import net.axay.hglaborlobby.data.database.*
import net.axay.hglaborlobby.main.PLUGIN_DATA_PREFIX

object DatabaseManager {

    val mongoDB = MongoDB(ConfigManager.databaseLoginInformation, spigot = true)

    val playerSettings = mongoDB.getCollectionOrCreate<PlayerSettings>("${PLUGIN_DATA_PREFIX}player_settings")

    val warps = mongoDB.getCollectionOrCreate<Warp>("${PLUGIN_DATA_PREFIX}warps")

    val ipAddresses = mongoDB.getCollectionOrCreate<IPCheckData>("${PLUGIN_DATA_PREFIX}ipcheckdata")

    val areas = mongoDB.getCollectionOrCreate<Area>("${PLUGIN_DATA_PREFIX}areas")

}