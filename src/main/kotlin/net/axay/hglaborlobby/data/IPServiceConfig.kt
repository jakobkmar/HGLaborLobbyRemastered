package net.axay.hglaborlobby.data

data class IPServiceConfig(
    val ipInfoToken: String
) {

    companion object {
        val NOTSET_DEFAULT = IPServiceConfig("notset")
    }

}