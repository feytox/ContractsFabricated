package contractsfabricated;

import contractsfabricated.mixin.ItemStackAccessor;
import contractsfabricated.util.ContractsUtil;
import contractsfabricated.util.DelayedTask;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static contractsfabricated.entrypoints.ContractsFabricated.CONFIG;

public class Deal {

    public static void claimBook(PlayerEntity player, ItemStack stack) {
        if (!ContractsUtil.isMoriarty(player)) return;
        if (!stack.isOf(Items.WRITABLE_BOOK)) return;
        if (!player.isSneaking()) return;

        stack.addHideFlag(ItemStack.TooltipSection.ENCHANTMENTS);
    }

    public static boolean signDeal(PlayerEntity player, ItemStack oldStack, ItemStack newStack) {
        if (oldStack == null || ContractsUtil.isBond(player)) return false;
        int flags = ((ItemStackAccessor)(Object) oldStack).callGetHideFlags();
        if (ItemStackAccessor.callIsSectionVisible(flags, ItemStack.TooltipSection.ENCHANTMENTS)) return false;

        List<String> bonded = CONFIG.bonded();
        bonded.add(player.getName().getString());
        CONFIG.bonded(bonded);
        CONFIG.dealCount(CONFIG.dealCount()+1);

        newStack.addEnchantment(Enchantments.LUCK_OF_THE_SEA, 10);
        newStack.setCustomName(Text.translatable("contractsfabricated.deal").formatted(Formatting.AQUA));
        DelayedTask.schedule(() -> spawnParticles(player, newStack), 40);
        return true;
    }

    private static void spawnParticles(PlayerEntity player, ItemStack bookStack) {
        if (player == null) return;
        World playerWorld = player.getWorld();
        if (!(playerWorld instanceof ServerWorld world)) return;
        Vec3d pos = player.getPos();

        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, 400, 0, 1, 0, 1);
        world.spawnParticles(ParticleTypes.SOUL, pos.x, pos.y, pos.z, 2100, 0, 1, 0, 1);
        world.spawnParticles(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 2400, 0, 1, 0, 1);

        world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.PLAYERS, 1.0f, 1.0f);
        world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, -1, 0, false, false, true));

        PlayerEntity moriarty = ContractsUtil.getMoriarty(world);
        if (moriarty == null || !(moriarty.getWorld() instanceof ServerWorld moriartyWorld)) return;
        Vec3d moriartyPos = moriarty.getPos();
        ItemScatterer.spawn(moriartyWorld, moriartyPos.x, moriartyPos.y, moriartyPos.z, bookStack);
    }

    public static boolean unbindDeal(PlayerEntity player, ItemStack dealStack) {
        if (!(player.getWorld() instanceof ServerWorld world)) return false;
        if (!ContractsUtil.isMoriarty(player)) return false;
        if (player.getStatusEffect(StatusEffects.UNLUCK) == null) return false;

        Pair<String, PlayerEntity> match = ContractsUtil.getBond(world, dealStack);
        String author = match.left();
        PlayerEntity victim = match.right();
        if (author == null) return false;

        List<String> bonded = CONFIG.bonded();
        bonded.remove(author);
        CONFIG.bonded(bonded);
        CONFIG.dealCount(CONFIG.dealCount()-1);
        dealStack.setCount(0);

        if (victim == null) return true;

        Vec3d pos = victim.getPos();
        victim.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 40, 1, false, false));
        victim.removeStatusEffect(StatusEffects.LUCK);
        if (!(victim.getWorld() instanceof ServerWorld victimWorld)) return true;

        victimWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, 300, 0, 1, 0, 1);
        victimWorld.spawnParticles(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 300, 0, 1, 0, 1);
        victimWorld.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1.0f, 1.0f);
        return true;
    }

    public static void markUnlucky(PlayerEntity player) {
        if (player.getWorld().isClient) return;
        if (!ContractsUtil.isMoriarty(player)) return;

        ItemStack stack = player.getOffHandStack();
        if (!stack.isOf(Items.WRITTEN_BOOK)) return;
        if (EnchantmentHelper.getLevel(Enchantments.LUCK_OF_THE_SEA, stack) != 10) return;
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, 100, 1, false, false));
    }
}
