package contractsfabricated.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemStack.class)
public interface ItemStackAccessor {
    @Invoker
    static boolean callIsSectionVisible(int flags, ItemStack.TooltipSection tooltipSection) {
        throw new UnsupportedOperationException();
    }

    @Invoker
    int callGetHideFlags();
}
