package contractsfabricated.entrypoints;

import contractsfabricated.util.CKeybindings;
import net.fabricmc.api.ClientModInitializer;

public class ContractsFabricatedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CKeybindings.registerAll();
    }
}
