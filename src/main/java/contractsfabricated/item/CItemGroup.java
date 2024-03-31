package contractsfabricated.item;

import contractsfabricated.util.CIdentifier;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

public class CItemGroup {

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(CItems.CANE_STAFF))
            .entries((context, entries) -> {
                entries.add(CItems.CANE_STAFF);
                entries.add(CItems.CANE_BLADE);
            })
            .displayName(Text.translatable("contractsfabricated.category"))
            .build();

    public static void registerAll() {
        Registry.register(Registries.ITEM_GROUP, new CIdentifier("item_group"), ITEM_GROUP);
    }
}
