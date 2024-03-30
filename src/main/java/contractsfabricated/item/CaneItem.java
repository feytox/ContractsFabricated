package contractsfabricated.item;

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
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static contractsfabricated.entrypoints.ContractsFabricated.CONFIG;

public class CaneItem extends SwordItem {

    private CaneItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed) {
        super(toolMaterial, attackDamage, attackSpeed, new FabricItemSettings().maxCount(1));
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
        if (world.getTime() % 5 != 0) return;
        if (!ContractsUtil.isMoriarty(entity) || !ContractsUtil.hasDeals()) return;

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
