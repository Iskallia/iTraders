package iskallia.itraders.gui.container;

import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityGhostPedestal;
import iskallia.itraders.container.ContainerGhostPedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiContainerGhostPedestal extends GuiContainer {

	public static final ResourceLocation PEDESTAL_GUI_TEXTURE = Traders.getResource("textures/gui/container/ghost_pedestal_gui.png");   
	
	public GuiContainerGhostPedestal(World world, EntityPlayer player, TileEntityGhostPedestal pedestal) {
		super(new ContainerGhostPedestal(world, player, pedestal));
        this.allowUserInput = false;
        this.ySize = 133;
	}
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(PEDESTAL_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

}
