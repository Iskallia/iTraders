package kaptainwutax.itraders.item;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.entity.EntityMiner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpawnEggMiner extends ItemSpawnEgg<EntityMiner> {

	public ItemSpawnEggMiner(String name) {
		super(name, Traders.getResource("miner"), true);
	}

	@Override
	protected EntityMiner onPlayerSpawn(World world, EntityPlayer player, BlockPos pos, ItemStack stack) {
		EntityMiner miner = this.spawnCreature(world, getNamedIdFrom(stack), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
		if (stack.hasDisplayName())miner.setCustomNameTag(stack.getDisplayName());
		ItemMonsterPlacer.applyItemEntityDataToEntity(world, (EntityPlayer)null, stack, miner);	
		miner.setMiningDirection(player.getHorizontalFacing());
		return miner;
	}

	@Override
	protected EntityMiner onDispenserSpawn(World world, EnumFacing facing, BlockPos pos, ItemStack stack) {
		EntityMiner miner = this.spawnCreature(world, getNamedIdFrom(stack), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
		if (stack.hasDisplayName())miner.setCustomNameTag(stack.getDisplayName());
		ItemMonsterPlacer.applyItemEntityDataToEntity(world, (EntityPlayer)null, stack, miner);				
		miner.setMiningDirection(facing);
		return miner;
	}
	
}
