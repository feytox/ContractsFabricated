package contractsfabricated.util.network.packets;

import contractsfabricated.item.CaneItem;
import contractsfabricated.util.CIdentifier;
import contractsfabricated.util.ContractsUtil;
import contractsfabricated.util.network.util.AbstractC2SPacket;
import contractsfabricated.util.network.util.C2SPacketInfo;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static contractsfabricated.entrypoints.ContractsFabricated.CONFIG;

public class MoriartySpecialC2S extends AbstractC2SPacket {

    public static final Identifier ID = new CIdentifier("moriarty_special_c2s");

    public static void receive(C2SPacketInfo packetInfo) {
        ServerPlayerEntity player = packetInfo.player();

        packetInfo.server().execute(() -> {
            if (!ContractsUtil.isMoriarty(player) || !ContractsUtil.hasDeals()) return;
            if (!CaneItem.isUsing(player)) return;

            int points = CONFIG.points();
            if (points < 45) {
                player.sendMessage(Text.translatable("contractsfabricated.moriarty_special_activate.fail").formatted(Formatting.GRAY), true);
                return;
            }

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 30*20, 5));
            CONFIG.points(0);
        });
    }

    @Override
    public PacketByteBuf encode(PacketByteBuf buf) {
        return buf;
    }

    @Override
    public Identifier getPacketID() {
        return ID;
    }
}
