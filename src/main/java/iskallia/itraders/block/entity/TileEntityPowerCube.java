package iskallia.itraders.block.entity;

import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import iskallia.itraders.block.BlockPowerCube;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/*
 * NBT Structure: {
 *     Nickname: "iGoodie", // Nickname of the subscriber
 *     Rarity: 1, // Enum ordinal of the Rarity
 *     Multiplier: 7 // Bw 1-7
 *     BaseRFRate: 23 // Base RF rate
 *     Decay: { RemainingTicks: 100, MaxTicks: 300 } // 100/300 of use time remaining
 * }
 */
public class TileEntityPowerCube extends TileEntitySynchronized {

    public static TileEntityPowerCube getTileEntity(World world, BlockPos pos) {
        if (world == null)
            return null;

        if (!world.isBlockLoaded(pos))
            return null;

        TileEntity tileEntity = world.getTileEntity(pos);

        if (!(tileEntity instanceof TileEntityPowerCube))
            return null;

        return (TileEntityPowerCube) tileEntity;
    }

    private String nickname = "";
    private BlockPowerCube.CubeRarity rarity = BlockPowerCube.CubeRarity.COMMON;
    private int multiplier = 1;
    private int baseRFRate;

    private int remainingTicks;
    private int maxTicks;

    public TileEntityPowerCube() {}

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        compound.setString("Nickname", nickname);
        compound.setInteger("Rarity", rarity.ordinal());
        compound.setInteger("BaseRFRate", baseRFRate);
        compound.setInteger("Multiplier", multiplier);

        NBTTagCompound decayNBT = new NBTTagCompound();
        decayNBT.setInteger("RemainingTicks", remainingTicks);
        decayNBT.setInteger("MaxTicks", maxTicks);
        compound.setTag("Decay", decayNBT);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.nickname = compound.getString("Nickname");
        this.rarity = BlockPowerCube.CubeRarity.values()[compound.getInteger("Rarity")];
        this.baseRFRate = compound.getInteger("BaseRFRate");

        if (compound.hasKey("Multiplier", Constants.NBT.TAG_INT))
            this.multiplier = compound.getInteger("Multiplier");

        NBTTagCompound decayNBT = compound.getCompoundTag("Decay");
        this.remainingTicks = decayNBT.getInteger("RemainingTicks");
        this.maxTicks = decayNBT.getInteger("MaxTicks");
    }

}
