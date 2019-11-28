package iskallia.itraders.net.packet;

import io.netty.buffer.ByteBuf;
import iskallia.itraders.item.ItemAccelerationBottle;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SItemScroll implements IMessage {

	private int index;

	public C2SItemScroll() {

	}

	public C2SItemScroll(int index) {
		this.index = index;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.index = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.index);
	}

	public static class C2SItemScrollHandler implements IMessageHandler<C2SItemScroll, IMessage> {
		@Override
		public C2SItemScroll onMessage(C2SItemScroll message, MessageContext ctx) {
			EntityPlayerMP playerMP = ctx.getServerHandler().player;

			ItemStack stack = playerMP.getHeldItemMainhand();

			if (!(stack.getItem() instanceof ItemAccelerationBottle))
				return null;
			ItemAccelerationBottle bottle = (ItemAccelerationBottle) stack.getItem();

			bottle.setSelectedSubIndex(stack, message.index);
			
			String selectedSub = bottle.getSelectedSub(stack);
			if(selectedSub == null) return null;

			playerMP.connection.sendPacket(new SPacketTitle(SPacketTitle.Type.ACTIONBAR, 
					new TextComponentString(TextFormatting.DARK_AQUA + "Selected Sub" + 
											TextFormatting.WHITE + ": " + 
											TextFormatting.YELLOW + selectedSub)));

			return null;
		}
	}

}
