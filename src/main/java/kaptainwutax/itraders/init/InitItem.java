package kaptainwutax.itraders.init;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.item.ItemBit;
import kaptainwutax.itraders.item.ItemCryoChamber;
import kaptainwutax.itraders.item.ItemEggPouch;
import kaptainwutax.itraders.item.ItemMagicOreDust;
import kaptainwutax.itraders.item.ItemSkullNeck;
import kaptainwutax.itraders.item.ItemSpawnEggFighter;
import kaptainwutax.itraders.item.ItemSpawnEggMiner;
import kaptainwutax.itraders.item.ItemSpawnEggTrader;
import kaptainwutax.itraders.item.ItemSubCard;
import kaptainwutax.itraders.tab.CreativeTabsITraders;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class InitItem {

	public static CreativeTabsITraders ITRADERS_TAB = new CreativeTabsITraders(Traders.MOD_ID);

	public static ItemBit BIT_100 = new ItemBit("bit_100", 100);
	public static ItemBit BIT_500 = new ItemBit("bit_500", 500);
	public static ItemBit BIT_1000 = new ItemBit("bit_1000", 1000);
	public static ItemBit BIT_5000 = new ItemBit("bit_5000", 5000);
	public static ItemBit BIT_10000 = new ItemBit("bit_10000", 10000);

	public static ItemSpawnEggTrader SPAWN_EGG_TRADER = new ItemSpawnEggTrader("spawn_egg_trader");
	public static ItemSpawnEggFighter SPAWN_EGG_FIGHTER = new ItemSpawnEggFighter("spawn_egg_fighter");
	public static ItemSpawnEggMiner SPAWN_EGG_MINER = new ItemSpawnEggMiner("spawn_egg_miner");
	public static ItemEggPouch EGG_POUCH = new ItemEggPouch("egg_pouch");
	public static ItemSkullNeck SKULL_NECKLACE = new ItemSkullNeck("skull_neck");
	public static ItemMagicOreDust MAGIC_ORE_DUST = new ItemMagicOreDust("magic_ore_dust");

	public static ItemSubCard SUB_CARD = new ItemSubCard("sub_card");
	public static ItemCryoChamber CRYO_CHAMBER = new ItemCryoChamber("item_cryo_chamber");

	public static void registerItems(IForgeRegistry<Item> registry) {
		registerItem(BIT_100, registry);
		registerItem(BIT_500, registry);
		registerItem(BIT_1000, registry);
		registerItem(BIT_5000, registry);
		registerItem(BIT_10000, registry);
		registerItem(SPAWN_EGG_TRADER, registry);
		registerItem(SPAWN_EGG_FIGHTER, registry);
		registerItem(SPAWN_EGG_MINER, registry);
		registerItem(EGG_POUCH, registry);
		registerItem(SUB_CARD, registry);
		registerItem(CRYO_CHAMBER, registry);

		registerItem(SKULL_NECKLACE, registry);
		registerItem(MAGIC_ORE_DUST, registry);
	}

	private static void registerItem(Item item, IForgeRegistry<Item> registry) {
		registry.register(item);
	}

}
