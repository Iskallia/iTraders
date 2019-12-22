package iskallia.itraders.block;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityPowerCube;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemSpawnEggFighter;
import iskallia.itraders.util.math.Randomizer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/*
 * NBT Structure: {
 *     Nickname: "iGoodie", // Nickname of the subscriber
 *     Rarity: 1, // Enum ordinal of the Rarity
 *     BaseRFRate: 23 // Base RF rate
 *
 *     Decay: { RemainingTicks: 100, MaxTicks: 300 } // 100/300 of use time remaining
 *
 *     FighterRot: 90 // Rotation in degrees
 * }
 */
public class BlockPowerCube extends Block {

    public static final PropertyEnum<CubeRarity> RARITY = PropertyEnum.create("rarity", CubeRarity.class);

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(
            1 / 16D, 1 / 16D, 1 / 16D,
            15 / 16D, 15 / 16D, 15 / 16D
    );

    public static ItemStack generateRandomly(ItemStack eggStack) {
        if (eggStack.getItem() != InitItem.SPAWN_EGG_FIGHTER)
            return ItemStack.EMPTY;

        NBTTagCompound eggNBT = eggStack.getTagCompound();
        int months = Math.max(InitItem.SPAWN_EGG_FIGHTER.getMonths(eggStack), 1);

        ItemStack cubeStack = new ItemStack(InitBlock.ITEM_POWER_CUBE, 1, 0);

        NBTTagCompound cubeNBT = new NBTTagCompound();
        cubeNBT.setString("Nickname", eggStack.getDisplayName());
        cubeNBT.setInteger("Rarity", CubeRarity.randomRarity().ordinal());
        cubeNBT.setInteger("BaseRFRate", Randomizer.randomInt(100, 1000) * months);

        NBTTagCompound decayNBT = new NBTTagCompound();
        int decayTicks = Randomizer.randomInt(100, 1000) * months; // TODO: Decide the formula later
        decayNBT.setInteger("RemainingTicks", decayTicks);
        decayNBT.setInteger("MaxTicks", decayTicks);
        cubeNBT.setTag("Decay", decayNBT);

        cubeStack.setTagCompound(cubeNBT);

        return cubeStack;
    }

    public static void placePowerCube(ItemStack cubeStack, World world, BlockPos pos) {
        IBlockState blockState = InitBlock.POWER_CUBE.getDefaultState();

        NBTTagCompound cubeStackNBT = cubeStack.getTagCompound();
        CubeRarity rarity = cubeStackNBT != null
                ? CubeRarity.values()[cubeStackNBT.getInteger("Rarity")] : CubeRarity.COMMON;

        // Set block state
        world.setBlockState(pos, blockState.withProperty(RARITY, rarity));

        // Set tile entity
        if (cubeStackNBT != null)
            ((TileEntityPowerCube) world.getTileEntity(pos)).readCustomNBT(cubeStackNBT);

        // Notify block update
        world.notifyNeighborsOfStateChange(pos, InitBlock.POWER_CUBE, false);
    }

    /* ------------------------------- */

