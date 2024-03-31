package contractsfabricated.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import contractsfabricated.Deal;
import contractsfabricated.entrypoints.ContractsFabricated;
import net.minecraft.entity.player.PlayerInventory;
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
    private void hookBookSigning(FilteredMessage title, List<FilteredMessage> pages, int slotId, CallbackInfo ci, @Local(ordinal = 0) ItemStack oldStack, @Local(ordinal = 1) ItemStack newStack) {
        if (!oldStack.isOf(Items.WRITABLE_BOOK)) {
            ContractsFabricated.LOGGER.error("Failed to hook book signing because the book is not equal to a writable book");
            return;
        }

        ServerPlayNetworkHandler it = ((ServerPlayNetworkHandler)(Object) this);
        Deal.signDeal(it.player, oldStack, newStack);
    }

    @WrapOperation(method = "addBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setStack(ILnet/minecraft/item/ItemStack;)V"))
    private void hookBookSigning(PlayerInventory instance, int slot, ItemStack newStack, Operation<Void> original, @Local(ordinal = 0) ItemStack oldStack) {
        if (!oldStack.isOf(Items.WRITABLE_BOOK)) {
            ContractsFabricated.LOGGER.error("Failed to hook book signing because the book is not equal to a writable book");
            original.call(instance, slot, newStack);
            return;
        }

        if (Deal.signDeal(instance.player, oldStack, newStack)) newStack = ItemStack.EMPTY;
        original.call(instance, slot, newStack);
    }
}
