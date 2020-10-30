package net.axay.hglaborlobby.main

const val PLUGIN_NAME = "lobby"
const val PLUGIN_DATA_PREFIX = "${PLUGIN_NAME}_"

fun lobbyPermission(perm: String) = "${PLUGIN_NAME}.$perm"