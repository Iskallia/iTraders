package iskallia.itraders.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import iskallia.itraders.Traders;
import iskallia.itraders.config.definition.LootDefinition;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemChanceBag.WeightedRandomBag.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemChanceBag extends Item {

	private EnumRarity rarity;

	public ItemChanceBag(String name, EnumRarity rarity) {
		this.setUnlocalizedName(name);
		this.setRegistryName(Traders.getResource(name));
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setMaxStackSize(1);

		this.rarity = rarity;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (world.isRemote)
			return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));

		ItemStack stack = player.getHeldItem(hand);

		if (stack.isEmpty() || !(stack.getItem() instanceof ItemChanceBag))
			return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));

		ItemChanceBag bag = (ItemChanceBag) stack.getItem();
		EnumRarity rarity = bag.getRarity();

		ItemStack loot = getLoot(rarity);

		player.dropItem(loot, true);

		if (!player.capabilities.isCreativeMode)
			stack.shrink(1);

		world.playSound(null, player.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0f, (float) Math.max(Math.random(), 0.9));

		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return this.rarity;
	}

	private ItemStack getLoot(EnumRarity rarity) {
		List<LootDefinition> loot;

		switch (rarity) {
		case COMMON:
			loot = InitConfig.CONFIG_CHANCE_BAG.COMMON_LOOT;
			break;
		case EPIC:
			loot = InitConfig.CONFIG_CHANCE_BAG.EPIC_LOOT;
			break;
		case RARE:
			loot = InitConfig.CONFIG_CHANCE_BAG.RARE_LOOT;
			break;
		default:
			return ItemStack.EMPTY;
		}

		return getWeightedRandomStack(loot);
	}

	private ItemStack getWeightedRandomStack(List<LootDefinition> loot) {
		WeightedRandomBag<String> weightedLoot = new WeightedRandomBag<>();

		for (LootDefinition def : loot) {
			weightedLoot.addEntry(def.getItemId(), def.getWeight(), def.getAmount(), def.getMeta(), def.getNbt());
		}

		Entry e = weightedLoot.getRandom();

		Item item = Item.getByNameOrId(e.itemId);

		if (item == null)
			return ItemStack.EMPTY;

		int amount = e.amount;
		int meta = e.meta;
		NBTTagCompound nbt = new NBTTagCompound();
		if (e.nbt != null) {
			try {
				nbt = JsonToNBT.getTagFromJson(e.nbt);
			} catch (NBTException ex) {
				Traders.LOG.error("Invalid NBT on item: " + e.itemId);
			}
		}

		ItemStack stack = new ItemStack(item, amount, meta);
		stack.setTagCompound(nbt);

		return stack;
	}

	public EnumRarity getRarity() {
		return this.rarity;
	}

	public class WeightedRandomBag<T extends Object> {

		public class Entry {
			double accumulatedWeight;
			String itemId;
			int amount;
			int meta;
			String nbt;
		}

		private List<Entry> entries = new ArrayList<>();
		private double accumulatedWeight;
		private Random rand = new Random();

		public void addEntry(String itemId, double weight, int amount, int meta, String nbt) {
			accumulatedWeight += weight;
			Entry e = new Entry();
			e.itemId = itemId;
			e.amount = amount;
			e.meta = meta;
			e.nbt = nbt;
			e.accumulatedWeight = accumulatedWeight;
			entries.add(e);
		}

		public Entry getRandom() {
			double r = rand.nextDouble() * accumulatedWeight;

			for (Entry entry : entries) {
				if (entry.accumulatedWeight >= r) {
					return entry;
				}
			}
			return null; // should only happen when there are no entries
		}
	}

}
