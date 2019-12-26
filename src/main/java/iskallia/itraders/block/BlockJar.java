package iskallia.itraders.block;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityJar;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockJar extends BlockFacing implements ITileEntityProvider {

    public BlockJar(String name) {
        super(Material.GLASS);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
    	return false;
    }
    
    public boolean isFullCube(IBlockState state) {     
    	return false;
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityJar();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		ItemStack heldItem = player.getHeldItem(hand);
		TileEntityJar jar = world.isRemote ? null : (TileEntityJar)world.getTileEntity(pos);
		
		if(!heldItem.isEmpty() && heldItem.getItem() == InitItem.SPAWN_EGG_TRADER) {		
			if(jar != null && jar.getDonator().isEmpty()) {
				jar.setDonator(heldItem.copy());
				
				if(!player.capabilities.isCreativeMode) {
					heldItem.shrink(1);
				}
				
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0f, 0.8f);
			}
			
			return true;
		}
		
		if(jar != null && jar.feed(heldItem)) {
			return true;
		}
		
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}

}
