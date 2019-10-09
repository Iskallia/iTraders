package kaptainwutax.itraders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.itraders.container.ContainerEggPouch;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;

public class C2SUpdatePouch implements IMessage {

    String searchQuery;

    public C2SUpdatePouch() {}

    public C2SUpdatePouch(String searchQuery) {
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

    public static class C2SUpdatePouchHandler implements IMessageHandler<C2SUpdatePouch, S2CUpdatePouch> {

        @Override
        public S2CUpdatePouch onMessage(C2SUpdatePouch message, MessageContext ctx) {
            EntityPlayerMP playerMP = ctx.getServerHandler().player;

            System.out.printf("Got C2S update pouch\n");

            if (playerMP.openContainer instanceof ContainerEggPouch) {
                ContainerEggPouch container = (ContainerEggPouch) playerMP.openContainer;
                container.searchQuery = message.searchQuery;
                container.updateSlots();
                return new S2CUpdatePouch(message.searchQuery);
            }

            return null;
        }

    }

}
