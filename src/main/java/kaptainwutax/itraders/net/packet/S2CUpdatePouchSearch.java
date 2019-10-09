package kaptainwutax.itraders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.itraders.container.ContainerEggPouch;
import kaptainwutax.itraders.gui.container.GuiContainerEggPouch;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

public class S2CUpdatePouchSearch implements IMessage {

    String searchQuery;

    public S2CUpdatePouchSearch() {}

    public S2CUpdatePouchSearch(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.searchQuery = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.searchQuery.length());
        buf.writeCharSequence(this.searchQuery, StandardCharsets.UTF_8);
    }

    public static class S2CUpdatePouchSearchHandler implements IMessageHandler<S2CUpdatePouchSearch, IMessage> {

        @Override
        public IMessage onMessage(S2CUpdatePouchSearch message, MessageContext ctx) {
            Minecraft minecraft = Minecraft.getMinecraft();

            System.out.printf("Got S2C update pouch %s\n", message.searchQuery);

            if (minecraft.currentScreen instanceof GuiContainerEggPouch) {
                GuiContainerEggPouch gui = (GuiContainerEggPouch) minecraft.currentScreen;
                ContainerEggPouch container = (ContainerEggPouch) gui.inventorySlots;
                container.searchQuery = message.searchQuery;
//                container.updateSlots();
            }

            return null;
        }

    }

}
