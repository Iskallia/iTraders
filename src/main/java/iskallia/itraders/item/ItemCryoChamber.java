package iskallia.itraders.item;

import iskallia.itraders.init.InitBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCryoChamber extends ItemChamber {
	
	public ItemCryoChamber(String name) {
		super(name);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (this.chamber_block == null)
			this.chamber_block = InitBlock.CRYO_CHAMBER;
		
		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}
}
