package net.axay.hglaborlobby.config

import net.axay.blueutils.database.DatabaseLoginInformation
import net.axay.hglaborlobby.data.IPServiceConfig
import net.axay.kspigot.config.PluginFile
import net.axay.kspigot.config.kSpigotJsonConfig

object ConfigManager {

    val databaseLoginInformation
            by kSpigotJsonConfig(PluginFile("databaseLoginInformation.json")) {
                DatabaseLoginInformation.NOTSET_DEFAULT
            }

    val ipServiceConfig
            by kSpigotJsonConfig(PluginFile("ipServiceConfig.json")) {
                IPServiceConfig.NOTSET_DEFAULT
            }

}