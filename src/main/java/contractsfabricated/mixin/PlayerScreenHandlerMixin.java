package contractsfabricated.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import contractsfabricated.Debuff;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/screen/PlayerScreenHandler$1")
public class PlayerScreenHandlerMixin {

    @ModifyReturnValue(method = "canInsert", at = @At("RETURN"))
    private boolean injectArmorDebuff(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (!original) return false;
        Slot slot = ((Slot)(Object) this);
        if (!(slot.inventory instanceof PlayerInventory playerInv)) return true;
        return !Debuff.applyDebuff(playerInv.player, stack);
    }
}
