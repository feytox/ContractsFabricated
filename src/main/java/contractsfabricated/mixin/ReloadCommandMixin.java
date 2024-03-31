package contractsfabricated.mixin;

import contractsfabricated.entrypoints.ContractsFabricated;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(ReloadCommand.class)
public class ReloadCommandMixin {

    @Inject(method = "tryReloadDataPacks", at = @At("HEAD"))
    private static void injectConfigReload(Collection<String> dataPacks, ServerCommandSource source, CallbackInfo ci) {
        ContractsFabricated.reloadConfig();
    }
}
