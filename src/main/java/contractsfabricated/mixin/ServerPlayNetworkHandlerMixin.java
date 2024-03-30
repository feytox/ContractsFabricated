package contractsfabricated.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import contractsfabricated.entrypoints.ContractsFabricated;
import contractsfabricated.Deal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.filter.FilteredMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Inject(method = "addBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    private void hookBookSigning(FilteredMessage title, List<FilteredMessage> pages, int slotId, CallbackInfo ci, @Local(ordinal = 1) ItemStack stack) {
        if (!stack.isOf(Items.WRITABLE_BOOK)) {
            ContractsFabricated.LOGGER.error("Failed to hook book signing because the book is not equal to a writable book");
            return;
        }

        ServerPlayNetworkHandler it = ((ServerPlayNetworkHandler)(Object) this);
        Deal.signDeal(it.player, stack);
    }
}
