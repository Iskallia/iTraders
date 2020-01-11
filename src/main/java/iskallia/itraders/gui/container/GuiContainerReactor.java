package iskallia.itraders.gui.container;

import iskallia.itraders.Traders;
import iskallia.itraders.container.ContainerReactor;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.init.InitPacket;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorCore;
import iskallia.itraders.net.packet.C2SReactorToggle;
import iskallia.itraders.util.Number2String;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GuiContainerReactor extends GuiContainer {

    private static final ResourceLocation INVENTORY_TEXTURE = Traders.getResource("textures/gui/container/reactor_gui.png");

    private TileEntityReactorCore reactorCore;

    public GuiContainerReactor(World world, EntityPlayer player, TileEntityReactorCore reactorCore) {
        super(new ContainerReactor(world, player, reactorCore));
        this.xSize = 192;
        this.ySize = 189;
        this.reactorCore = reactorCore;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        if (mouseX > startX + 11 && mouseY > startY + 79
                && mouseX < startX + 29 && mouseY < startY + 96) {
            InitPacket.PIPELINE.sendToServer(new C2SReactorToggle(reactorCore));
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(INVENTORY_TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        // Render running icon
        if (reactorCore.isRunning()) {
            this.mc.getTextureManager().bindTexture(INVENTORY_TEXTURE);
            this.drawTexturedModalRect(
                    startX + 11, startY + 79, // X, Y,
                    211, 16,// U, V
                    19, 18// W, H
            );
        }

        // Render stored energy
        int energyStored = reactorCore.getEnergyStorage().getEnergyStored();
        int energyToGenerate = reactorCore.getEnergyToGenerate();
        this.fontRenderer.drawString(Number2String.withCommas(energyStored) + " RF",
                startX + 34, startY + 80, 4210752);
        this.fontRenderer.drawString("+" + Number2String.convert(energyToGenerate) + " RF/tick",
                startX + 34, startY + 90, 4210752);

        // Render multi-block's name
        String blockName = "Power Reactor";
        this.fontRenderer.drawString(blockName,
                this.guiLeft + (this.xSize - this.fontRenderer.getStringWidth(blockName)) / 2,
                this.guiTop + 6, 4210752);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;

        // Render turn on/off
        if (mouseX > startX + 11 && mouseY > startY + 79
                && mouseX < startX + 29 && mouseY < startY + 96) {
            if (!reactorCore.isRunning()) {
                this.mc.getTextureManager().bindTexture(INVENTORY_TEXTURE);
                this.drawTexturedModalRect(
                        11, 79, // X, Y
                        192, 16, // U, V
                        19, 18 // W, H
                );
            }

            this.drawHoveringText("Turn the Reactor " + (reactorCore.isRunning() ? "off" : "on"),
                    mouseX - startX, mouseY - startY);
        }

        // Render hovered tooltip
        this.renderHoveredToolTip(mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    /* ------------------------------------------ */

}
