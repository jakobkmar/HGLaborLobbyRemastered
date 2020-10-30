package net.axay.hglaborlobby.main

import net.axay.hglaborlobby.eventmanager.joinserver.JoinMessage
import net.axay.hglaborlobby.eventmanager.joinserver.OnJoinManager
import net.axay.hglaborlobby.eventmanager.leaveserver.KickMessageListener
import net.axay.hglaborlobby.eventmanager.leaveserver.OnLeaveManager
import net.axay.hglaborlobby.functionality.LobbyItems
import net.axay.hglaborlobby.functionality.PlayerSettingsHolder
import net.axay.hglaborlobby.functionality.SoupHealing
import net.axay.hglaborlobby.gui.guis.AdminGUI
import net.axay.hglaborlobby.gui.guis.MainGUI
import net.axay.hglaborlobby.gui.guis.PlayerVisiblityGUI
import net.axay.hglaborlobby.protection.ServerProtection
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.info
import net.axay.kspigot.extensions.bukkit.register
import net.axay.kspigot.extensions.bukkit.success
import net.axay.kspigot.extensions.console
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.sound.sound
import org.bukkit.Sound

class InternalMainClass : KSpigot() {

    companion object {
        lateinit var INSTANCE: InternalMainClass; private set
    }

    override fun load() {

        INSTANCE = this

        console.info("Loading Lobby plugin...")

    }

    override fun startup() {

        ServerProtection.enable()

        PlayerSettingsHolder.enable()

        SoupHealing.enable()
        JoinMessage.enable()
        OnJoinManager.enable()
        LobbyItems.enable()
        OnLeaveManager.enable()
        KickMessageListener.enable()

        AdminGUI.register("admingui")

        broadcast("${KColors.MEDIUMSPRINGGREEN}-> ENABLED PLUGIN")
        onlinePlayers.forEach { it.sound(Sound.BLOCK_BEACON_ACTIVATE) }

        console.success("Lobby plugin enabled.")

    }

    override fun shutdown() {

        console.info("Shutting down Lobby plugin...")

        broadcast("${KColors.TOMATO}-> DISABLING PLUGIN ${KColors.DARKGRAY}(maybe a reload)")
        onlinePlayers.forEach { it.sound(Sound.BLOCK_BEACON_DEACTIVATE) }

        console.success("Shut down Lobby plugin.")

    }

}

val Manager by lazy { InternalMainClass.INSTANCE }