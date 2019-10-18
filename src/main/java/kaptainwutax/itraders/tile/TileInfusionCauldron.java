package kaptainwutax.itraders.tile;

import hellfirepvp.astralsorcery.client.effect.EffectHandler;
import hellfirepvp.astralsorcery.client.effect.texture.TextureSpritePlane;
import hellfirepvp.astralsorcery.client.util.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import kaptainwutax.itraders.client.SpriteHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class: TileInfusionCauldron
 * Created by HellFirePvP
 * Date: 18.10.2019 / 06:25
 */
public class TileInfusionCauldron extends TileEntity implements ITickable {

    private Object effectRef;

    @Override
    public void update() {
        if (world.isRemote) {
            playRitualEffect();
        }
    }

    @SideOnly(Side.CLIENT)
    private void playRitualEffect() {
        TextureSpritePlane spr = (TextureSpritePlane) effectRef;
        if (spr == null || spr.canRemove() || spr.isRemoved()) {
            SpriteSheetResource sprite = SpriteHelper.loadEffectSheet("halo_cauldron", 6, 8);
            spr = EffectHandler.getInstance().textureSpritePlane(sprite, Vector3.RotAxis.Y_AXIS.clone());
            spr.setPosition(new Vector3(this).add(0.5, 0.06, 0.5));
            spr.setAlphaOverDistance(false);
            spr.setNoRotation(45);
            spr.setRefreshFunc(() -> {
                if (isInvalid()) {
                    return false;
                }
                if (this.getWorld().provider == null || Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().world.provider == null) {
                    return false;
                }
                return this.getWorld().provider.getDimension() == Minecraft.getMinecraft().world.provider.getDimension();
            });
            spr.setScale(3F);
            effectRef = spr;
        }
    }
}