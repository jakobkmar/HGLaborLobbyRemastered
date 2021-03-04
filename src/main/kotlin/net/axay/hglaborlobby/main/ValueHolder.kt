package net.axay.hglaborlobby.main

import com.google.gson.Gson

const val PLUGIN_NAME = "lobby"
const val PLUGIN_DATA_PREFIX = "${PLUGIN_NAME}_"
const val LOBBY_PREFIX = "§7[§bHG§7Labor]"

fun lobbyPermission(perm: String) = "${PLUGIN_NAME}.$perm"

val gson by lazy { Gson() }