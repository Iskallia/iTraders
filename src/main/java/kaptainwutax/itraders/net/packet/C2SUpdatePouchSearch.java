package kaptainwutax.itraders.net.packet;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import kaptainwutax.itraders.container.ContainerEggPouch;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SUpdatePouchSearch implements IMessage {

	String searchQuery;

	public C2SUpdatePouchSearch() {
	}

	public C2SUpdatePouchSearch(String searchQuery) {
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

	public static class C2SUpdatePouchSearchHandler
			implements IMessageHandler<C2SUpdatePouchSearch, S2CUpdatePouchSearch> {

		@Override
		public S2CUpdatePouchSearch onMessage(C2SUpdatePouchSearch message, MessageContext ctx) {
			EntityPlayerMP playerMP = ctx.getServerHandler().player;

			if (playerMP.openContainer instanceof ContainerEggPouch) {
				ContainerEggPouch container = (ContainerEggPouch) playerMP.openContainer;
				container.pouchInventory.setSearchQuery(message.searchQuery);
				return new S2CUpdatePouchSearch(message.searchQuery);
			}

			return null;
		}

	}

}
