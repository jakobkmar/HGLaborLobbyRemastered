package net.axay.hglaborlobby.database

import com.mongodb.client.model.IndexOptions
import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.hglaborlobby.config.ConfigManager
import net.axay.hglaborlobby.main.PLUGIN_DATA_PREFIX
import net.axay.kspigot.serialization.serializables.SerializableLocation
import org.litote.kmongo.ascendingIndex
import java.util.*

object DatabaseManager {

    private val mongoDB = MongoDB(ConfigManager.databaseLoginInformation)

    val playerSettings
            = mongoDB.getCollectionOrCreate<DatabasePlayerSettings>("${PLUGIN_DATA_PREFIX}player_settings") {
                it.createIndex(ascendingIndex(DatabasePlayerSettings::uuid), IndexOptions().unique(true))
            }

}

data class DatabasePlayerSettings(
        val uuid: UUID,
        val customSpawnLoc: SerializableLocation
)