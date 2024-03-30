package contractsfabricated;

import contractsfabricated.mixin.ItemStackAccessor;
import contractsfabricated.util.ContractsUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Deal {

    public static void claimBook(PlayerEntity player, ItemStack stack) {
        if (!ContractsUtil.isMoriarty(player)) return;
        if (!stack.isOf(Items.WRITABLE_BOOK)) return;
        if (!player.isSneaking()) return;

        stack.addHideFlag(ItemStack.TooltipSection.ENCHANTMENTS);
    }

    public static void signDeal(PlayerEntity player, ItemStack stack) {
        if (stack == null) return;
        ItemStackAccessor accessor = ((ItemStackAccessor)(Object) stack);
        int flags = accessor.callGetHideFlags();
        if (!ItemStackAccessor.callIsSectionVisible(flags, ItemStack.TooltipSection.ENCHANTMENTS)) return;


    }
}
