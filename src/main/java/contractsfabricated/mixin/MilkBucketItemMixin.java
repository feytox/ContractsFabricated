package contractsfabricated.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import contractsfabricated.Bond;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.MilkBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {

    @WrapWithCondition(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
    private boolean hookMilkUsing(LivingEntity instance) {
        return !Bond.cancelMilk(instance);
    }
}
