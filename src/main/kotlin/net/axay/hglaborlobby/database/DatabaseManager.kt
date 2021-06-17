package net.axay.hglaborlobby.database

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.axay.blueutils.database.DatabaseLoginInformation
import net.axay.blueutils.database.mongodb.MongoDB
import net.axay.hglaborlobby.config.ConfigManager
import net.axay.hglaborlobby.data.database.*
import net.axay.hglaborlobby.main.PLUGIN_DATA_PREFIX
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

val mongoScope = CoroutineScope(Dispatchers.IO)

object DatabaseManager {

    //val mongoDB = MongoDB(DatabaseLoginInformation.NOTSET_DEFAULT, spigot = true)

    val mongoClient = KMongo.createClient(settings = MongoClientSettings.builder()
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .credential(MongoCredential.createCredential(
            ConfigManager.otherDbUsername,
            ConfigManager.otherDbDatabase,
            ConfigManager.otherDbPassword
        ))
        .applyToClusterSettings { it.hosts(listOf(ServerAddress("localhost"))) }
        .build()
    )

    val statsClient = KMongo.createClient(settings = MongoClientSettings.builder()
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .credential(MongoCredential.createCredential(
            ConfigManager.hgStatsDbUsername,
            ConfigManager.hgStatsDbDatabase,
            ConfigManager.hgStatsDbPassword
        ))
        .applyToClusterSettings { it.hosts(listOf(ServerAddress("localhost"))) }
        .build()
    )

    val statsDB = statsClient.getDatabase("hg")

    val mongoDB = mongoClient.getDatabase("hglabordb")

    init {
        //System.setProperty("org.litote.mongo.test.mapping.service", "org.litote.kmongo.jackson.JacksonClassMappingTypeService")
    }

    val hgStats = statsDB.getCollection<HGStats>("hgStats")

    val playerNameCache = statsDB.getCollection<PlayerNameCache>("playerNameCache")

    //val playerSettings = mongoDB.getCollectionOrCreate<PlayerSettings>("${PLUGIN_DATA_PREFIX}player_settings")

    //val warps = mongoDB.getCollectionOrCreate<Warp>("${PLUGIN_DATA_PREFIX}warps")

    val ipAddresses = mongoDB.getCollection<IPCheckData>("${PLUGIN_DATA_PREFIX}ipcheckdata")

    //val areas = mongoDB.getCollectionOrCreate<Area>("${PLUGIN_DATA_PREFIX}areas")

}