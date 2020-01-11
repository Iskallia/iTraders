package iskallia.itraders.block.entity;

import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import iskallia.itraders.block.BlockPowerCube;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.util.math.Randomizer;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.awt.Color;

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
public class TileEntityPowerCube extends TileEntitySynchronized implements ITickable {

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

    @Nonnull
    @SideOnly(Side.CLIENT)
    private SkinProfile skin = new SkinProfile();

    @Nonnull
    private String nickname = "";

    private BlockPowerCube.CubeRarity rarity = BlockPowerCube.CubeRarity.COMMON;
    private int baseRFRate;

    private int remainingTicks;
    private int maxTicks;

    private float fighterRotation;

    public TileEntityPowerCube() {}

    @Nonnull
    public String getNickname() {
        return nickname;
    }

    @Nonnull
    public SkinProfile getSkin() {
        return skin;
    }

    public BlockPowerCube.CubeRarity getRarity() {
        return rarity;
    }

    public int getBaseRFRate() {
        return baseRFRate;
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    public void consumeDecay(int amount) {
        this.remainingTicks -= amount;
        this.markForUpdate();
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public float getFighterRotation() {
        return fighterRotation;
    }

    public void setFighterRotation(float fighterRotation) {
        this.fighterRotation = fighterRotation;
        this.markForUpdate();
    }

    @Override
    public void update() {
        if (world.isRemote) {
            updateClient();
        }
    }

    protected void updateClient() {
        String latestNickname = skin.getLatestNickname();
        if (latestNickname == null || !latestNickname.equals(nickname)) {
            skin.updateSkin(nickname);
        }

        if (Randomizer.randomIntEx(5) == 0) {
            EntityFXFacingParticle p = EffectHelper.genericFlareParticle(
                    pos.getX() + Randomizer.randomDouble(),
                    pos.getY() + Randomizer.randomDouble(),
                    pos.getZ() + Randomizer.randomDouble()
            );
            p.motion(0, Randomizer.randomDouble() * 0.05, 0);
            p.scale(0.2f);

            switch (rarity) {
                case RARE:
                    p.setColor(new Color(0xFF_FDE802));
                    break;
                case EPIC:
                    p.setColor(new Color(0xFF_DE2CD8));
                    break;
                case MEGA:
                    p.setColor(new Color(0xFF_85FA0F));
                    break;
                default:
                    p.setColor(new Color(0xFF_EBEBEB));
                    break;
            }
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        compound.setString("Nickname", nickname);
        compound.setInteger("Rarity", rarity.ordinal());
        compound.setInteger("BaseRFRate", baseRFRate);

        NBTTagCompound decayNBT = new NBTTagCompound();
        decayNBT.setInteger("RemainingTicks", remainingTicks);
        decayNBT.setInteger("MaxTicks", maxTicks);
        compound.setTag("Decay", decayNBT);

        compound.setFloat("FighterRot", fighterRotation);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.nickname = compound.getString("Nickname");
        this.rarity = BlockPowerCube.CubeRarity.values()[compound.getInteger("Rarity")];
        this.baseRFRate = compound.getInteger("BaseRFRate");

        NBTTagCompound decayNBT = compound.getCompoundTag("Decay");
        this.remainingTicks = decayNBT.getInteger("RemainingTicks");
        this.maxTicks = decayNBT.getInteger("MaxTicks");

        this.fighterRotation = compound.getFloat("FighterRot");
    }

}
