package iskallia.itraders.init;

import iskallia.itraders.config.*;

public class InitConfig {

	public static ConfigTrader CONFIG_TRADER = null;
	public static ConfigFighter CONFIG_FIGHTER = null;
	public static ConfigSkullNecklace CONFIG_SKULL_NECKLACE = null;
	public static ConfigCardGenerator CONFIG_CARD_GENERATOR = null;
	public static ConfigCryoChamber CONFIG_CRYO_CHAMBER = null;
	public static ConfigCubeChamber CONFIG_CUBE_CHAMBER = null;
	public static ConfigAccelerationBottle CONFIG_ACCELERATION_BOTTLE = null;
	public static ConfigJar CONFIG_JAR = null;
	
	public static void registerConfigs() {
		CONFIG_TRADER = (ConfigTrader) new ConfigTrader().readConfig();
		CONFIG_FIGHTER = (ConfigFighter) new ConfigFighter().readConfig();
		CONFIG_SKULL_NECKLACE = (ConfigSkullNecklace) new ConfigSkullNecklace().readConfig();
		CONFIG_CARD_GENERATOR = (ConfigCardGenerator) new ConfigCardGenerator().readConfig();
		CONFIG_CRYO_CHAMBER = (ConfigCryoChamber) new ConfigCryoChamber().readConfig();
		CONFIG_CUBE_CHAMBER = (ConfigCubeChamber) new ConfigCubeChamber().readConfig();
		CONFIG_ACCELERATION_BOTTLE = (ConfigAccelerationBottle) new ConfigAccelerationBottle().readConfig();
		CONFIG_JAR = (ConfigJar) new ConfigJar().readConfig();
	}

}
