package net.axay.hglaborlobby.gui.guis

import net.axay.kspigot.chat.KColors
import net.minecraft.server.v1_16_R3.EntityInsentient
import net.minecraft.server.v1_16_R3.PathfinderGoalSelector
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity
import org.bukkit.entity.*
import net.axay.hglaborlobby.pathfinding.LaborPathfinderMoveToPlayer
import net.axay.kspigot.extensions.bukkit.*
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.*
import net.axay.kspigot.runnables.task
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat
import org.bukkit.Sound
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack


object PetGUI {

    data class Pet(val name: String, val entity: EntityType, val icon: Material) {

        lateinit var petEntity: Entity

        fun despawn() {
            petEntity.remove()
        }

        fun spawn(owner: Player) {
            petEntity = owner.world.spawnEntity(owner.location, entity)
            petEntity.isInvulnerable = true
            petEntity.customName = "${KColors.CORNFLOWERBLUE}${owner.name}'s $name"
            petEntity.isCustomNameVisible = true
            if(petEntity is Mob) {
                val craftMonster = (petEntity as CraftEntity).handle as EntityInsentient
                clearPathfinders(craftMonster)
                (petEntity as Mob).target = null
                craftMonster.goalSelector.a(0, LaborPathfinderMoveToPlayer(owner, craftMonster))
                craftMonster.goalSelector.a(1, PathfinderGoalFloat(craftMonster));
            }
            if(petEntity is AbstractHorse) {
                petEntity.addPassenger(owner)
            }
            if(petEntity is Tameable) {
                (petEntity as Tameable).owner = owner
                (petEntity as Tameable).isTamed = true
            }
            if(petEntity is Llama) {
                (petEntity as Llama).color = Llama.Color.values().random()
            }
            if(petEntity is Wolf) {
                (petEntity as Wolf).collarColor = DyeColor.values().random()
            }
        }

        private fun clearPathfinders(entity: EntityInsentient) {
            entity.goalSelector = PathfinderGoalSelector(entity.getWorld().methodProfilerSupplier)
            entity.targetSelector = PathfinderGoalSelector(entity.getWorld().methodProfilerSupplier)
        }

    }

    var Player.pet: Pet?
    get() = pets[player]
    set(value) {
        pets[player!!] = value!!
    }

    private val pets = hashMapOf<Player, Pet>()

    private val petsGui = kSpigotGUI(GUIType.THREE_BY_NINE) {

        title = "${KColors.BLACK}PETS"

        page(1) {
            placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
            val petCompound = createRectCompound<Pet>(
                Slots.RowTwoSlotTwo,
                Slots.RowTwoSlotEight,
                iconGenerator = {
                    itemStack(it.icon) {
                        setMeta {
                            name = "${KColors.CORAL}${it.name}"
                            addLore {
                                + "${KColors.BISQUE}Hole dir ein ${it.name} als treuen Begleiter,"
                                + "${KColors.BISQUE}um dich in der Lobby aufzuhalten!"
                                + " "
                                + "${KColors.LIGHTSLATEGRAY}${KColors.ITALIC}Klicke auf dieses Item,"
                                + "${KColors.LIGHTSLATEGRAY}${KColors.ITALIC}um das Pet zu beschwören."
                            }
                        }
                    }
                },
                onClick = {clickEvent, element ->
                    clickEvent.bukkitEvent.isCancelled = true
                    val player = clickEvent.player
                    player.closeInventory()
                    if(pets.containsKey(player) || player.pet != null) {
                        player.pet?.despawn()
                        player.pet = null
                    } else {
                        element.spawn(player)
                        player.pet = element
                    }
                }
            )
            petCompound.addContent(Pet("Esel", EntityType.DONKEY, Material.DONKEY_SPAWN_EGG))
            petCompound.addContent(Pet("Skelletpferd", EntityType.SKELETON_HORSE, Material.SKELETON_SKULL))
            petCompound.addContent(Pet("Lama", EntityType.LLAMA, Material.LLAMA_SPAWN_EGG))
            petCompound.addContent(Pet("Papagei", EntityType.PARROT, Material.PARROT_SPAWN_EGG))
            petCompound.addContent(Pet("Doggo", EntityType.WOLF, Material.BONE))
            petCompound.addContent(Pet("Schildkröte", EntityType.TURTLE, Material.TURTLE_HELMET))
            petCompound.addContent(Pet("Panda", EntityType.PANDA, Material.BAMBOO))
        }

    }

    fun enable() {

        MainGUI.addContent(MainGUI.MainGUICompoundElement(
            Material.CARROT_ON_A_STICK,
            "Pets",
            "Hole dir einen treuen Begleiter.",
            onClick = { it.player.openGUI(petsGui) }
        ))

    }



}