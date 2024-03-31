package contractsfabricated;

import contractsfabricated.util.ContractsUtil;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;

public class Debuff {

    public static boolean applyDebuff(LivingEntity player, ItemStack stack) {
        if (!ContractsUtil.isMoriarty(player) || !ContractsUtil.hasDeals()) return false;

        Item item = stack.getItem();
        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock) return false;
        if (stack.isOf(Items.CARVED_PUMPKIN) || stack.isOf(Items.TURTLE_HELMET)) return false;
        if (item instanceof ElytraItem) return false;

        return !stack.isOf(Items.GOLDEN_LEGGINGS) && !stack.isOf(Items.LEATHER_BOOTS);
    }
}
