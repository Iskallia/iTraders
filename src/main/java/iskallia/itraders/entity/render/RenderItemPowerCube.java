package iskallia.itraders.entity.render;

import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import iskallia.itraders.block.BlockPowerCube;
import iskallia.itraders.entity.EntityItemPowerCube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import java.awt.*;

public class RenderItemPowerCube extends RenderEntityItem {

    private static IRenderFactory renderFactory = new RenderItemPowerCube.Factory();

    public RenderItemPowerCube(RenderManager renderManagerIn) {
        super(renderManagerIn, Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        ItemStack itemStack = entity.getItem();
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

        float yOffset = shouldBob() ? MathHelper.sin(((float) entity.getAge() + partialTicks) / 10.0F + entity.hoverStart) * 0.1F + 0.1F : 0;
        RenderingUtils.renderLightRayEffects(x, y + 0.3 + yOffset, z, color, 16024L, entity.getAge(), 20, 17, 17);
    }

    public static IRenderFactory getRenderFactory() {
        return renderFactory;
    }

    public static class Factory implements IRenderFactory<EntityItemPowerCube> {

        @Override
        public Render<? super EntityItemPowerCube> createRenderFor(RenderManager manager) {
            return new RenderItemPowerCube(manager);
        }

    }

}
