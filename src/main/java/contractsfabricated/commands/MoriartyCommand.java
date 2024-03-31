package contractsfabricated.commands;

import contractsfabricated.Bond;
import contractsfabricated.util.ContractsUtil;
import lombok.val;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import static contractsfabricated.entrypoints.ContractsFabricated.CONFIG;
import static net.minecraft.server.command.CommandManager.literal;

public class MoriartyCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(literal("moriarty")
                    .then(literal("markPos")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;
                                if (!ContractsUtil.isTpAllowed(player)) return 0;

                                CONFIG.worldId(player.getWorld().getRegistryKey().getValue());
                                CONFIG.markedPos(player.getPos());
                                player.sendMessage(Text.translatable("contractsfabricated.moriarty_command_markPos").formatted(Formatting.GREEN));

                                return 1;
                            }))
                    .then(literal("tp")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                if (player == null) return 0;
                                if (!ContractsUtil.isTpAllowed(player)) return 0;

                                Identifier worldId = CONFIG.worldId();
                                if (worldId == null) return 0;
                                val registryKey = RegistryKey.of(RegistryKeys.WORLD, worldId);
                                MinecraftServer server = player.getWorld().getServer();
                                if (server == null) return 0;

                                ServerWorld targetWorld = server.getWorld(registryKey);
                                Vec3d pos = CONFIG.markedPos();

                                player.teleport(targetWorld, pos.x, pos.y, pos.z, player.getYaw(), player.getPitch());
                                Bond.spawnTpParticles(player);
                                Bond.playSound(player, SoundEvents.BLOCK_LAVA_EXTINGUISH);

                                return 1;
                            }))));
    }
}
