package contractsfabricated.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import contractsfabricated.item.CaneItem;
import contractsfabricated.util.ContractsUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @ModifyExpressionValue(method = "dropAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private boolean cancelCaneDeathDrop(boolean original, @Local ItemStack stack) {
        if (original) return true;
        if (!(stack.getItem() instanceof CaneItem)) return false;

        PlayerEntity player = ((PlayerInventory)(Object) this).player;
        return ContractsUtil.isMoriarty(player);
    }
}
