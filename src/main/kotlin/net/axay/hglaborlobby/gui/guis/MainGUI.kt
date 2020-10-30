package net.axay.hglaborlobby.gui.guis

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.*
import net.axay.kspigot.gui.elements.GUICompoundElement
import net.axay.kspigot.gui.elements.GUIRectSpaceCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.toLoreList
import net.axay.kspigot.utils.hasMark
import org.bukkit.Material
import org.bukkit.event.player.PlayerInteractEvent

object MainGUI {

    private var menuCompound: GUIRectSpaceCompound<ForInventorySixByNine, GUICompoundElement<ForInventorySixByNine>>? =
        null

    private val gui = kSpigotGUI(GUIType.SIX_BY_NINE) {

        title = "${KColors.FIREBRICK}HAUPTMENÜ"

        page(1) {

            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })

            menuCompound = createSimpleRectCompound(Slots.RowTwoSlotTwo, Slots.RowFiveSlotEight)

        }

    }

    class MainGUICompoundElement(
        material: Material,
        name: String,
        description: String,
        onClick: ((GUIClickEvent<ForInventorySixByNine>) -> Unit)? = null
    ) : GUICompoundElement<ForInventorySixByNine>(

        itemStack(material) {
            meta {
                this.name = "${KColors.ORANGE}$name"
                lore = description.toLoreList(lineColor = KColors.CHARTREUSE)
            }
        },

        onClick

    )

    fun addContent(element: GUICompoundElement<ForInventorySixByNine>) {
        menuCompound?.addContent(element)
    }

    fun enable() {

        listen<PlayerInteractEvent> {

            if (it.item?.hasMark("maingui") == true) {
                it.isCancelled = true
                it.player.openGUI(gui)
            }

        }

    }

}