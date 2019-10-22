package iskallia.itraders.block;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityCryoChamber;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCryoChamber extends Block {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyEnum<BlockCryoChamber.EnumPartType> PART = PropertyEnum.create("part", BlockCryoChamber.EnumPartType.class);

    public BlockCryoChamber(String name, Material materialIn) {
        super(materialIn);
        this.setRegistryName(Traders.getResource(name));
        this.setUnlocalizedName(name);
        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PART, BlockCryoChamber.EnumPartType.BOTTOM));
        this.setHardness(2f);
    }

    public int getMetaFromState(IBlockState state) {
        return (state.getValue(PART).ordinal() << 2)
                | state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void neighborChanged(IBlockState stateObserved, World world, BlockPos posObserved, Block blockChanged, BlockPos fromPos) {
        if (blockChanged != InitBlock.CRYO_CHAMBER)
            return; // No need to handle, anything other than Cryo-chamber was changed

        EnumPartType observedPart = stateObserved.getValue(PART);
        BlockPos posOtherPart = observedPart == EnumPartType.BOTTOM ? posObserved.up() : posObserved.down();
        IBlockState stateOtherPart = world.getBlockState(posOtherPart);

        // Block was changed from Cryo-chamber to something else
        if (stateOtherPart.getBlock() != InitBlock.CRYO_CHAMBER) {
            world.setBlockToAir(posObserved);

            if (!world.isRemote)
                dropBlockAsItem(world, posObserved, stateObserved, 0);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(PART) == EnumPartType.BOTTOM
                ? InitItem.CRYO_CHAMBER
                : Items.AIR;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(InitItem.CRYO_CHAMBER);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(PART, BlockCryoChamber.EnumPartType.values()[meta >> 2]).withProperty(FACING, EnumFacing.getHorizontal(meta & 0b11));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PART, FACING);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCryoChamber();
    }

    public enum EnumPartType implements IStringSerializable {

        TOP("top"), BOTTOM("bottom");

        private final String name;

        EnumPartType(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

    }

}
