package kaptainwutax.itraders.gui.container;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;

import kaptainwutax.itraders.container.ContainerEggPouch;
import kaptainwutax.itraders.init.InitPacket;
import kaptainwutax.itraders.net.packet.C2SMovePouchRow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.io.IOException;

public class GuiContainerEggPouch extends GuiContainer {

    private static Minecraft MINECRAFT = Minecraft.getMinecraft();
    private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/generic_54.png");

    private int currentScroll = 1;
    private int totalScroll = 1;
    private GuiTextField searchField;

    public GuiContainerEggPouch(World world, EntityPlayer player) {
        super(new ContainerEggPouch(world, player));
        this.xSize = 176;
        this.ySize = 222;
    }

    @Override
    public void initGui() {
        super.initGui();

        this.searchField = new GuiTextField(0, this.fontRenderer,
                (this.width / 2) - 75,
                (this.height / 2) - 105,
                80, this.fontRenderer.FONT_HEIGHT);
        this.searchField.setMaxStringLength(50);
        this.searchField.setEnableBackgroundDrawing(true);
        this.searchField.setText("");
        this.searchField.setVisible(true);
        this.searchField.setTextColor(16777215);

        InitPacket.PIPELINE.sendToServer(new C2SMovePouchRow(0));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.searchField.drawTextBox();

        this.drawHoveringText(this.currentScroll + "/" + this.totalScroll,
                (this.width / 2) + 54,
                (this.height / 2) - 95);

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!this.searchField.textboxKeyTyped(typedChar, keyCode))
            super.keyTyped(typedChar, keyCode);
        else {
        	// TODO filter items & update container
		}
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        int scroll = Mouse.getDWheel();
        int rawMove = 0;

        while (scroll >= 120) {
            scroll -= 120;
            rawMove--;
        }

        while (scroll <= -120) {
            scroll += 120;
            rawMove++;
        }

        if (rawMove != 0) InitPacket.PIPELINE.sendToServer(new C2SMovePouchRow(rawMove));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();

        MINECRAFT.getTextureManager().bindTexture(INVENTORY_TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    public void setScroll(int currentScroll, int totalScroll) {
        this.currentScroll = Math.max(currentScroll, 1);
        this.totalScroll = Math.max(totalScroll, 1);
    }

}
