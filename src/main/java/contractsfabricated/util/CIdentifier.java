package contractsfabricated.util;

import contractsfabricated.entrypoints.ContractsFabricated;
import net.minecraft.util.Identifier;

public class CIdentifier extends Identifier {

    public CIdentifier(String path) {
        super(ContractsFabricated.MOD_ID, path);
    }
}
