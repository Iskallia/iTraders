package kaptainwutax.itraders.init;

import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import kaptainwutax.itraders.util.TieredLoot;

public class InitTieredLoot {

	public static TieredLoot COMMON_TIER = new TieredLoot(TieredLoot.Tier.COMMON);
	public static TieredLoot RARE_TIER = new TieredLoot(TieredLoot.Tier.RARE);
	public static TieredLoot EPIC_TIER = new TieredLoot(TieredLoot.Tier.EPIC);
	
	public static void registerTiers() {
		COMMON_TIER.setArmor(merge(TieredLoot.LEATHER_ARMOR, TieredLoot.GOLDEN_ARMOR));
		RARE_TIER.setArmor(merge(TieredLoot.IRON_ARMOR, TieredLoot.CHAINMAIL_ARMOR));
		EPIC_TIER.setArmor(merge(TieredLoot.IRON_ARMOR, TieredLoot.CHAINMAIL_ARMOR, TieredLoot.DIAMOND_ARMOR));
		
		COMMON_TIER.setWeapons(merge(TieredLoot.WOODEN_WEAPONS, TieredLoot.STONE_WEAPONS, TieredLoot.IRON_WEAPONS));
		RARE_TIER.setWeapons(merge(TieredLoot.STONE_WEAPONS, TieredLoot.IRON_WEAPONS, TieredLoot.GOLDEN_WEAPONS));
		EPIC_TIER.setWeapons(merge(TieredLoot.IRON_WEAPONS, TieredLoot.DIAMOND_WEAPONS));	
	}
	
	private static Object[] merge(Object[] ...arrays) {
		return Stream.of(arrays)
						.flatMap(Stream::of)	
						.toArray();
	}
	
}
