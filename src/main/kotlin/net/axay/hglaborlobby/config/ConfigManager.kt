package net.axay.hglaborlobby.config

import net.axay.hglaborlobby.data.config.IPServiceConfig
import net.axay.kspigot.config.PluginFile
import net.axay.kspigot.config.kSpigotJsonConfig
import org.bukkit.configuration.file.YamlConfiguration

object ConfigManager {

    val hgStatsDbPassword
    get() = hgStatsDbConfig.getString("dbPassword")?.toCharArray() ?: "bingus".toCharArray()
    val hgStatsDbUsername
    get() = hgStatsDbConfig.getString("dbUsername") ?: "name"
    val hgStatsDbDatabase
    get() = hgStatsDbConfig.getString("dbDatabase") ?: "db"

    val otherDbPassword
        get() = otherDbConfig.getString("dbPassword")?.toCharArray() ?: "bingus".toCharArray()
    val otherDbUsername
        get() = otherDbConfig.getString("dbUsername") ?: "name"
    val otherDbDatabase
        get() = otherDbConfig.getString("dbDatabase") ?: "db"

    val ipServiceConfig
            by kSpigotJsonConfig(PluginFile("ipServiceConfig.json"), true) {
                IPServiceConfig()
            }

    private val hgStatsDbConfig = YamlConfiguration.loadConfiguration(PluginFile("hgStatsDb.yml"))

    private val otherDbConfig = YamlConfiguration.loadConfiguration(PluginFile("otherDb.yml"))


}