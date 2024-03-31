package contractsfabricated.util;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import static contractsfabricated.entrypoints.ContractsFabricated.CONFIG;

public class ContractsUtil {
    public static boolean isMoriarty(@Nullable Entity entity) {
        return entity != null && entity.getName().getString().equals(CONFIG.moriarty());
    }

    public static boolean hasDeals() {
        return CONFIG.dealCount() >= 1;
    }

    public static boolean isBond(@Nullable Entity entity) {
        return entity != null && CONFIG.bonded().contains(entity.getName().getString());
    }

    public static boolean isBattleMode(@Nullable PlayerEntity player) {
        if (!(player instanceof BattleModeProdiver prodiver)) return false;
        return prodiver.contractsFabricated$getBattleMode();
    }

    @Nullable
    public static PlayerEntity getMoriarty(ServerWorld world) {
        return world.getServer().getPlayerManager().getPlayer(CONFIG.moriarty());
    }

    public static Pair<String, PlayerEntity> getBond(ServerWorld world, ItemStack bookStack) {
        if (!bookStack.isOf(Items.WRITTEN_BOOK)) return Pair.of(null, null);
        if (EnchantmentHelper.getLevel(Enchantments.LUCK_OF_THE_SEA, bookStack) != 10) return Pair.of(null, null);

        NbtCompound nbt = bookStack.getNbt();
        String author = nbt == null ? null : nbt.getString("author");
        if (author == null) return Pair.of(null, null);
        return Pair.of(author, world.getServer().getPlayerManager().getPlayer(author));
    }

    public static boolean isTpAllowed(@Nullable PlayerEntity player) {
        if (player == null) return false;
        return CONFIG.tpAllowed().contains(player.getName().getString());
    }
}
