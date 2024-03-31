package contractsfabricated.network.packets;

import contractsfabricated.item.CaneItem;
import contractsfabricated.network.util.AbstractC2SPacket;
import contractsfabricated.network.util.C2SPacketInfo;
import contractsfabricated.util.CIdentifier;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MoriartySpecialC2S extends AbstractC2SPacket {

    public static final Identifier ID = new CIdentifier("moriarty_special_c2s");

    public static void receive(C2SPacketInfo packetInfo) {
        ServerPlayerEntity player = packetInfo.player();

        packetInfo.server().execute(() -> CaneItem.activateSpecial(player));
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
