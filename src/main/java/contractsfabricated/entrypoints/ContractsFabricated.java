package contractsfabricated.entrypoints;

import contractsfabricated.commands.MoriartyCommand;
import contractsfabricated.config.ContractsConfig;
import contractsfabricated.item.CItemGroup;
import contractsfabricated.item.CItems;
import contractsfabricated.network.FeyNetworkUtil;
import contractsfabricated.util.DelayedTask;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContractsFabricated implements ModInitializer {

	public static final String MOD_ID = "contractsfabricated";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ContractsConfig CONFIG = ContractsConfig.createAndLoad();

	@Override
	public void onInitialize() {
		CItems.registerAll();
		FeyNetworkUtil.registerPackets();
		MoriartyCommand.register();
		CItemGroup.registerAll();

		ServerTickEvents.END_SERVER_TICK.register(server -> DelayedTask.tickTasks());

		LOGGER.info("ContractsFabricated has been loaded");
	}

	public static void reloadConfig() {
		CONFIG.load();
	}
}