package iskallia.itraders.gui.container;

import iskallia.itraders.Traders;
import iskallia.itraders.container.ContainerReactor;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorCore;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(INVENTORY_TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Render multi-block's name
        String blockName = "Reactor";
        this.fontRenderer.drawString(blockName,
                this.guiLeft + (this.xSize - this.fontRenderer.getStringWidth(blockName)) / 2,
                this.guiTop + 6, 4210752);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        // Render hovered tooltip
        this.renderHoveredToolTip(mouseX - this.guiLeft, mouseY - this.guiTop);
    }
}
