package contractsfabricated.mixin;

import contractsfabricated.item.CaneItem;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onKilledBy", at = @At("HEAD"))
    private void injectPointsIncrease(LivingEntity adversary, CallbackInfo ci) {
        LivingEntity entity = ((LivingEntity)(Object) this);
        CaneItem.incrementPoints(adversary, entity);
    }
}
