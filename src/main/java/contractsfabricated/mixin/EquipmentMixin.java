package contractsfabricated.mixin;

import contractsfabricated.Debuff;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Equipment.class)
public interface EquipmentMixin {

    @Inject(method = "equipAndSwap", at = @At("HEAD"), cancellable = true)
    private void injectArmorDebuff(Item item, World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (world.isClient) return;
        ItemStack stack = user.getStackInHand(hand);
        if (Debuff.applyDebuff(user, stack)) cir.setReturnValue(TypedActionResult.fail(stack));
    }
}
