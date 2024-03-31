package contractsfabricated;

import contractsfabricated.util.ContractsUtil;
import contractsfabricated.util.DelayedTask;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public class Bond {

    public static boolean cancelDamage(PlayerEntity player, DamageSource source) {
        if (player.getWorld().isClient) return false;
        if (!ContractsUtil.isMoriarty(player)) return false;
        if (!ContractsUtil.isBond(source.getAttacker())) return false;
        return ContractsUtil.isBattleMode(player);
    }

    public static boolean cancelMilk(LivingEntity player) {
        return ContractsUtil.isBond(player);
    }

    public static float modifyDamage(PlayerEntity target, Entity attacker, float damage) {
        if (!ContractsUtil.isMoriarty(attacker)) return damage;
        return ContractsUtil.isBond(target) ? damage * 2 : damage;
    }

    public static void tickLuck(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!ContractsUtil.isBond(player)) return;
        if (player.getStatusEffect(StatusEffects.LUCK) != null) return;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, -1, 0, false, false));
    }

    public static void tpBond(ServerPlayerEntity player) {
        if (!ContractsUtil.isMoriarty(player)) return;

        ServerWorld world = player.getServerWorld();
        ItemStack stack = player.getMainHandStack();
        Pair<String, PlayerEntity> match = ContractsUtil.getBond(world, stack);
        PlayerEntity victim = match.right();
        if (victim == null) return;

        spawnTpParticles(victim);
        playSound(victim, SoundEvents.BLOCK_LAVA_EXTINGUISH);

        DelayedTask.schedule(() -> {
            teleport(victim, player);
            spawnTpParticles(victim);
            playSound(victim, SoundEvents.BLOCK_LAVA_EXTINGUISH);
            victim.removeStatusEffect(StatusEffects.JUMP_BOOST);
            victim.removeStatusEffect(StatusEffects.SLOWNESS);
            victim.removeStatusEffect(StatusEffects.MINING_FATIGUE);
            victim.removeStatusEffect(StatusEffects.WEAKNESS);
        }, 40);
    }

    public static void tpToBond(ServerPlayerEntity player) {
        if (!ContractsUtil.isMoriarty(player)) return;

        ServerWorld world = player.getServerWorld();
        ItemStack stack = player.getMainHandStack();
        Pair<String, PlayerEntity> match = ContractsUtil.getBond(world, stack);
        PlayerEntity victim = match.right();
        if (victim == null) return;

        spawnTpParticles(player);
        playSound(player, SoundEvents.BLOCK_LAVA_EXTINGUISH);

        DelayedTask.schedule(() -> {
            teleport(player, victim);
            spawnTpParticles(player);
            playSound(player, SoundEvents.BLOCK_LAVA_EXTINGUISH);
        }, 40);
    }

    public static void stanBond(ServerPlayerEntity player) {
        if (!ContractsUtil.isMoriarty(player)) return;

        ServerWorld world = player.getServerWorld();
        ItemStack stack = player.getMainHandStack();
        Pair<String, PlayerEntity> match = ContractsUtil.getBond(world, stack);
        PlayerEntity victim = match.right();
        if (victim == null) return;

        playSound(victim, SoundEvents.ENTITY_WITHER_AMBIENT);
        playSound(victim, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK);
        if (!(victim.getWorld() instanceof ServerWorld victimWorld)) return;

        Vec3d pos = victim.getPos();
        victimWorld.spawnParticles(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 300, 0, 1, 0, 1);
        if (victim.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            victim.removeStatusEffect(StatusEffects.JUMP_BOOST);
            victim.removeStatusEffect(StatusEffects.SLOWNESS);
            victim.removeStatusEffect(StatusEffects.MINING_FATIGUE);
            victim.removeStatusEffect(StatusEffects.WEAKNESS);
        } else {
            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, -1, 128, false, false));
            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, -1, 255, false, false));
            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, -1, 255, false, false));
            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, -1, 255, false, false));
        }
    }

    private static void teleport(PlayerEntity player, PlayerEntity target) {
        if (!(target.getWorld() instanceof ServerWorld targetWorld)) return;
        player.teleport(targetWorld, target.getX(), target.getY(), target.getZ(), Set.of(), target.getYaw(), target.getPitch());
    }

    public static void spawnTpParticles(PlayerEntity target) {
        if (!(target.getWorld() instanceof ServerWorld world)) return;

        Vec3d pos = target.getPos();
        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, 300, 0, 1, 0, 1);
        world.spawnParticles(ParticleTypes.SOUL, pos.x, pos.y, pos.z, 300, 0, 1, 0, 1);
        world.spawnParticles(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 300, 0, 1, 0, 1);
    }

    public static void playSound(PlayerEntity target, SoundEvent soundEvent) {
        if (!(target.getWorld() instanceof ServerWorld world)) return;
        world.playSound(null, target.getX(), target.getY(), target.getZ(), soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }
}
