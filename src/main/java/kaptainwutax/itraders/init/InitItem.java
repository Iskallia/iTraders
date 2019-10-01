package kaptainwutax.itraders.init;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.item.ItemBit;
import kaptainwutax.itraders.item.ItemSpawnEggFighter;
import kaptainwutax.itraders.tab.CreativeTabsITraders;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.registries.IForgeRegistry;

public class InitItem {

	public static CreativeTabsITraders ITRADERS_TAB = new CreativeTabsITraders(Traders.MOD_ID);
	
	public static ItemBit BIT_100 = new ItemBit("bit_100", 100);
	public static ItemBit BIT_500 = new ItemBit("bit_500", 500);
	public static ItemBit BIT_1000 = new ItemBit("bit_1000", 1000);
	public static ItemBit BIT_5000 = new ItemBit("bit_5000", 5000);
	public static ItemBit BIT_10000 = new ItemBit("bit_10000", 10000);
	
	public static ItemSpawnEggFighter SPAWN_EGG_FIGHTER = new ItemSpawnEggFighter("spawn_egg_fighter");
	
	public static void registerItems(IForgeRegistry<Item> registry) {
		registerItem(BIT_100, registry);
		registerItem(BIT_500, registry);
		registerItem(BIT_1000, registry);
		registerItem(BIT_5000, registry);
		registerItem(BIT_10000, registry);
		registerItem(SPAWN_EGG_FIGHTER, registry);
		
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(SPAWN_EGG_FIGHTER, new BehaviorDefaultDispenseItem() {
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                EnumFacing enumfacing = (EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING);
                double d0 = source.getX() + (double)enumfacing.getFrontOffsetX();
                double d1 = (double)((float)(source.getBlockPos().getY() + enumfacing.getFrontOffsetY()) + 0.2F);
                double d2 = source.getZ() + (double)enumfacing.getFrontOffsetZ();
                Entity entity = ItemSpawnEggFighter.spawnCreature(source.getWorld(), ItemMonsterPlacer.getNamedIdFrom(stack), d0, d1, d2);

                if (entity instanceof EntityLivingBase && stack.hasDisplayName()) {
                    entity.setCustomNameTag(stack.getDisplayName());
                }

                ItemMonsterPlacer.applyItemEntityDataToEntity(source.getWorld(), (EntityPlayer)null, stack, entity);
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
		itemBlock.setUnlocalizedName(itemBlock.getBlock().getUnlocalizedName());
		registry.register(itemBlock);
	}

}
