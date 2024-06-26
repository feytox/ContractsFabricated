package contractsfabricated.item;

import contractsfabricated.util.BattleModeProdiver;
import contractsfabricated.util.ContractsUtil;
import lombok.val;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static contractsfabricated.entrypoints.ContractsFabricated.CONFIG;

public class CaneItem extends SwordItem {

    private CaneItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed) {
        super(toolMaterial, attackDamage, attackSpeed, new FabricItemSettings().maxCount(1).maxDamage(-1));
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return super.use(world, user, hand);

        ItemStack caneStack = user.getStackInHand(hand);
        CaneItem newItem = CItems.CANE_STAFF;
        if (caneStack.isOf(CItems.CANE_STAFF)) newItem = CItems.CANE_BLADE;

        ItemStack newStack = newItem.getDefaultStack();
        newStack.setNbt(caneStack.getNbt());
        user.setStackInHand(hand, newStack);

        return super.use(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient || !(entity instanceof LivingEntity livingEntity)) return;
        if (world.getTime() % 40 != 0) return;
        if (!ContractsUtil.isMoriarty(entity)) return;

        if (entity instanceof BattleModeProdiver provider) {
            boolean mode = stack.getItem() instanceof CaneBlade;
            provider.contractsFabricated$setBattleMode(mode);
        }

        if (!ContractsUtil.hasDeals()) return;
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 16*20, 1, false, false, true));
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 16*20, 1, false, false, true));
    }

    public static void incrementPoints(LivingEntity killer, LivingEntity target) {
        if (!ContractsUtil.isMoriarty(killer) || !ContractsUtil.hasDeals()) return;

        int change = 0;
        if (target instanceof MobEntity) change = 1;
        if (target instanceof PlayerEntity) change = 5;
        if (change == 0) return;

        CONFIG.points(CONFIG.points() + change);
        killer.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10*20));
    }

    public static boolean isUsing(LivingEntity entity) {
        val items = entity.getHandItems();
        for (ItemStack stack : items) {
            if (stack.getItem() instanceof CaneItem) return true;
        }
        return false;
    }

    public static void activateSpecial(ServerPlayerEntity player) {
        if (!ContractsUtil.isMoriarty(player) || !ContractsUtil.hasDeals()) return;
        if (!CaneItem.isUsing(player)) return;

        int points = CONFIG.points();
        if (points < 45) {
            player.sendMessage(Text.translatable("contractsfabricated.moriarty_special_activate.fail").formatted(Formatting.GRAY), true);
            return;
        }

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 30*20, 4, false, false));
        CONFIG.points(0);

        Vec3d pos = player.getPos();
        ServerWorld world = player.getServerWorld();

        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, 300, 0, 1, 0, 1);
        world.spawnParticles(ParticleTypes.SOUL, pos.x, pos.y, pos.z, 1200, 0, 1, 0, 1);
        world.spawnParticles(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 900, 0, 1, 0, 1);
        world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.PLAYERS, 1.0f, 1.0f);
        world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_WARDEN_HEARTBEAT, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    public static void toggleInvisibility(ServerPlayerEntity player) {
        if (!ContractsUtil.isMoriarty(player) || !ContractsUtil.hasDeals()) return;
        if (!CaneItem.isUsing(player)) return;

        val effect = player.getStatusEffect(StatusEffects.INVISIBILITY);
        if (effect != null) {
            player.removeStatusEffect(StatusEffects.INVISIBILITY);
            player.removeStatusEffect(StatusEffects.SPEED);
            player.removeStatusEffect(StatusEffects.HASTE);
            return;
        }

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 0, false, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, -1, 4, false, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, -1, 2, false, false, true));
    }

    public static void tickInvisibility(PlayerEntity player) {
        if (!ContractsUtil.isMoriarty(player)) return;
        if (!(player.getWorld() instanceof ServerWorld world)) return;
        if (player.getStatusEffect(StatusEffects.INVISIBILITY) == null) return;

        if (player.isOnFire()) {
            player.extinguish();
            player.setOnFire(false);
        }

        Vec3d pos = player.getPos();
        world.spawnParticles(ParticleTypes.SOUL, pos.x, pos.y, pos.z, 15, 0.5, 0, 0.5, 0);
    }

    public static class CaneStaff extends CaneItem {
        public CaneStaff() {
            super(ToolMaterials.WOOD, 3, -2.4F);
        }
    }

    public static class CaneBlade extends CaneItem {
        public CaneBlade() {
            super(ToolMaterials.NETHERITE, 3, -2.4F);
        }
    }
}
