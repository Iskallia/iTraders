package kaptainwutax.itraders.gui.component;

import kaptainwutax.itraders.init.InitPacket;
import kaptainwutax.itraders.net.packet.C2SMovePouchRow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class GuiScrollbar extends Gui {

    private static Minecraft MINECRAFT = Minecraft.getMinecraft();

    private enum GrabState {DEFAULT, HOVERED, GRABBED}

    private int x, y;
    private int scrollHeight;

    private int currentScroll;
    private int totalScroll;

    private ResourceLocation knobTexture;
    private int knobU, knobV;
    private int knobWidth;
    private int knobUpperU, knobUpperV;
    private int knobLowerU, knobLowerV;
    private int knobUpperH, knobLowerH;

    private GrabState grabState;
    private int grabX, grabY;

    public GuiScrollbar(int x, int y, int scrollHeight) {
        this.x = x;
        this.y = y;
        this.currentScroll = 1;
        this.totalScroll = 1;
        this.scrollHeight = scrollHeight;
        this.grabState = GrabState.DEFAULT;
    }

    public float getScrollbarUnits() {
        return ((float) scrollHeight) / (6 + totalScroll - 1);
    }

    public void setKnobTexture(ResourceLocation texture, int u, int v, int width) {
        this.knobTexture = texture;
        this.knobU = u;
        this.knobV = v;
        this.knobWidth = width;
    }

    public void setKnobUpperUV(int u, int v, int h) {
        this.knobUpperU = u;
        this.knobUpperV = v;
        this.knobUpperH = h;
    }

    public void setKnobLowerUV(int u, int v, int h) {
        this.knobLowerU = u;
        this.knobLowerV = v;
        this.knobLowerH = h;
    }

    public void setScroll(int currentScroll, int totalScroll) {
        this.currentScroll = currentScroll;
        this.totalScroll = totalScroll;
    }

    public void mouseMoved(int mouseX, int mouseY) {
        if (grabState == GrabState.GRABBED) {
            float scrollbarUnits = getScrollbarUnits();
            int dy = mouseY - grabY;
            int rawMove = (int) (dy / scrollbarUnits);
            if (rawMove != 0) {
                InitPacket.PIPELINE.sendToServer(new C2SMovePouchRow(rawMove));
                grabY = mouseY;
            }
            return;
        }

        if (intersects(mouseX, mouseY)) {
            grabState = GrabState.HOVERED;
            return;
        }

        grabState = GrabState.DEFAULT;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton != 0)
            return; // 0 = Primary button

        if (intersects(mouseX, mouseY)) {
            grabState = GrabState.GRABBED;
            grabX = mouseX;
            grabY = mouseY;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (grabState == GrabState.GRABBED) {
            grabState = GrabState.DEFAULT;
        }
    }

    private boolean intersects(int x, int y) {
        float scrollBarUnit = getScrollbarUnits();

        int knobX = this.x;
        int knobY = this.y + (int) ((currentScroll - 1) * scrollBarUnit);
        int knobHeight = (int) (6 * scrollBarUnit); // 6 for 6 slot columns

        int scrollX = knobX - knobUpperH + 2;
        int scrollY = knobY - 2;
        int scrollW = knobWidth;
        int scrollH = knobHeight + knobUpperH + knobLowerH;

        return scrollX <= x && x <= scrollX + scrollW
                && scrollY <= y && y <= scrollY + scrollH;
    }

    public void drawScrollbar() {
        float scrollbarUnit = getScrollbarUnits();

        int knobX = x;
        int knobY = y + (int) ((currentScroll - 1) * scrollbarUnit);
        int knobHeight = (int) (6 * scrollbarUnit); // 6 for 6 slot columns

        MINECRAFT.getTextureManager().bindTexture(knobTexture);

        int uOffset;
        switch (grabState) {
            case GRABBED: uOffset = 2 * (2 + knobWidth); break;
            case HOVERED: uOffset = 2 + knobWidth; break;
            default: uOffset = 0; break;
        }

        this.drawTexturedModalRect(knobX, knobY,
                knobU + uOffset, knobV,
                knobWidth, knobHeight);
        this.drawTexturedModalRect(knobX, knobY - knobUpperH,
                knobUpperU + uOffset, knobUpperV,
                knobWidth, knobUpperH);
        this.drawTexturedModalRect(knobX, knobY + knobHeight,
                knobLowerU + uOffset, knobLowerV,
                knobWidth, knobLowerH);
    }

}
