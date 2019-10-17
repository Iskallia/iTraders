package kaptainwutax.itraders.block;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.item.ItemSkullNeck;
import kaptainwutax.itraders.util.Randomizer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandParticle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;

public class BlockInfusionCauldron extends BlockCauldron {

    public static final double NECKLACE_CREATION_RATE = 0.10d;

    public BlockInfusionCauldron(String name) {
        super();

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

        if (Math.random() <= NECKLACE_CREATION_RATE) {
            String ghostName = skullOwnerNBT.getString("Name");
            ItemStack necklaceStack = ItemSkullNeck.generateRandom(ghostName);
            this.spawnNecklace((WorldServer) world, pos, necklaceStack);

        } else {
            this.spawnXP((WorldServer) world, pos);
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
                pos.getX() + .5d, pos.getY() + .5d, pos.getZ() +.5d,
                particleCount,
                0, 0, 0,
                Math.PI);
        world.spawnEntity(itemEntity);
    }

    public void spawnXP(WorldServer world, BlockPos pos) {
        double xpEntityX = pos.getX() + 0.5d;
        double xpEntityY = pos.getY() + 1.0d;
        double xpEntityZ = pos.getZ() + 0.5d;

        EntityXPOrb entityXPOrb = new EntityXPOrb(world, xpEntityX, xpEntityY, xpEntityZ, Randomizer.randomInt(10, 200));

        int particleCount = 300;

        world.playSound(null,
                pos.getX(), pos.getY(), pos.getZ(),
                SoundEvents.ENTITY_ITEM_BREAK,
                SoundCategory.MASTER,
                1.0f, (float) Math.random());
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false,
                pos.getX() + .5d, pos.getY() + .5d, pos.getZ() +.5d,
                particleCount,
                0, 0, 0,
                0.1d);
        world.spawnEntity(entityXPOrb);
    }

}
