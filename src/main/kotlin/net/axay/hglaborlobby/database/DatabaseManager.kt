package net.axay.hglaborlobby.database

import com.mongodb.client.model.IndexOptions
import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.hglaborlobby.config.ConfigManager
import net.axay.hglaborlobby.database.data.PlayerSettings
import net.axay.hglaborlobby.main.PLUGIN_DATA_PREFIX
import org.litote.kmongo.ascendingIndex

object DatabaseManager {

    private val mongoDB = MongoDB(ConfigManager.databaseLoginInformation, spigot = true)

    val playerSettings
            = mongoDB.getCollectionOrCreate<PlayerSettings>("${PLUGIN_DATA_PREFIX}player_settings") {
                it.createIndex(ascendingIndex(PlayerSettings::uuid), IndexOptions().unique(true))
            }

}