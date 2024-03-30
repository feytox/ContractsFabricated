package contractsfabricated.util;

import contractsfabricated.entrypoints.ContractsFabricated;
import contractsfabricated.util.network.packets.MoriartyInvisibleC2S;
import contractsfabricated.util.network.packets.MoriartySpecialC2S;
import lombok.val;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class CKeybindings {

    private static final KeyBinding MORIARTY_INVISIBLE_TOGGLE = register("moriarty_invisible_toggle", InputUtil.GLFW_KEY_H);
    private static final KeyBinding MORIARTY_SPECIAL_ACTIVATE = register("moriarty_special_activate", InputUtil.GLFW_KEY_J);

    public static void registerAll() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (MORIARTY_INVISIBLE_TOGGLE.wasPressed()) {
                val packet = new MoriartyInvisibleC2S();
                packet.sendToServer();
            }

            while (MORIARTY_SPECIAL_ACTIVATE.wasPressed()) {
                val packet = new MoriartySpecialC2S();
                packet.sendToServer();
            }
        });
    }

    private static KeyBinding register(String key, int code) {
        KeyBinding keyBinding = new KeyBinding(ContractsFabricated.MOD_ID + "." + key, code, ContractsFabricated.MOD_ID + ".category");
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }
}
