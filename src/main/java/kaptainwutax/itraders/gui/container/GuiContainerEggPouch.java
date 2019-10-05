package kaptainwutax.itraders.gui.container;

import kaptainwutax.itraders.container.ContainerEggPouch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiContainerEggPouch extends GuiContainer {
	
	private static Minecraft MINECRAFT = Minecraft.getMinecraft();
    private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

	public GuiContainerEggPouch(World world, EntityPlayer player) {
		super(new ContainerEggPouch(world, player));
		this.xSize = 176;
		this.ySize = 222;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		
		MINECRAFT.getTextureManager().bindTexture(INVENTORY_TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);	
	}

}
