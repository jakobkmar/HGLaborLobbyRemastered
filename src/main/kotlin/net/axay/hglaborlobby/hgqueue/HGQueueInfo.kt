package net.axay.hglaborlobby.hgqueue

import org.bukkit.plugin.messaging.StandardMessenger

class HGQueueInfo(var serverName: String)

val HG_QUEUE = StandardMessenger.validateAndCorrectChannel("hglabor:hgqueue")