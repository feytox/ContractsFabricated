package contractsfabricated.config;

import io.wispforest.owo.config.annotation.Config;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

@Config(name = "contracts-config", wrapperName = "ContractsConfig")
public class ContractsConfigModel {

    // TODO: 28.03.2024 change
    public String moriarty = "Feytox";
    public int dealCount;
    public int points;
    public boolean battle;
    public boolean giveCane;
    public List<String> bonded = new ObjectArrayList<>();
}
