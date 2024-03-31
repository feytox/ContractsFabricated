package contractsfabricated.util.network.packets;

import contractsfabricated.item.CaneItem;
import contractsfabricated.util.CIdentifier;
import contractsfabricated.util.ContractsUtil;
import contractsfabricated.util.network.util.AbstractC2SPacket;
import contractsfabricated.util.network.util.C2SPacketInfo;
import lombok.val;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MoriartyInvisibleC2S extends AbstractC2SPacket {

    public static final Identifier ID = new CIdentifier("moriarty_invisible_c2s");

    public static void receive(C2SPacketInfo packetInfo) {
        ServerPlayerEntity player = packetInfo.player();

        packetInfo.server().execute(() -> {
            if (!ContractsUtil.isMoriarty(player) || !ContractsUtil.hasDeals()) return;
            if (!CaneItem.isUsing(player)) return;

            val effect = player.getStatusEffect(StatusEffects.INVISIBILITY);
            if (effect != null) {
                player.removeStatusEffect(StatusEffects.INVISIBILITY);
                return;
            }

            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 0, false, false, true));
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
