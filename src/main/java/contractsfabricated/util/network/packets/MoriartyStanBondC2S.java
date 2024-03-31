package contractsfabricated.util.network.packets;

import contractsfabricated.Bond;
import contractsfabricated.util.CIdentifier;
import contractsfabricated.util.network.util.AbstractC2SPacket;
import contractsfabricated.util.network.util.C2SPacketInfo;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MoriartyStanBondC2S extends AbstractC2SPacket {

    public static final Identifier ID = new CIdentifier("moriarty_stan_bond_c2s");

    public static void receive(C2SPacketInfo packetInfo) {
        ServerPlayerEntity player = packetInfo.player();

        packetInfo.server().execute(() -> Bond.stanBond(player));
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
