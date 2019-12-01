package iskallia.itraders.block;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemSpawnEggFighter;
import iskallia.itraders.util.math.Randomizer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

/*
 * NBT Structure: {
 *     Nickname: "iGoodie", // Nickname of the subscriber
 *     Months: 3, // Months of subscription
 *
 *     Rarity: 1, // Enum ordinal of the Rarity
 *     Decay: { RemainingTicks: 100, MaxTicks: 300 } // 100/300 of use time remaining
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
        cubeNBT.setInteger("Multiplier", Randomizer.randomInt(1, 7));
        cubeNBT.setInteger("BaseRFRate", Randomizer.randomInt(10, 100));

        NBTTagCompound decayNBT = new NBTTagCompound();
        int decayTicks = Randomizer.randomInt(100, 1000) * months;
        decayNBT.setInteger("RemainingTicks", decayTicks);
        decayNBT.setInteger("MaxTicks", decayTicks);
        cubeNBT.setTag("Decay", decayNBT);

        cubeStack.setTagCompound(cubeNBT);

        return cubeStack;
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
    }

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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        NBTTagCompound stackNBT = stack.getTagCompound();

        if (stackNBT == null) return;

        CubeRarity rarity = CubeRarity.values()[stackNBT.getInteger("Rarity")];

        TextFormatting rarityColor = rarity.color;
        tooltip.add(rarityColor + "Rarity: "
                + TextFormatting.GRAY + rarity.translation());
        tooltip.add(rarityColor + "Nickname: "
                + TextFormatting.GRAY + stackNBT.getString("Nickname"));
        tooltip.add(rarityColor + "Multiplier: "
                + TextFormatting.GRAY + stackNBT.getInteger("Multiplier"));
        tooltip.add(rarityColor + "Base RF/tick: "
                + TextFormatting.GRAY + stackNBT.getInteger("BaseRFRate"));

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

        COMMON(0.7, "common", TextFormatting.AQUA),
        RARE(0.15, "rare", TextFormatting.GOLD),
        EPIC(0.10, "epic", TextFormatting.LIGHT_PURPLE),
        MEGA(0.5, "mega", TextFormatting.GREEN);

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
        private TextFormatting color;
        private String i18nKey;

        CubeRarity(double chance, String i18nKey, TextFormatting color) {
            this.chance = chance;
            this.i18nKey = i18nKey;
            this.color = color;
        }

        public String translation() {
            return I18n.format("itraders.cube.rarity." + i18nKey + ".name");
        }

        @Override
        public String getName() {
            return i18nKey;
        }

    }

}
