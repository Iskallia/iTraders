package iskallia.itraders.block.render;

import iskallia.itraders.Traders;
import iskallia.itraders.block.BlockCubeChamber;
import iskallia.itraders.block.entity.TileEntityCubeChamber;
import iskallia.itraders.item.ItemSpawnEggFighter;
import iskallia.itraders.util.math.Randomizer;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class TESRCubeChamber extends TileEntitySpecialRenderer<TileEntityCubeChamber> {

    public static ResourceLocation PROGRESS_BAR_TEXTURE
            = Traders.getResource("textures/blocks/cube_chamber_progress_bar.png");

    private static ModelPlayer playerModel = new ModelPlayer(.1f, false);
    private static Entity dummyEntity;

    @Override
    public void render(TileEntityCubeChamber tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.hasWorld())
            return;

        ItemStack eggStack = tileEntity.getInventoryHandler().getStackInSlot(TileEntityCubeChamber.INPUT_SLOT);

        if (eggStack != ItemStack.EMPTY)
            renderFighter(eggStack, tileEntity, x, y, z);

        if (tileEntity.state != TileEntityCubeChamber.CubeChamberStates.IDLE)
            renderProgressBar(tileEntity, x, y, z);
    }

    protected void renderFighter(ItemStack eggStack, TileEntityCubeChamber tileEntity, double x, double y, double z) {
        if (dummyEntity == null)
            initDummyEntity(tileEntity.getWorld());

        String nickname = ItemSpawnEggFighter.getNickname(eggStack);

        if (nickname == null)
            return;

        // Re-fetch skin if necessary
        String latestNickname = tileEntity.skin.getLatestNickname();
        if (latestNickname == null || !latestNickname.equals(nickname)) {
            tileEntity.skin.updateSkin(nickname);
        }

        bindTexture(!nickname.isEmpty()
                ? tileEntity.skin.getLocationSkin()
                : DefaultPlayerSkin.getDefaultSkinLegacy());

        long elapsedTime = System.currentTimeMillis() - tileEntity.animationStartTime;

        double scale = 0.01;
        float rotation = (float) ((elapsedTime * 0.025d) % 360);
        float yOffset = (float) (Math.sin(elapsedTime * 0.001d)) * 1 / 16f;

        if (dummyEntity != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5f, y + yOffset + 1.5f, z + 0.5f);
            GlStateManager.rotate(rotation, 0f, 1f, 0f);
            GlStateManager.rotate(180f, 0f, 0f, 1f);
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(0, -scale * 125, 0);
            playerModel.bipedHead.render(1f);
            playerModel.bipedBody.render(1f);
            playerModel.bipedLeftLeg.render(1.02f);
            playerModel.bipedRightLeg.render(1.02f);
            playerModel.bipedLeftArm.render(1.05f);
            playerModel.bipedRightArm.render(1.05f);

            playerModel.bipedBodyWear.render(1f);
            playerModel.bipedHeadwear.render(1f);
            playerModel.bipedLeftLegwear.render(1.02f);
            playerModel.bipedRightLegwear.render(1.02f);
            playerModel.bipedLeftArmwear.render(1.05f);
            GlStateManager.translate(0f, 0f, -11.05f); // No idea why right arm-wear isn't orienting correctly :/
            playerModel.bipedRightArmwear.render(1.05f);
            GlStateManager.popMatrix();
        }
    }

    protected void renderProgressBar(TileEntityCubeChamber tileEntity, double x, double y, double z) {
        float infusionPercentage = tileEntity.getInfusionPercentage();

        EnumFacing facing = tileEntity.getBlockState().getValue(BlockCubeChamber.FACING);

        final float marginSide = 3 / 16f;
        final float length = 10 / 16f * infusionPercentage;
        final float marginTop = 3 / 16f;
        final float height = 3 / 16f;

        bindTexture(PROGRESS_BAR_TEXTURE);

        GlStateManager.pushMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

        GlStateManager.translate(x + 0.5f, y + 0.5f, z + 0.5f);
        GlStateManager.rotate(-facing.getHorizontalAngle() + 180, 0f, 1f, 0f);
        GlStateManager.translate(-0.5f, -0.5f, -0.5f - (1 / 16f) * 0.2f);

        bufferBuilder.pos(1f - (marginSide + length), 1f - (marginTop + height), 0f)
                .tex(length, height)
                .lightmap(0xF0, 0xF0)
                .color(1f, 1f, 1f, 1f)
                .endVertex();

        bufferBuilder.pos(1f - (marginSide + length), 1f - marginTop, 0f)
                .tex(length, 0f)
                .lightmap(0xF0, 0xF0)
                .color(1f, 1f, 1f, 1f)
                .endVertex();

        bufferBuilder.pos(1f - marginSide, 1f - marginTop, 0f)
                .tex(0, 0)
                .lightmap(0xF0, 0xF0)
                .color(1f, 1f, 1f, 1f)
                .endVertex();

        bufferBuilder.pos(1f - marginSide, 1f - (marginTop + height), 0f)
                .tex(0f, height)
                .lightmap(0xF0, 0xF0)
                .color(1f, 1f, 1f, 1f)
                .endVertex();

        tessellator.draw();

        GlStateManager.popMatrix();
    }

    private void initDummyEntity(World world) {
        dummyEntity = new Entity(world) {
            @Override
            protected void entityInit() {}

            @Override
            protected void readEntityFromNBT(NBTTagCompound compound) {}

            @Override
            protected void writeEntityToNBT(NBTTagCompound compound) {}
        };
        dummyEntity.posX = 0;
        dummyEntity.posY = 0;
        dummyEntity.posZ = 0;
    }

}
