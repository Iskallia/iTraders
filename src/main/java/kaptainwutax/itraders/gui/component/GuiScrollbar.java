package kaptainwutax.itraders.gui.component;

import net.minecraft.client.gui.Gui;

public class GuiScrollbar extends Gui {

    private int x, y;

    private int currentScroll;
    private int totalScroll;

    public GuiScrollbar(int x, int y) {
        this.x = x;
        this.y = y;
        this.currentScroll = 1;
        this.totalScroll = 1;
    }

    public void updateScroll(int currentScroll, int totalScroll) {
        this.currentScroll = currentScroll;
        this.totalScroll = totalScroll;
    }

    public void drawScrollbar() {
        int scrollBarHeight = 109;
        float scrollBarUnit = ((float) scrollBarHeight) / (6 + this.totalScroll - 1);

        int knobX = x;
        int knobY1 = (int) (y + (scrollBarUnit * (this.currentScroll - 1)));
        int knobY2 = (int) (knobY1 + (6 * scrollBarUnit));

        this.drawVerticalLine(knobX + 1, knobY1, knobY2, 0xFF_C3C3C3);
        this.drawVerticalLine(knobX, knobY1, knobY2, 0xFF_000000);
    }

}
