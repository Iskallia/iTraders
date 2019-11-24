package iskallia.itraders.gui.container;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityCubeChamber;
import iskallia.itraders.container.ContainerCubeChamber;
import iskallia.itraders.init.InitPacket;
import iskallia.itraders.net.packet.C2SCubeChamberStart;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

public class GuiContainerCubeChamber extends GuiContainer {

    public static final ResourceLocation CUBE_CHAMBER_GUI_TEXTURE = Traders.getResource("textures/gui/container/cube_chamber.png");

    private TileEntityCubeChamber cubeChamber;

    private GuiButton startButton;

    public GuiContainerCubeChamber(World world, EntityPlayer player, TileEntityCubeChamber cubeChamber) {
        super(new ContainerCubeChamber(world, player, cubeChamber));
        this.cubeChamber = cubeChamber;
    }

    @Override
    public void initGui() {
        super.initGui();

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        // Start button
        String startButtonText = "Infuse";
        this.startButton = new GuiButton(0,
                startX + 115, startY + 56,
                this.fontRenderer.getStringWidth(startButtonText) + 10, 20,
                startButtonText);
    }

    /* ----------------------------- */

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.startButton.mousePressed(this.mc, mouseX, mouseY)) {
            InitPacket.PIPELINE.sendToServer(new C2SCubeChamberStart(cubeChamber));
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /* ----------------------------- */

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        String blockName = "Cube Chamber";

        this.fontRenderer.drawString(blockName,
                startX + (this.xSize - this.fontRenderer.getStringWidth(blockName)) / 2, startY + 6, 4210752);

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        this.startButton.enabled = !cubeChamber.getInventoryHandler().getStackInSlot(0).isEmpty()
                && cubeChamber.getInventoryHandler().getStackInSlot(2).isEmpty();

        this.startButton.drawButton(this.mc, mouseX, mouseY, partialTicks);

        this.mc.getTextureManager().bindTexture(CUBE_CHAMBER_GUI_TEXTURE);

        int powerScaled = 69 - getPowerScaled(69);
        this.drawTexturedModalRect(startX + 9, startY + 8 + powerScaled,
                178, 22 + powerScaled,
                17, 69 - powerScaled);

        this.fontRenderer.drawString("RF",
                startX + 12, startY + 67, 4210752);

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        this.mc.getTextureManager().bindTexture(CUBE_CHAMBER_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    /* ----------------------------- */

    public int getPowerScaled(int pixels) {
        int current = cubeChamber.getEnergyStorage().getEnergyStored();
        int max = cubeChamber.getEnergyStorage().getMaxEnergyStored();
        return current != 0 && max != 0 ? current * pixels / max : 0;
    }

}
