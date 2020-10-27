package net.axay.hglaborlobby.gui

import net.axay.hglaborlobby.data.Warp
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import net.axay.kspigot.items.setMeta
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object WarpGUI {

    val gui = kSpigotGUI(GUIType.THREE_BY_NINE) {

        page(1) {

            placeholder(Slots.Border, ItemStack(Material.PINK_STAINED_GLASS_PANE))

            val warpsCompound = createCompound<Warp>(

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