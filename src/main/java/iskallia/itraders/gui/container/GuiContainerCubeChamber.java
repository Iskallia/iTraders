package iskallia.itraders.gui.container;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import iskallia.itraders.Traders;
import iskallia.itraders.block.BlockCubeChamber;
import iskallia.itraders.block.entity.TileEntityCubeChamber;
import iskallia.itraders.container.ContainerCubeChamber;
import iskallia.itraders.init.InitPacket;
import iskallia.itraders.item.ItemBooster;
import iskallia.itraders.net.packet.C2SCubeChamberStart;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
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
        this.startButton = new GuiButton(0,
                startX + 115, startY + 53,
                this.fontRenderer.getStringWidth(startButtonText) + 10, 20,
                startButtonText);
    }

    public void updateGui() {
        this.startButton.enabled = canStartInfusion();
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
    public void updateScreen() {
        this.updateGui();
        super.updateScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /* ----------------------------- */

    protected boolean canStartInfusion() {
        if (cubeChamber.state == TileEntityCubeChamber.CubeChamberStates.PROCESSING)
            return false;

        ItemStack eggStack = cubeChamber.getInventoryHandler().getStackInSlot(0);
        ItemStack outputStack = cubeChamber.getOutputHandler().getStackInSlot(2);

        return !eggStack.isEmpty() && outputStack.isEmpty();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        this.mc.getTextureManager().bindTexture(CUBE_CHAMBER_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        // TODO: (93,30) -> (176,2)
        // Render current infusion progress, if still processing
        this.mc.getTextureManager().bindTexture(CUBE_CHAMBER_GUI_TEXTURE);
        if (cubeChamber.state == TileEntityCubeChamber.CubeChamberStates.PROCESSING) {
            int infusionScaled = 24 - getInfusionScaled(24);
            this.drawTexturedModalRect(
                    startX + 93, startY + 21, // X, Y
                    176, 2, // U, V
                    infusionScaled, 17 // W, H
            );
        }

        // Render Chance Indicator
        ItemStack boosterStack = cubeChamber.getInventoryHandler().getStackInSlot(TileEntityCubeChamber.BOOSTER_SLOT);
        double successRate = ItemBooster.getSuccessRate(boosterStack);
        int chanceScaled = getChanceScaled(41);
        this.mc.getTextureManager().bindTexture(CUBE_CHAMBER_GUI_TEXTURE);
        this.drawTexturedModalRect(
                startX + 42, startY + 44,
                202, 2,
                chanceScaled, 7
        );

        // Render RF Indicator
        int powerScaled = 68 - getPowerScaled(68);
        this.mc.getTextureManager().bindTexture(CUBE_CHAMBER_GUI_TEXTURE);
        this.drawTexturedModalRect(
                startX + 9, startY + 9 + powerScaled, // X, Y
                176, 20 + powerScaled, // U, V
                6, 68 - powerScaled // W, H
        );

        // Draw Typographic elements
        String blockName = "Cube Chamber";
        this.fontRenderer.drawString(blockName, startX + (this.xSize - this.fontRenderer.getStringWidth(blockName)) / 2, startY + 6, 4210752);

//        this.fontRenderer.drawString("RF", startX + 12, startY + 67, 0x000000);

        this.fontRenderer.drawString(
                "Success: " + (int) (100 * successRate) + "%",
                startX + 35, startY + 54, 0x0F0F0F
        );

//        this.fontRenderer.drawString(
//                "Failure: " + (int) (100 - 100 * successRate) + "%",
//                startX + 37, startY + 64, 0xFF6562
//        );

//        this.fontRenderer.drawString(TextFormatting.GREEN + "" + (100 * successRate) + "% / "
//                        + TextFormatting.RED + "" + (100 - 100 * successRate) + "%",
//                startX + 42, startY + 54, 0x1515FF);

        // Render button
        this.startButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        // Render hovering text of RF Indicator
        if (mouseX > startX + 9 && mouseY > startY + 9
                && mouseX < startX + 14 && mouseY < startY + 76) {
            List<String> tooltipLines = new LinkedList<>();
            tooltipLines.add(TextFormatting.RED + "" + this.cubeChamber.getEnergyStorage().getEnergyStored() + " RF");
            tooltipLines.add("");
            tooltipLines.add(TextFormatting.BLUE + "Accepts: " + TileEntityCubeChamber.MAX_RECEIVE + " RF/tick");
            this.drawHoveringText(tooltipLines, mouseX - startX, mouseY - startY);
        }

        // Render hovering text of Chance Indicator
        if (mouseX > startX + 42 && mouseY > startY + 44
                && mouseX < startX + 82 && mouseY < startY + 50) {
            ItemStack boosterStack = cubeChamber.getInventoryHandler().getStackInSlot(TileEntityCubeChamber.BOOSTER_SLOT);
            double successRate = ItemBooster.getSuccessRate(boosterStack);

            List<String> tooltipLines = new LinkedList<>();
            tooltipLines.add(TextFormatting.GREEN + "Infusion Success Rate: " + (int) (successRate * 100) + "%");
            tooltipLines.add(TextFormatting.RED + "Infusion Failure Rate: " + (int) (100 - 100 * successRate) + "%");

            this.drawHoveringText(tooltipLines, mouseX - startX, mouseY - startY);
        }

        // Render hovering text of Progress Indicator
        if (mouseX > startX + 94 && mouseY > startY + 21
                && mouseX < startX + 115 && mouseY < startY + 35) {
            this.drawHoveringText(TextFormatting.BLUE + "Infusion consumes "
                            + TileEntityCubeChamber.ENERGY_USAGE_PER_TICK + " RF/tick",
                    mouseX - startX, mouseY - startY);
        }

        // Render hovered tooltip
        this.renderHoveredToolTip(mouseX - startX, mouseY - startY);
    }

    /* ----------------------------- */

    public int getPowerScaled(int pixels) {
        int current = cubeChamber.getEnergyStorage().getEnergyStored();
        int max = cubeChamber.getEnergyStorage().getMaxEnergyStored();
        return current != 0 && max != 0 ? current * pixels / max : 0;
    }

    public int getInfusionScaled(int pixels) {
        int current = cubeChamber.getRemainingTicks();
        int max = TileEntityCubeChamber.REQUIRED_PROCESS_TICKS;
        return current != 0 ? current * pixels / max : 0;
    }

    public int getChanceScaled(int pixels) {
        ItemStack boosterStack = cubeChamber.getInventoryHandler().getStackInSlot(TileEntityCubeChamber.BOOSTER_SLOT);
        double current = ItemBooster.getSuccessRate(boosterStack);
        double max = 1.0; // 100% chance
        return current != 0 ? (int) (current * pixels / max) : 0;
    }

}
