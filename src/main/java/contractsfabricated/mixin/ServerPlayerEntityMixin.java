package contractsfabricated.mixin;

import contractsfabricated.util.BattleModeProdiver;
import contractsfabricated.util.ContractsUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements BattleModeProdiver {

    @Unique
    private boolean battleMode = false;

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void cancelCaneDeathDrop(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if (alive) return;
        if (!ContractsUtil.isMoriarty(oldPlayer)) return;

        ((ServerPlayerEntity)(Object) this).getInventory().clone(oldPlayer.getInventory());
    }

    @Override
    public void contractsFabricated$setBattleMode(boolean mode) {
        battleMode = mode;
    }

    @Override
    public boolean contractsFabricated$getBattleMode() {
        return battleMode;
    }
}
