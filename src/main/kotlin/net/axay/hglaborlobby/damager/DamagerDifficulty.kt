package net.axay.hglaborlobby.damager

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

object DamagerDifficulty {

    private var inconsistencyPlayers = mutableSetOf<String>()
    var inconsistencyRanges = mutableMapOf<String, Pair<Int, Int>>()

    var Player.inconsistencyEnabled: Boolean
        get() {
            return this.name in inconsistencyPlayers
        }
        set(value) {
            if (value) inconsistencyPlayers.add(this.name)
            else inconsistencyPlayers.remove(this.name)
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
        player.sendMessage("§7[§bHG§7Labor] Der Damager Schaden beträgt nun ${Damager.playerDamage[player.name]?.div(2)} Herzen")
        if (player.name in inconsistencyPlayers) inconsistencyPlayers.remove(player.name)
    }

    private fun buildDamageGUI(player: Player): GUI<ForInventoryOneByNine> {
        return kSpigotGUI(GUIType.ONE_BY_NINE) {
            title = "${KColors.INDIANRED} Difficulty"

            page(0) {

                transitionFrom = PageChangeEffect.SLIDE_HORIZONTALLY
                transitionTo = PageChangeEffect.SLIDE_HORIZONTALLY

                placeholder(Slots.RowOneSlotOne, ItemStack(Material.AIR))
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
                    meta { name = "Inconsistency settings" }
                })
            }


            page(1) {
                transitionFrom = PageChangeEffect.SLIDE_HORIZONTALLY
                transitionTo = PageChangeEffect.SLIDE_HORIZONTALLY

                previousPage(Slots.RowOneSlotOne, itemStack(Material.PAPER) {
                    meta { name = "zurück" }
                })

                button(Slots.RowOneSlotFour, itemStack(Material.GREEN_DYE) {
                    meta { name = "§2Mindest-Schaden: ${player.inconsistencyRange.first.toDouble() / 2} Herzen" }
                }) {
                    val oldRange = player.inconsistencyRange

                    if (it.bukkitEvent.isLeftClick)
                        player.inconsistencyRange = player.inconsistencyRange.first - 1 to player.inconsistencyRange.second
                    else
                        player.inconsistencyRange = player.inconsistencyRange.first + 1 to player.inconsistencyRange.second

                    validate(player, oldRange)
                    it.bukkitEvent.currentItem?.meta { name = "§2Mindest-Schaden: ${player.inconsistencyRange.first.toDouble() / 2} Herzen" }
                }
                button(Slots.RowOneSlotSix, itemStack(Material.RED_DYE) {
                    meta { name = "§4Maximal-Schaden: ${player.inconsistencyRange.second.toDouble() / 2} Herzen" }
                }) {
                    val oldRange = player.inconsistencyRange
                    if (it.bukkitEvent.isLeftClick)
                        player.inconsistencyRange = player.inconsistencyRange.first to player.inconsistencyRange.second - 1
                    else
                        player.inconsistencyRange = player.inconsistencyRange.first to player.inconsistencyRange.second + 1

                    validate(player, oldRange)
                    it.bukkitEvent.currentItem?.meta { name = "§4Maximal-Schaden: ${player.inconsistencyRange.second.toDouble() / 2} Herzen" }
                }
                button(Slots.RowOneSlotNine, itemStack(Material.REDSTONE_BLOCK) {
                    meta { name = "Inconsistency enabled: ${player.inconsistencyEnabled}" }
                }) {
                    player.inconsistencyEnabled = !player.inconsistencyEnabled
                    it.bukkitEvent.currentItem?.meta { name = "Inconsistency enabled: ${player.inconsistencyEnabled}" }
                }
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

}