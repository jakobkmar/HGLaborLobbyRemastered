package net.axay.hglaborlobby.pathfinding

import net.minecraft.server.v1_16_R3.EntityInsentient
import net.minecraft.server.v1_16_R3.PathfinderGoal
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.entity.Player

class LaborPathfinderMoveToPlayer(private val player: Player?, mob: EntityInsentient?) : PathfinderGoal() {
    private val mob: EntityInsentient? = mob
    override fun a(): Boolean {
        return player != null && !player.isDead
    }

    // e() executes whenever a() returns true
    override fun e() {
        if (player == null || player.isDead) {
            return
        }
        if (mob == null) {
            return
        }
        if (mob.bukkitEntity.location.distanceSquared(player.location) >= 1200) {
            mob.bukkitEntity.teleport(player.location.add(0.0, 1.0, 0.0))
            mob.goalTarget = null
            return
        }
        if (mob.goalTarget != null) {
            return
        }
        if (mob.bukkitEntity.location.distanceSquared(player.location) >= 24.0) {
            mob.navigation.a(player.location.x, player.location.y + 1.0, player.location.z, 1.5)
            mob.controllerLook.a((player as CraftPlayer).handle, 10.0f, 0.0f)
        }
    }

    override fun b(): Boolean {
        return false
    }

}