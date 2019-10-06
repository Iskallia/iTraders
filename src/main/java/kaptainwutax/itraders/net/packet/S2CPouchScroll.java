package kaptainwutax.itraders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.itraders.gui.container.GuiContainerEggPouch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class S2CPouchScroll implements IMessage {

	private int currentScroll;
	private int totalScroll;
	
	public S2CPouchScroll() {
		
	}
	
	public S2CPouchScroll(int currentScroll, int totalScroll) {
		this.currentScroll = currentScroll;
		this.totalScroll = totalScroll;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.currentScroll = buf.readInt();
		this.totalScroll = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.currentScroll);
		buf.writeInt(this.totalScroll);		
	}
	
	public static class S2CPouchScrollHandler implements IMessageHandler<S2CPouchScroll, IMessage> {
		@Override
		public IMessage onMessage(S2CPouchScroll message, MessageContext ctx) {
			Minecraft minecraft = Minecraft.getMinecraft();
			EntityPlayerSP player = minecraft.player;
			World world = player.world;

			if(minecraft.currentScreen instanceof GuiContainerEggPouch) {
				GuiContainerEggPouch gui = (GuiContainerEggPouch)minecraft.currentScreen;
				gui.setScroll(message.currentScroll, message.totalScroll);
			}
			
			return null;
		}
	}
	
}
