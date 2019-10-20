package kaptainwutax.itraders.entity;

import java.awt.Color;

import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class: EntityItemMagicOreDust
 * Created by HellFirePvP
 * Date: 18.10.2019 / 20:59
 */
public class EntityItemMagicOreDust extends EntityItem {

    public EntityItemMagicOreDust(World worldIn) {
        super(worldIn);
    }

    public EntityItemMagicOreDust(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityItemMagicOreDust(World worldIn, double x, double y, double z, ItemStack stack) {
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
        for (int i = 0; i < 3; i++) {
            if (rand.nextInt(6) != 0) {
                continue;
            }
            Vector3 at = Vector3.atEntityCorner(this)
                    .add(-0.35 + rand.nextFloat() * 0.7, rand.nextFloat() * 0.7, -0.35 + rand.nextFloat() * 0.7);
            EntityFXFacingParticle p = EffectHelper.genericFlareParticle(at);
            p.setColor(Color.GREEN)
                    .enableAlphaFade(EntityComplexFX.AlphaFunction.FADE_OUT)
                    .gravity(0.004F)
                    .scale(0.08F + rand.nextFloat() * 0.07F)
                    .setMaxAge(7 + rand.nextInt(6));
        }
    }
}
