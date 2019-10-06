package kaptainwutax.itraders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.itraders.container.ContainerEggPouch;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SMovePouchRow implements IMessage {

	private int amount;
	
	public C2SMovePouchRow() {
		
	}
	
	public C2SMovePouchRow(int amount) {
		this.amount = amount;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.amount = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.amount);
	}
	
	public static class C2SMovePouchRowHandler implements IMessageHandler<C2SMovePouchRow, S2CPouchScroll> {
		@Override
		public S2CPouchScroll onMessage(C2SMovePouchRow message, MessageContext ctx) {
			EntityPlayerMP playerMP = ctx.getServerHandler().player;
			
			if(playerMP.openContainer instanceof ContainerEggPouch) {
				ContainerEggPouch container = (ContainerEggPouch)playerMP.openContainer;
				container.onMove(message.amount);
				return new S2CPouchScroll(container.currentScroll, container.totalScroll);
			}
			
			return null;
		}
	}

}
