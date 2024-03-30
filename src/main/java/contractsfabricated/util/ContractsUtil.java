package contractsfabricated.util;

import contractsfabricated.entrypoints.ContractsFabricated;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

import static contractsfabricated.entrypoints.ContractsFabricated.CONFIG;

public class ContractsUtil {
    public static boolean isMoriarty(@Nullable Entity player) {
        if (player == null) return false;
        return player.getName().getString().equals(CONFIG.moriarty());
    }

    public static boolean hasDeals() {
        return CONFIG.dealCount() >= 1;
    }
}
