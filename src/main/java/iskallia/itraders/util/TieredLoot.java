package iskallia.itraders.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class TieredLoot {

	protected static final Random RAND = new Random();
	protected static Map<TieredLoot.Tier, TieredLoot> TIER_MAP = new HashMap<>();

	public static final ItemArmor[] LEATHER_ARMOR = { Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE,
			Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS };
	public static final ItemArmor[] GOLDEN_ARMOR = { Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE,
			Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS };
	public static final ItemArmor[] CHAINMAIL_ARMOR = { Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE,
			Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS };
	public static final ItemArmor[] IRON_ARMOR = { Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS,
			Items.IRON_BOOTS };
	public static final ItemArmor[] DIAMOND_ARMOR = { Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE,
			Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS };

	public static final Item[] WOODEN_WEAPONS = { Items.WOODEN_SWORD, Items.WOODEN_AXE, Items.WOODEN_PICKAXE,
			Items.WOODEN_SHOVEL, Items.WOODEN_HOE };
	public static final Item[] STONE_WEAPONS = { Items.STONE_SWORD, Items.STONE_AXE, Items.STONE_PICKAXE,
			Items.STONE_SHOVEL, Items.STONE_HOE };
	public static final Item[] GOLDEN_WEAPONS = { Items.GOLDEN_SWORD, Items.GOLDEN_AXE, Items.GOLDEN_PICKAXE,
			Items.GOLDEN_SHOVEL, Items.GOLDEN_HOE };
	public static final Item[] IRON_WEAPONS = { Items.IRON_SWORD, Items.IRON_AXE, Items.IRON_PICKAXE, Items.IRON_SHOVEL,
			Items.IRON_HOE };
	public static final Item[] DIAMOND_WEAPONS = { Items.DIAMOND_SWORD, Items.DIAMOND_AXE, Items.DIAMOND_PICKAXE,
			Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE };

	public final Tier tier;

	private ItemArmor[] possibleHelmets = new ItemArmor[0];
	private ItemArmor[] possibleChestplates = new ItemArmor[0];
	private ItemArmor[] possibleLeggings = new ItemArmor[0];
	private ItemArmor[] possibleBoots = new ItemArmor[0];
	private Item[] possibleWeapons = new Item[0];

	public TieredLoot(Tier tier) {
		this.tier = tier;
		TIER_MAP.put(this.tier, this);
	}

	public static TieredLoot get(Tier tier) {
		return TIER_MAP.get(tier);
	}

	public void setArmor(Object[] armor) {
		this.possibleHelmets = Arrays.stream(armor).filter(a -> a instanceof ItemArmor)
				.filter(a -> ((ItemArmor) a).armorType == EntityEquipmentSlot.HEAD).toArray(ItemArmor[]::new);

		this.possibleChestplates = Arrays.stream(armor).filter(a -> a instanceof ItemArmor)
				.filter(a -> ((ItemArmor) a).armorType == EntityEquipmentSlot.CHEST).toArray(ItemArmor[]::new);

		this.possibleLeggings = Arrays.stream(armor).filter(a -> a instanceof ItemArmor)
				.filter(a -> ((ItemArmor) a).armorType == EntityEquipmentSlot.LEGS).toArray(ItemArmor[]::new);

		this.possibleBoots = Arrays.stream(armor).filter(a -> a instanceof ItemArmor)
				.filter(a -> ((ItemArmor) a).armorType == EntityEquipmentSlot.FEET).toArray(ItemArmor[]::new);
	}

	public void setWeapons(Object[] weapons) {
		this.possibleWeapons = Arrays.stream(weapons).collect(Collectors.toSet()).stream().toArray(Item[]::new);
	}

	private ItemStack getRandomItem(Item[] set) {
		int i = RAND.nextInt(set.length);
		Item item = set[i];
		return new ItemStack(item, 1);
	}

	public ItemStack getRandomLoot(EntityEquipmentSlot slot) {
		if (slot == EntityEquipmentSlot.HEAD) {
			return this.getRandomHelmet();
		} else if (slot == EntityEquipmentSlot.CHEST) {
			return this.getRandomChestplate();
		} else if (slot == EntityEquipmentSlot.LEGS) {
			return this.getRandomLeggings();
		} else if (slot == EntityEquipmentSlot.FEET) {
			return this.getRandomBoots();
		} else {
			return this.getRandomWeapon();
		}
	}

	public ItemStack getRandomHelmet() {
		return this.getRandomItem(this.possibleHelmets);
	}

	public ItemStack getRandomChestplate() {
		return this.getRandomItem(this.possibleChestplates);
	}

	public ItemStack getRandomLeggings() {
		return this.getRandomItem(this.possibleLeggings);
	}

	public ItemStack getRandomBoots() {
		return this.getRandomItem(this.possibleBoots);
	}

	public ItemStack getRandomWeapon() {
		return this.getRandomItem(this.possibleWeapons);
	}

	public ItemStack enchant(ItemStack stack) {
		EnchantmentHelper.addRandomEnchantment(RAND, stack,
				EnchantmentHelper.calcItemStackEnchantability(RAND, this.tier.enchantmentPower, 15, stack), true);
		return stack;
	}

	public static enum Tier {
		COMMON(0), RARE(1), EPIC(2);

		public final int enchantmentPower;

		private Tier(int enchantmentPower) {
			this.enchantmentPower = enchantmentPower;
		}
	}

}
