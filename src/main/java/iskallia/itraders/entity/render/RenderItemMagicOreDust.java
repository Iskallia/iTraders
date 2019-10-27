package iskallia.itraders.entity.render;

import java.awt.Color;

import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import iskallia.itraders.entity.EntityItemMagicOreDust;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderItemMagicOreDust extends RenderEntityItem {

    protected static IRenderFactory renderFactory = new RenderItemMagicOreDust.Factory();

    public RenderItemMagicOreDust(RenderManager renderManagerIn) {
        super(renderManagerIn, Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        float yOffset = shouldBob() ? MathHelper.sin(((float) entity.getAge() + partialTicks) / 10.0F + entity.hoverStart) * 0.1F + 0.1F : 0;
        RenderingUtils.renderLightRayEffects(x, y + 0.3 + yOffset, z, Color.GREEN, 16024L, entity.getAge(), 20, 17, 17);
    }

    public static IRenderFactory getRenderFactory() {
        return RenderItemMagicOreDust.renderFactory;
    }

    public static class Factory implements IRenderFactory<EntityItemMagicOreDust> {
        @Override
        public Render<EntityItem> createRenderFor(RenderManager manager) {
            return new RenderItemMagicOreDust(manager);
        }
    }
}
