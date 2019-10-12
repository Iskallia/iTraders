package kaptainwutax.itraders.gui.container;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.container.ContainerEggPouch;
import kaptainwutax.itraders.gui.component.GuiScrollbar;
import kaptainwutax.itraders.init.InitPacket;
import kaptainwutax.itraders.net.packet.C2SMovePouchRow;
import kaptainwutax.itraders.net.packet.C2SUpdatePouchSearch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiContainerEggPouch extends GuiContainer {

    private static Minecraft MINECRAFT = Minecraft.getMinecraft();
    private static final ResourceLocation INVENTORY_TEXTURE = new ResourceLocation(Traders.MOD_ID, "textures/gui/container/pouch_gui.png");

    public GuiScrollbar scrollbar;
    public GuiTextField searchField;

    private String lastSearch = "";
    
    public GuiContainerEggPouch(World world, EntityPlayer player) {
        super(new ContainerEggPouch(world, player));
        this.xSize = 186;
        this.ySize = 222;
    }

    @Override
    public void initGui() {
        super.initGui();

        // Search bar
        this.searchField = new GuiTextField(0, this.fontRenderer,
                (this.width / 2) - 68,
                (this.height / 2) - 100,
                140, this.fontRenderer.FONT_HEIGHT);
        this.searchField.setMaxStringLength(60);
        this.searchField.setText("");
        this.searchField.setVisible(true);
        this.searchField.setTextColor(0xFFFFFF);
        this.searchField.setEnabled(true);
        this.searchField.setEnableBackgroundDrawing(false);

        // Scroll bar
        this.scrollbar = new GuiScrollbar(
                (this.width / 2) + 79,
                (this.height / 2) - 84,
                107
        );
        
        this.scrollbar.setKnobTexture(INVENTORY_TEXTURE, 188, 3, 8);
        this.scrollbar.setKnobUpperUV(188, 0, 2);
        this.scrollbar.setKnobLowerUV(188, 111, 2);

        InitPacket.PIPELINE.sendToServer(new C2SMovePouchRow(0));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        this.scrollbar.drawScrollbar();

        this.searchField.drawTextBox();

        this.scrollbar.drawScrollbar();
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.scrollbar.mouseClicked(mouseX, mouseY, mouseButton);
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.scrollbar.mouseReleased(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(!this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        } else {
            InitPacket.PIPELINE.sendToServer(new C2SUpdatePouchSearch(this.searchField.getText()));
            ((ContainerEggPouch)this.inventorySlots).pouchInventory.setSearchQuery(this.searchField.getText());
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        int mouseX = (int) (Mouse.getX() * ((float) this.width / MINECRAFT.displayWidth));
        int mouseY = this.height - (int) (Mouse.getY() * ((float) this.height / MINECRAFT.displayHeight)) - 1;
        int scroll = Mouse.getDWheel();
        int rawMove = 0;

        this.scrollbar.mouseMoved(mouseX, mouseY);

        while (scroll >= 120) {
            scroll -= 120;
            rawMove--;
        }

        while (scroll <= -120) {
            scroll += 120;
            rawMove++;
        }

        if(rawMove != 0)InitPacket.PIPELINE.sendToServer(new C2SMovePouchRow(rawMove));
        ((ContainerEggPouch)this.inventorySlots).pouchInventory.move(rawMove);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();

        MINECRAFT.getTextureManager().bindTexture(INVENTORY_TEXTURE);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    public void setScroll(int currentScroll, int totalScroll) {
        this.scrollbar.setScroll(
                Math.max(currentScroll, 1),
                Math.max(totalScroll, 1)
        );

        ((ContainerEggPouch)this.inventorySlots).pouchInventory.currentScroll = Math.max(currentScroll, 1);
        ((ContainerEggPouch)this.inventorySlots).pouchInventory.totalScroll = Math.max(totalScroll, 1);
    }

}
