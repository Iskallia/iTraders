package kaptainwutax.itraders.init;

import kaptainwutax.itraders.config.ConfigCardGenerator;
import kaptainwutax.itraders.config.ConfigFighter;
import kaptainwutax.itraders.config.ConfigSkullNecklace;
import kaptainwutax.itraders.config.ConfigTrader;

public class InitConfig {

	public static ConfigTrader CONFIG_TRADER = null;
	public static ConfigFighter CONFIG_FIGHTER = null;
	public static ConfigSkullNecklace CONFIG_SKULL_NECKLACE = null;
	public static ConfigCardGenerator CONFIG_CARD_GENERATOR	= null;

	public static void registerConfigs() {
		CONFIG_TRADER = (ConfigTrader) new ConfigTrader().readConfig();
		CONFIG_FIGHTER = (ConfigFighter) new ConfigFighter().readConfig();
		CONFIG_SKULL_NECKLACE = (ConfigSkullNecklace) new ConfigSkullNecklace().readConfig();
		CONFIG_CARD_GENERATOR = (ConfigCardGenerator) new ConfigCardGenerator().readConfig();
	}

}
