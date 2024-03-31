package contractsfabricated.network.util;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public interface FeyPacket {
    PacketByteBuf encode(PacketByteBuf buf);
    Identifier getPacketID();
}