    public BlockPowerCube(String name) {
        super(Material.ROCK);

        this.setUnlocalizedName(name);
        this.setRegistryName(Traders.getResource(name));

        this.setCreativeTab(InitItem.ITRADERS_TAB);
        this.setHardness(2f);

        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(RARITY, CubeRarity.COMMON)
        );
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(RARITY).ordinal());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(RARITY, CubeRarity.values()[meta]);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, RARITY);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        NBTTagCompound stackNBT = stack.getTagCompound();

        if (stackNBT != null) {
            CubeRarity rarity = CubeRarity.values()[stackNBT.getInteger("Rarity")];
            world.setBlockState(pos, state.withProperty(RARITY, rarity));
        }

        TileEntityPowerCube powerCube = TileEntityPowerCube.getTileEntity(world, pos);

        if (powerCube != null && stackNBT != null) {
            powerCube.readCustomNBT(stackNBT);
//            Vec3d A = placer.getPositionVector();
//            Vec3d B = new Vec3d(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5f);
//            Vec3d C = A.subtract(B);
//            powerCube.setFighterRotation((float) (Math.atan2(C.y, C.x) * (180f / Math.PI)) + 180f);
            powerCube.setFighterRotation(180f + placer.getRotationYawHead());
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!world.isRemote && !player.isCreative()) {
            TileEntityPowerCube powerCube = TileEntityPowerCube.getTileEntity(world, pos);

            if (powerCube != null) {
                ItemStack cubeStack = new ItemStack(InitBlock.ITEM_POWER_CUBE);

                NBTTagCompound cubeNBT = new NBTTagCompound();
                powerCube.writeCustomNBT(cubeNBT);
                cubeStack.setTagCompound(cubeNBT);

                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), cubeStack);
            }
        }

        super.onBlockHarvested(world, pos, state, player);
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        ItemStack cubeStack = new ItemStack(InitBlock.ITEM_POWER_CUBE);

        TileEntityPowerCube powerCube = TileEntityPowerCube.getTileEntity(world, pos);

        if (powerCube != null) {
            NBTTagCompound cubeNBT = new NBTTagCompound();
            powerCube.writeCustomNBT(cubeNBT);
            cubeStack.setTagCompound(cubeNBT);
        }

        return cubeStack;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, @Nonnull Random random) {
        return 0;
    }

    @Nonnull
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityPowerCube();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        NBTTagCompound stackNBT = stack.getTagCompound();

        if (stackNBT == null) return;

        CubeRarity rarity = CubeRarity.values()[stackNBT.getInteger("Rarity")];
        int baseRFRate = stackNBT.getInteger("BaseRFRate");

        TextFormatting rarityColor = rarity.color;
        tooltip.add(rarityColor + "Rarity: "
                + TextFormatting.GRAY + rarity.translation());
        tooltip.add(rarityColor + "Nickname: "
                + TextFormatting.GRAY + stackNBT.getString("Nickname"));

        tooltip.add("");

        tooltip.add(rarityColor + "" + (rarity.getMultiplier() * baseRFRate) + " RF/tick");

        if (stackNBT.hasKey("Decay", Constants.NBT.TAG_COMPOUND)) {
            tooltip.add("");

            NBTTagCompound decayNBT = stackNBT.getCompoundTag("Decay");
            tooltip.add(rarityColor + "Decay: "
                    + TextFormatting.GRAY + decayNBT.getInteger("RemainingTicks")
                    + "/" + decayNBT.getInteger("MaxTicks"));
        }

    }

    /* ------------------------------- */

    public enum CubeRarity implements IStringSerializable {

        COMMON(0.7, 1, "common", TextFormatting.AQUA),
        RARE(0.15, 2, "rare", TextFormatting.GOLD),
        EPIC(0.10, 4, "epic", TextFormatting.LIGHT_PURPLE),
        MEGA(0.5, 7, "mega", TextFormatting.GREEN);

        public static CubeRarity randomRarity() {
            double random = Math.random();
            for (CubeRarity rarity : values()) {
                if (random <= rarity.chance) {
                    return rarity;
                }

                random -= rarity.chance;
            }

            return COMMON; // Shouldn't even reach here
        }

        private double chance;
        private int multiplier;
        private TextFormatting color;
        private String i18nKey;

        CubeRarity(double chance, int multiplier, String i18nKey, TextFormatting color) {
            this.chance = chance;
            this.i18nKey = i18nKey;
            this.multiplier = multiplier;
            this.color = color;
        }

        public String translation() {
            return I18n.format("itraders.cube.rarity." + i18nKey + ".name");
        }

        @Nonnull
        @Override
        public String getName() {
            return i18nKey;
        }

        public int getMultiplier() {
            return multiplier;
        }

        public double getChance() {
            return chance;
        }

    }

}
