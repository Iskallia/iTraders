package kaptainwutax.itraders.block;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitBlock;
import kaptainwutax.itraders.init.InitConfig;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.item.ItemSkullNeck;
import kaptainwutax.itraders.tile.TileInfusionCauldron;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockInfusionCauldron extends BlockCauldron implements ITileEntityProvider {

    public BlockInfusionCauldron(String name) {
        super();
        this.setHardness(2f);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ))
            return true; // TODO handle only WATER_BUCKET and BUCKET cases

        if (world.isRemote) return true;

        ItemStack heldStack = player.getHeldItem(hand);
        int currentWaterLevel = state.getValue(LEVEL);

        if (currentWaterLevel <= 0)
            return true;

        if (heldStack.getItem() != Items.SKULL)
            return true;

        NBTTagCompound stackNBT = heldStack.getTagCompound();

        if (stackNBT == null || !stackNBT.hasKey("SkullOwner", Constants.NBT.TAG_COMPOUND))
            return true;

        NBTTagCompound skullOwnerNBT = stackNBT.getCompoundTag("SkullOwner");

        if (!skullOwnerNBT.hasKey("Name", Constants.NBT.TAG_STRING))
            return true;

        if (!player.isCreative())
            heldStack.shrink(1);

        if (Math.random() <= InitConfig.CONFIG_SKULL_NECKLACE.NECKLACE_CREATION_RATE) {
            String ghostName = skullOwnerNBT.getString("Name");
            ItemStack necklaceStack = ItemSkullNeck.generateRandom(ghostName);
            this.spawnNecklace((WorldServer) world, pos, necklaceStack);

        } else {
            int particleCount = 300;

            world.playSound(null,
                    pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.ENTITY_ITEM_BREAK,
                    SoundCategory.MASTER,
                    1.0f, (float) Math.random());
            ((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false,
                    pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d,
                    particleCount,
                    0, 0, 0,
                    0.1d);
        }

        setWaterLevel(world, pos, state, currentWaterLevel - 1);

        return true;
    }

    public void spawnNecklace(WorldServer world, BlockPos pos, ItemStack necklaceStack) {
        double itemEntityX = pos.getX() + 0.5d;
        double itemEntityY = pos.getY() + 1.0d;
        double itemEntityZ = pos.getZ() + 0.5d;

        EntityItem itemEntity = new EntityItem(world, itemEntityX, itemEntityY, itemEntityZ, necklaceStack);

        int particleCount = 100;

        world.playSound(null,
                pos.getX(), pos.getY(), pos.getZ(),
                SoundEvents.ENTITY_PLAYER_LEVELUP,
                SoundCategory.MASTER,
                1.0f, (float) Math.random());
        world.spawnParticle(EnumParticleTypes.SPELL_WITCH, false,
                pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d,
                particleCount,
                0, 0, 0,
                Math.PI);
        world.spawnEntity(itemEntity);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return InitBlock.ITEM_INFUSION_CAULDRON;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);

        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileInfusionCauldron();
    }

}
