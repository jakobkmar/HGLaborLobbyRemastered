package net.axay.hglaborlobby.damager

import net.axay.hglaborlobby.main.LOBBY_PREFIX
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

object DamagerDifficulty {

    private var inconsistencyPlayers = mutableSetOf<String>()
    private var crapDamagerPlayers = mutableSetOf<String>()
    var inconsistencyRanges = mutableMapOf<String, Pair<Int, Int>>()
    val crapItems = listOf(
        Material.OAK_PLANKS,
        Material.STICK,
        Material.OAK_SAPLING,
        Material.DIAMOND,
        Material.DIRT,
        Material.STONE_SWORD,
        Material.MUSHROOM_STEW
    )

    fun enable() {
        listen<PlayerInteractEvent> {
            val clickedBlock = it.clickedBlock
            if (clickedBlock?.type != Material.OAK_WALL_SIGN) return@listen
            val sign = clickedBlock.state as Sign
            if (sign.lines[1] != "DAMAGER") return@listen
            if (it.player.isInDamager) return@listen
            it.player.openGUI(buildDamageGUI(it.player), 0)
        }
    }

    fun buildDamageGUI(player: Player): GUI<ForInventoryOneByNine> {
        return kSpigotGUI(GUIType.ONE_BY_NINE) {
            title = "${KColors.INDIANRED} Difficulty"

            page(0) {

                transitionFrom = PageChangeEffect.SLIDE_HORIZONTALLY
                transitionTo = PageChangeEffect.SLIDE_HORIZONTALLY

                lateinit var compound: GUIRectSpaceCompound<*, Boolean>
                compound = createRectCompound(
                    Slots.RowOneSlotOne,
                    Slots.RowOneSlotOne,
                    iconGenerator = {
                        itemStack(Material.STICK) {
                            val status = if (it) "${KColors.GREEN}enabled" else "${KColors.RED}disabled"
                            meta { name = "${KColors.GRAY}Crap Damager: $status" }
                        }
                    },
                    onClick = { e, b ->
                        updateCrapDamagerInfo(player, e.bukkitEvent.currentItem)
                        compound.setContent(!b)
                        e.bukkitEvent.isCancelled = true
                    }
                )
                compound.addContent(player.crapDamagerEnabled)

                button(Slots.RowOneSlotTwo, itemStack(Material.WHITE_DYE) {
                    meta { name = "${org.bukkit.ChatColor.WHITE}1.5 Herzen" }
                }) {
                    updatePlayerDamage(player, 3.0)
                }
                button(Slots.RowOneSlotThree, itemStack(Material.GREEN_DYE) {
                    meta { name = "${org.bukkit.ChatColor.DARK_GREEN}2 Herzen" }
                }) {
                    updatePlayerDamage(player, 4.0)
                }
                button(Slots.RowOneSlotFour, itemStack(Material.LIME_DYE) {
                    meta { name = "${org.bukkit.ChatColor.GREEN}2.5 Herzen" }
                }) {
                    updatePlayerDamage(player, 5.0)
                }
                button(Slots.RowOneSlotFive, itemStack(Material.YELLOW_DYE) {
                    meta { name = "${org.bukkit.ChatColor.YELLOW}3 Herzen" }
                }) {
                    updatePlayerDamage(player, 6.0)
                }
                button(Slots.RowOneSlotSix, itemStack(Material.ORANGE_DYE) {
                    meta { name = "${org.bukkit.ChatColor.GOLD}3.5 Herzen" }
                }) {
                    updatePlayerDamage(player, 7.0)
                }
                button(Slots.RowOneSlotSeven, itemStack(Material.RED_DYE) {
                    meta { name = "${org.bukkit.ChatColor.RED}4 Herzen" }
                }) {
                    updatePlayerDamage(player, 8.0)
                }
                button(Slots.RowOneSlotEight, itemStack(Material.PURPLE_DYE) {
                    meta { name = "${org.bukkit.ChatColor.DARK_PURPLE}4.5 Herzen" }
                }) {
                    updatePlayerDamage(player, 9.0)
                }
                nextPage(Slots.RowOneSlotNine, itemStack(Material.REPEATER) {
                    meta { name = "§rInconsistency settings" }
                })
            }

            page(1) {
                transitionFrom = PageChangeEffect.SLIDE_HORIZONTALLY
                transitionTo = PageChangeEffect.SLIDE_HORIZONTALLY

                previousPage(Slots.RowOneSlotOne, itemStack(Material.PAPER) {
                    meta { name = "§rzurück" }
                })

                button(Slots.RowOneSlotFour, itemStack(Material.GREEN_DYE) {
                    meta { name = "§2Mindest-Schaden: ${player.inconsistencyRange.first.toDouble() / 2} Herzen" }
                }) {
                    val oldRange = player.inconsistencyRange

                    if (it.bukkitEvent.isLeftClick)
                        player.inconsistencyRange =
                            player.inconsistencyRange.first - 1 to player.inconsistencyRange.second
                    else
                        player.inconsistencyRange =
                            player.inconsistencyRange.first + 1 to player.inconsistencyRange.second

                    validate(player, oldRange)
                    it.bukkitEvent.currentItem?.meta {
                        name = "§2Mindest-Schaden: ${player.inconsistencyRange.first.toDouble() / 2} Herzen"
                    }
                }
                button(Slots.RowOneSlotSix, itemStack(Material.RED_DYE) {
                    meta { name = "§4Maximal-Schaden: ${player.inconsistencyRange.second.toDouble() / 2} Herzen" }
                }) {
                    val oldRange = player.inconsistencyRange
                    if (it.bukkitEvent.isLeftClick)
                        player.inconsistencyRange =
                            player.inconsistencyRange.first to player.inconsistencyRange.second - 1
                    else
                        player.inconsistencyRange =
                            player.inconsistencyRange.first to player.inconsistencyRange.second + 1

                    validate(player, oldRange)
                    it.bukkitEvent.currentItem?.meta {
                        name = "§4Maximal-Schaden: ${player.inconsistencyRange.second.toDouble() / 2} Herzen"
                    }
                }
                lateinit var compound: GUIRectSpaceCompound<*, Boolean>
                compound = createRectCompound(
                    Slots.RowOneSlotNine,
                    Slots.RowOneSlotNine,
                    iconGenerator = {
                        itemStack(Material.REDSTONE_BLOCK) {
                            val status = if (it) "${KColors.GREEN}enabled" else "${KColors.RED}disabled"
                            meta { name = "§rInconsistency Damager: $status" }
                        }
                    },
                    onClick = { e, b ->
                        updateInconsistencyDamagerInfo(player, e.bukkitEvent.currentItem)
                        compound.setContent(!b)
                        e.bukkitEvent.isCancelled = true
                    }
                )
                compound.addContent(player.inconsistencyEnabled)
            }
        }
    }

