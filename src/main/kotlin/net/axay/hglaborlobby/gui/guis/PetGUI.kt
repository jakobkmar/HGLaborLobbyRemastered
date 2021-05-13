package net.axay.hglaborlobby.gui.guis

import net.axay.hglaborlobby.pathfinding.LaborPathfinderMoveToPlayer
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.*
import net.minecraft.server.v1_16_R3.EntityInsentient
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat
import net.minecraft.server.v1_16_R3.PathfinderGoalSelector
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity
import org.bukkit.entity.*
import net.axay.kspigot.gui.*
import net.axay.kspigot.utils.mark
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object PetGUI {

    data class Pet(val name: String, val entity: EntityType, val icon: Material) {

        private lateinit var petEntity: Entity

        fun despawn() {
            petEntity.eject()
            petEntity.remove()
        }

        fun ride(player: Player) {
            petEntity.passengers.add(player)
        }

        fun spawn(owner: Player) {
            val currentPetEntity = owner.world.spawnEntity(owner.location, entity)
            petEntity = currentPetEntity
            currentPetEntity.isInvulnerable = true
            currentPetEntity.customName = "${KColors.CORNFLOWERBLUE}${owner.name}'s $name"
            currentPetEntity.isCustomNameVisible = true
            if (currentPetEntity is Mob) {
                try {
                    val craftMonster = (currentPetEntity as CraftEntity).handle as EntityInsentient
                    clearPathfinders(craftMonster)
                    currentPetEntity.target = null
                    craftMonster.goalSelector.a(0, LaborPathfinderMoveToPlayer(owner, craftMonster))
                    craftMonster.goalSelector.a(1, PathfinderGoalFloat(craftMonster))
                } catch (e: NoClassDefFoundError) {
                }
            }
            if (currentPetEntity is Tameable) {
                currentPetEntity.owner = owner
                currentPetEntity.isTamed = true
            }
            if (currentPetEntity is AbstractHorse) {
                currentPetEntity.addPassenger(owner)
                if(currentPetEntity !is Llama) currentPetEntity.inventory.saddle = ItemStack(Material.SADDLE)
            }
            if (currentPetEntity is Strider) {
                owner.inventory.setItemInOffHand(itemStack(Material.WARPED_FUNGUS_ON_A_STICK) {
                    meta {
                        isUnbreakable = true
                        mark("lobbyitem")
                    }
                })
                currentPetEntity.isShivering = false
                currentPetEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.let { attribute -> attribute.baseValue = 0.275 }
                currentPetEntity.addPassenger(owner)
            }
            if (currentPetEntity is Llama) {
                currentPetEntity.color = Llama.Color.values().random()
                currentPetEntity.inventory.saddle = ItemStack(Material.PINK_CARPET)
            }
            if (currentPetEntity is Wolf) {
                currentPetEntity.collarColor = DyeColor.values().random()
            }
            if (currentPetEntity is Cat) {
                currentPetEntity.catType = Cat.Type.RED
            }
        }
        private fun clearPathfinders(entity: EntityInsentient) {
            entity.goalSelector = PathfinderGoalSelector(entity.getWorld().methodProfilerSupplier)
            entity.targetSelector = PathfinderGoalSelector(entity.getWorld().methodProfilerSupplier)
        }
    }

    var Player.pet: Pet?
        get() = pets[this]
        set(value) {
            pets[this] = value!!
        }

    private val pets = hashMapOf<Player, Pet>()

    private fun petsGui(player: Player): GUI<ForInventoryFourByNine> {
        return kSpigotGUI(GUIType.FOUR_BY_NINE) {
            title = "${KColors.BLACK}PETS"
            page(1) {
                placeholder(Slots.Border, itemStack(Material.WHITE_STAINED_GLASS_PANE) { meta { name = null } })
                val petCompound = createRectCompound<Pet>(
                    Slots.RowTwoSlotTwo,
                    Slots.RowThreeSlotEight,
                    iconGenerator = {
                        itemStack(it.icon) {
                            setMeta {
                                name = "${KColors.CORAL}${it.name}"
                                if(player.pet != null) {
                                    if(player.pet?.entity == it.entity) {
                                        addEnchant(Enchantment.DAMAGE_ALL, 1, true)
                                        addItemFlags(ItemFlag.HIDE_ENCHANTS)
                                    }
                                }
                                addLore {
                                    +"${KColors.BISQUE}Hole dir ein ${it.name} als treuen Begleiter,"
                                    +"${KColors.BISQUE}um dich in der Lobby aufzuhalten!"
                                    +" "
                                    +"${KColors.LIGHTSLATEGRAY}${KColors.ITALIC}Klicke auf dieses Item,"
                                    +"${KColors.LIGHTSLATEGRAY}${KColors.ITALIC}um das Pet zu beschwören."
                                }
                                addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                            }
                        }
                    },
                    onClick = { clickEvent, element ->
                        clickEvent.bukkitEvent.isCancelled = true
                        val player = clickEvent.player
                        player.closeInventory()
                        if (pets.containsKey(player) || player.pet != null) {
                            if (element.entity == player.pet?.entity) {
                                player.pet?.despawn()
                                pets.remove(player)
                            } else {
                                player.pet?.despawn()
                                element.spawn(player)
                                player.pet = element
                            }
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
                petCompound.addContent(Pet("Floppa", EntityType.CAT, Material.MUSIC_DISC_CAT))
                petCompound.addContent(Pet("Strider", EntityType.STRIDER, Material.WARPED_FUNGUS))

                button(Slots.CornerBottomRight, itemStack(Material.SADDLE) {
                    meta {
                        name = "${KColors.SADDLEBROWN}Haustier reiten"
                        addLore {
                            + "${KColors.BISQUE}Reite dein aktuell ausgewähltes Haustier"
                        }
                    }
                }, onClick = {
                    player.closeInventory()
                    if(player.pet == null) {
                        player.sendMessage("${KColors.TOMATO}Du hast aktuell kein Haustier ausgewählt!")
                    } else {
                        player.playSound(player.location, Sound.ENTITY_PIG_SADDLE, 1.0f, 1.0f)
                        player.pet?.ride(player)
                    }
                })
            }
        }
    }

    fun enable() {
        MainGUI.addContent(MainGUI.MainGUICompoundElement(
            Material.CARROT_ON_A_STICK,
            "Pets",
            "Hole dir einen treuen Begleiter.",
            onClick = { it.player.openGUI(petsGui(it.player)) }
        ))
    }
}