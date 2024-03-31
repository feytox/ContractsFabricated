package contractsfabricated.config;

import io.wispforest.owo.config.annotation.Config;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

@Config(name = "contracts-config", wrapperName = "ContractsConfig")
public class ContractsConfigModel {

    // TODO: 28.03.2024 change
    public String moriarty = "Feytox";
    public int dealCount;
    public int points;
    public List<String> bonded = new ObjectArrayList<>();
    public List<String> tpAllowed = ObjectArrayList.of("K00HOME", "DrAkKoWaS", "Feytox");
    public Identifier worldId;
    public Vec3d markedPos;
}
