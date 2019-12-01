package iskallia.itraders.entity;

import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import iskallia.itraders.block.BlockPowerCube;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class EntityItemPowerCube extends EntityItem {

    public EntityItemPowerCube(World worldIn) {
        super(worldIn);
    }

    public EntityItemPowerCube(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityItemPowerCube(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if (world.isRemote) {
            playEffects();
        }
    }

    @SideOnly(Side.CLIENT)
    private void playEffects() {
        ItemStack itemStack = getItem();
        NBTTagCompound stackNBT = itemStack.getTagCompound();
        if (stackNBT == null) stackNBT = new NBTTagCompound();
        BlockPowerCube.CubeRarity rarity = BlockPowerCube.CubeRarity.values()[stackNBT.getInteger("Rarity")];

        Color color;

        switch (rarity) {
            case RARE:
                color = new Color(0xFF_FDE802);
                break;
            case EPIC:
                color = new Color(0xFF_DE2CD8);
                break;
            case MEGA:
                color = new Color(0xFF_85FA0F);
                break;
            default:
                color = new Color(0xFF_EBEBEB);
                break;
        }

        for (int i = 0; i < 3; i++) {
            if (rand.nextInt(6) != 0) {
                continue;
            }
            Vector3 at = Vector3.atEntityCorner(this)
                    .add(-0.35 + rand.nextFloat() * 0.7, rand.nextFloat() * 0.7, -0.35 + rand.nextFloat() * 0.7);
            EntityFXFacingParticle p = EffectHelper.genericFlareParticle(at);
            p.setColor(color)
                    .enableAlphaFade(EntityComplexFX.AlphaFunction.FADE_OUT)
                    .gravity(0.004F)
                    .scale(0.08F + rand.nextFloat() * 0.07F)
                    .setMaxAge(7 + rand.nextInt(6));
        }
    }

}
