package kaptainwutax.itraders.item;

import kaptainwutax.itraders.Traders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpawnEggMiner extends ItemSpawnEgg {

	public ItemSpawnEggMiner(String name) {
		super(name, Traders.getResource("miner"));
	}

	@Override
	protected void doSpawning(World world, BlockPos pos, ItemStack stack) {
		Entity entity = spawnCreature(world, getNamedIdFrom(stack), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
		if (stack.hasDisplayName())entity.setCustomNameTag(stack.getDisplayName());
		ItemMonsterPlacer.applyItemEntityDataToEntity(world, (EntityPlayer) null, stack, entity);		
	}

}
