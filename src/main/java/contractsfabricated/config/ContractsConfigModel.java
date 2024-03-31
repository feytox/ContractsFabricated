package contractsfabricated.config;

import io.wispforest.owo.config.annotation.Config;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

@Config(name = "contracts-config", wrapperName = "ContractsConfig")
public class ContractsConfigModel {

    public String moriarty = "DrAkKoWaS";
    public int dealCount;
    public int points;
    public List<String> bonded = new ObjectArrayList<>();
    public List<String> tpAllowed = ObjectArrayList.of("K00HOME", "DrAkKoWaS");
    public Identifier worldId;
    public Vec3d markedPos;
}
