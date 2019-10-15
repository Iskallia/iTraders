package kaptainwutax.itraders.init;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.item.ItemBit;
import kaptainwutax.itraders.item.ItemEggPouch;
import kaptainwutax.itraders.item.ItemSpawnEggFighter;
import kaptainwutax.itraders.item.ItemSpawnEggMiner;
import kaptainwutax.itraders.item.ItemSpawnEggTrader;
import kaptainwutax.itraders.tab.CreativeTabsITraders;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(SPAWN_EGG_FIGHTER, new BehaviorDefaultDispenseItem() {
			public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
				EnumFacing enumfacing = (EnumFacing) source.getBlockState().getValue(BlockDispenser.FACING);
				double x = source.getX() + (double) enumfacing.getXOffset();
				double y = (double) ((float) (source.getBlockPos().getY() + enumfacing.getYOffset()) + 0.2F);
				double z = source.getZ() + (double) enumfacing.getZOffset();

				NBTTagCompound stackNBT = stack.getTagCompound();

				InitItem.SPAWN_EGG_FIGHTER.doSpawning(source.getWorld(), new BlockPos(x, y, z), stack);

				stack.shrink(1);
				return stack;
			}
		});
	}

	private static void registerItem(Item item, IForgeRegistry<Item> registry) {
		registry.register(item);
	}

	private static void registerItemBlock(ItemBlock itemBlock, IForgeRegistry<Item> registry) {
		itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName());
		itemBlock.setTranslationKey(itemBlock.getBlock().getTranslationKey());
		registry.register(itemBlock);
	}

}
