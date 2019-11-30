package iskallia.itraders.gui.container;

import java.io.IOException;

import iskallia.itraders.Traders;
import iskallia.itraders.block.BlockCubeChamber;
import iskallia.itraders.block.entity.TileEntityCubeChamber;
import iskallia.itraders.container.ContainerCubeChamber;
import iskallia.itraders.init.InitPacket;
import iskallia.itraders.net.packet.C2SCubeChamberStart;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
        this.startButton = new GuiButton(0, startX + 115, startY + 56, this.fontRenderer.getStringWidth(startButtonText) + 10, 20, startButtonText);
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

        // Draw Block's Name
        String blockName = "Cube Chamber";
        this.fontRenderer.drawString(blockName, startX + (this.xSize - this.fontRenderer.getStringWidth(blockName)) / 2, startY + 6, 4210752);

        // Reset some GL States
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        // Set button's state
        this.startButton.enabled = canStartInfusion();

        // Render button
        this.startButton.drawButton(this.mc, mouseX, mouseY, partialTicks);

        // Render hovering text of RF Indicator TODO: Extract to a new GUI component
        if (mouseX > startX + 9 && mouseY > startY + 8 && mouseX < startX + 26 && mouseY < startY + 77) {
            this.drawHoveringText("RF: " + this.cubeChamber.getEnergyStorage().getEnergyStored(), mouseX, mouseY + 10);
        }

        // TODO: Reset some GL color state before drawing string

        // Render hovered tooltip
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected boolean canStartInfusion() {
        if (cubeChamber.state == TileEntityCubeChamber.CubeChamberStates.PROCESSING)
            return false;

        ItemStack eggStack = cubeChamber.getInventoryHandler().getStackInSlot(0);
        ItemStack outputStack = cubeChamber.getInventoryHandler().getStackInSlot(2);

        return !eggStack.isEmpty() && outputStack.isEmpty();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        this.mc.getTextureManager().bindTexture(CUBE_CHAMBER_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        // Bind GUI Sprite
        this.mc.getTextureManager().bindTexture(CUBE_CHAMBER_GUI_TEXTURE);

        // TODO: (93,30) -> (176,2)
        // Render current infusion progress, if still processing
        if (cubeChamber.state == TileEntityCubeChamber.CubeChamberStates.PROCESSING) {
            int infusionScaled = 24 - getInfusionScaled(24);
            this.drawTexturedModalRect(
                    93, 30, // X, Y
                    176, 2, // U, V
                    infusionScaled, 17 // W, H
            );
        }


        // Render RF Indicator TODO: Extract to a new GUI component
        int powerScaled = 69 - getPowerScaled(69);
        this.drawTexturedModalRect(
                9, 8 + powerScaled,
                178, 22 + powerScaled,
                17, 69 - powerScaled
        );
        this.fontRenderer.drawString("RF", 12, 67, 0x404040);
    }

    /* ----------------------------- */

    public int getPowerScaled(int pixels) {
        int current = cubeChamber.getEnergyStorage().getEnergyStored();
        int max = cubeChamber.getEnergyStorage().getMaxEnergyStored();
        return current != 0 && max != 0 ? current * pixels / max : 0;
    }

    public int getInfusionScaled(int pixes) {
        int current = cubeChamber.getRemainingTicks();
        int max = TileEntityCubeChamber.REQUIRED_PROCESS_TICKS;
        return current != 0 ? current * pixes / max : 0;
    }

}
