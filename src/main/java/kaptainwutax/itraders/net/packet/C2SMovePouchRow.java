package kaptainwutax.itraders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.itraders.container.ContainerEggPouch;
import kaptainwutax.itraders.entity.EntityFighter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
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
	
	public static class C2SMovePouchRowHandler implements IMessageHandler<C2SMovePouchRow, IMessage> {
		@Override
		public IMessage onMessage(C2SMovePouchRow message, MessageContext ctx) {
			EntityPlayerMP playerMP = ctx.getServerHandler().player;
			Container container = playerMP.openContainer;
			
			if(container instanceof ContainerEggPouch) {
				((ContainerEggPouch)container).onMove(message.amount);
			}
			
			return null;
		}
	}

}
