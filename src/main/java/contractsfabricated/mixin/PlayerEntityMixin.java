package contractsfabricated.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import contractsfabricated.Bond;
import contractsfabricated.Deal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
    private void injectBookClaiming(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEntity player = ((PlayerEntity)(Object) this);
        Deal.claimBook(player, stack);
    }

    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void injectBookUnbind(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEntity player = ((PlayerEntity)(Object) this);
        if (Deal.unbindDeal(player, stack)) cir.setReturnValue(null);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void hookPlayerTick(CallbackInfo ci) {
        PlayerEntity player = ((PlayerEntity)(Object) this);
        Deal.markUnlucky(player);
        Bond.tickLuck(player);
    }

    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    private boolean cancelDamageByBond(boolean original, @Local(argsOnly = true) DamageSource source) {
        if (original) return true;

        PlayerEntity player = ((PlayerEntity)(Object) this);
        return Bond.cancelDamage(player, source);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void injectDamageModification(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) LocalFloatRef damageRef) {
        PlayerEntity target = ((PlayerEntity)(Object) this);
        Entity attacker = source.getAttacker();
        float newDamage = Bond.modifyDamage(target, attacker, amount);
        if (newDamage == amount) return;
        damageRef.set(newDamage);
    }
}
