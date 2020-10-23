package net.axay.hglaborlobby.gui

import net.axay.hglaborlobby.database.data.DatabaseWarp
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.inventory.InventoryType
import net.axay.kspigot.inventory.Slots
import net.axay.kspigot.inventory.kSpigotGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import net.axay.kspigot.items.setMeta
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object WarpGUI {

    val gui = kSpigotGUI(InventoryType.THREE_BY_NINE) {

        page(1) {

            placeholder(Slots.Border, ItemStack(Material.PINK_STAINED_GLASS_PANE))

            val warpsCompound = createCompound<DatabaseWarp>(

                Slots.RowTwoSlotTwo,
                Slots.RowTwoSlotEight,

                iconGenerator = {
                    itemStack(it.icon) {
                        setMeta {
                            name = "${KColors.CORAL}${it.name}"
                            setLore {
                                +"${KColors.DARKAQUA}${it.description}"
                            }
                        }
                    }
                },

                onClick = { clickEvent, element ->

                    clickEvent.bukkitEvent.isCancelled = true

                    clickEvent.bukkitEvent.whoClicked.teleport(element.location.toSpigot())

                }

            )

        }

    }



}