package kaptainwutax.itraders.gui.container;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.gui.component.GuiScrollbar;
import kaptainwutax.itraders.net.packet.C2SUpdatePouchSearch;
import net.minecraft.client.gui.GuiTextField;
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
    private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation(Traders.MOD_ID, "textures/gui/container/pouch_gui.png");

    private GuiScrollbar scrollbar;
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
                (this.width / 2) - 62,
                (this.height / 2) - 100,
                140, this.fontRenderer.FONT_HEIGHT);
        this.searchField.setMaxStringLength(60);
        this.searchField.setText("");
        this.searchField.setVisible(true);
        this.searchField.setTextColor(0xFF_FFFFFF);
        this.searchField.setEnabled(true);
        this.searchField.setEnableBackgroundDrawing(false);

        this.scrollbar = new GuiScrollbar(
                (this.width / 2) + 82,
                (this.height / 2) - 85
        );

        InitPacket.PIPELINE.sendToServer(new C2SMovePouchRow(0));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.searchField.drawTextBox();

        this.drawString(this.fontRenderer,
                "WIP: Search",
                (this.width / 2) - 80,
                (this.height / 2) - 120,
                0xFF_FFFFFF);

        if (!((ContainerEggPouch) this.inventorySlots).inSearchMode())
            this.scrollbar.drawScrollbar();

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
            // TODO: Enable search to filter inventory
//            InitPacket.PIPELINE.sendToServer(new C2SUpdatePouchSearch(this.searchField.getText()));
//            ContainerEggPouch container = (ContainerEggPouch) this.inventorySlots;
//            container.searchQuery = this.searchField.getText();
//            container.updateSlots();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        int scroll = Mouse.getDWheel();
        int rawMove = 0;

        if (((ContainerEggPouch) this.inventorySlots).inSearchMode())
            return;

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
        this.scrollbar.updateScroll(
                Math.max(currentScroll, 1),
                Math.max(totalScroll, 1)
        );
    }

}
