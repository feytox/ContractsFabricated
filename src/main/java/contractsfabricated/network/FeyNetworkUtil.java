package contractsfabricated.network;

import com.google.common.collect.ImmutableMap;
import contractsfabricated.network.packets.CPacketManager;
import contractsfabricated.network.util.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class FeyNetworkUtil {

    public static void registerPackets() {
        registerPackets(CPacketManager.INSTANCE);
    }

    private static void registerPackets(AbstractPacketManager... packetManagers) {
        var s2cBuilder = new ImmutableMap.Builder<Identifier, AbstractS2CPacket.S2CHandler>();
        var c2sBuilder = new ImmutableMap.Builder<Identifier, AbstractC2SPacket.C2SHandler>();
        for (AbstractPacketManager packetManager : packetManagers) {
            packetManager.registerS2C(s2cBuilder);
            packetManager.registerC2S(c2sBuilder);
        }
        ImmutableMap<Identifier, AbstractS2CPacket.S2CHandler> s2cPackets = s2cBuilder.build();
        ImmutableMap<Identifier, AbstractC2SPacket.C2SHandler> c2sPackets = c2sBuilder.build();

        s2cPackets.forEach((id, handler) ->
                ClientPlayNetworking.registerGlobalReceiver(id, (client, handler1, buf, responseSender) ->
                        handler.receive(new S2CPacketInfo(client, handler1, buf, responseSender))));
        c2sPackets.forEach((id, handler) ->
                ServerPlayNetworking.registerGlobalReceiver(id, ((server, player, handler1, buf, responseSender) ->
                        handler.receive(new C2SPacketInfo(server, player, handler1, buf, responseSender)))));
    }
}