    private fun validate(player: Player, oldRange: Pair<Int, Int>) {
        val range = player.inconsistencyRange
        if (range.first < 1 ||
            range.second < 2 ||
            range.first > 18 ||
            range.second > 19 ||
            range.first >= range.second ||
            range.second <= range.first ||
            range.first <= 0 ||
            range.second >= 20
        )
            player.inconsistencyRange = oldRange
    }

    fun getCrapDamagerItem(): ItemStack {
        val item = crapItems.random()
        if (item == Material.STONE_SWORD || item == Material.MUSHROOM_STEW) return ItemStack(item, 1)
        return ItemStack(item, Random.nextInt(1, 22))
    }

    var Player.inconsistencyEnabled: Boolean
        get() {
            return this.name in inconsistencyPlayers
        }
        set(value) {
            if (value) inconsistencyPlayers.add(this.name)
            else inconsistencyPlayers.remove(this.name)
        }

    var Player.crapDamagerEnabled: Boolean
        get() {
            return this.name in crapDamagerPlayers
        }
        set(value) {
            if (value) crapDamagerPlayers.add(this.name)
            else crapDamagerPlayers.remove(this.name)
        }

    private var Player.inconsistencyRange: Pair<Int, Int>
        get() {
            if (this.name !in inconsistencyRanges) inconsistencyRanges[this.name] = 4 to 7
            return inconsistencyRanges[this.name] ?: 4 to 7
        }
        set(value) {
            inconsistencyRanges[this.name] = value
        }

    private fun updatePlayerDamage(player: Player, damage: Double) {
        Damager.playerDamage[player.name] = damage
        player.sendMessage("$LOBBY_PREFIX Der Damager Schaden beträgt nun ${Damager.playerDamage[player.name]?.div(2)} Herzen")
        if (player.name in inconsistencyPlayers) inconsistencyPlayers.remove(player.name)
    }

    private fun updateCrapDamagerInfo(player: Player, item: ItemStack?) {
        player.crapDamagerEnabled = !player.crapDamagerEnabled
        val status = if (player.crapDamagerEnabled) "${KColors.GREEN}enabled" else "${KColors.RED}disabled"
        item?.meta { name = "${KColors.GRAY}Crap Damager: $status" }
        player.sendMessage("$LOBBY_PREFIX Der Crap Damager ist nun $status")
    }

    private fun updateInconsistencyDamagerInfo(player: Player, item: ItemStack?) {
        player.inconsistencyEnabled = !player.inconsistencyEnabled
        val status = if (player.inconsistencyEnabled) "${KColors.GREEN}enabled" else "${KColors.RED}disabled"
        item?.meta { name = "§rInconsistency Damager: $status" }
        player.sendMessage("$LOBBY_PREFIX Der Inconsistency Damager ist nun $status")
    }

}