package contractsfabricated.util.network.packets;

import com.google.common.collect.ImmutableMap;
import contractsfabricated.util.network.util.AbstractC2SPacket;
import contractsfabricated.util.network.util.AbstractPacketManager;
import net.minecraft.util.Identifier;

public class CPacketManager extends AbstractPacketManager {

    public static final CPacketManager INSTANCE = new CPacketManager();

    @Override
    public void registerC2S(ImmutableMap.Builder<Identifier, AbstractC2SPacket.C2SHandler> builder) {
        builder.put(MoriartySpecialC2S.ID, MoriartySpecialC2S::receive);
        builder.put(MoriartyInvisibleC2S.ID, MoriartyInvisibleC2S::receive);
        builder.put(MoriartyTpBondC2S.ID, MoriartyTpBondC2S::receive);
        builder.put(MoriartyTpToBondC2S.ID, MoriartyTpToBondC2S::receive);
        builder.put(MoriartyStanBondC2S.ID, MoriartyStanBondC2S::receive);
    }
}
