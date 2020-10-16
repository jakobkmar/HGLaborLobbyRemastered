package net.axay.hglaborlobby.config

import net.axay.blueutils.database.DatabaseLoginInformation
import net.axay.kspigot.config.PluginFile
import net.axay.kspigot.config.kSpigotJsonConfig

object ConfigManager {

    val databaseLoginInformation
            by kSpigotJsonConfig(PluginFile("databaseLoginInformation.json"))
                { DatabaseLoginInformation.NOTSET_DEFAULT }

}