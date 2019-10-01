package kaptainwutax.itraders.init;

import kaptainwutax.itraders.config.ConfigFighter;
import kaptainwutax.itraders.config.ConfigTrader;

public class InitConfig {

	public static ConfigTrader CONFIG_TRADER = null;
	public static ConfigFighter CONFIG_FIGHTER = null;
	
	public static void registerConfigs() {
		CONFIG_TRADER = (ConfigTrader)new ConfigTrader().readConfig();
		CONFIG_FIGHTER = (ConfigFighter)new ConfigFighter().readConfig();
	}
	
}
