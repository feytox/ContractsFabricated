package contractsfabricated.item;

import contractsfabricated.entrypoints.ContractsFabricated;
import contractsfabricated.util.CIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CItems {

    public static final CaneItem CANE_BLADE = (CaneItem) register("cane_blade", new CaneItem.CaneBlade());
    public static final CaneItem CANE_STAFF = (CaneItem) register("cane_staff", new CaneItem.CaneStaff());

    public static void registerAll() {}

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, new CIdentifier(id), item);
    }
}
