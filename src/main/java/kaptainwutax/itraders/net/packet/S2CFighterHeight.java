package kaptainwutax.itraders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.itraders.entity.EntityFighter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class S2CFighterHeight implements IMessage {

	private int entityId;
	private float sizeMultiplier;
	
	public S2CFighterHeight() {

	}
	
	public S2CFighterHeight(EntityFighter fighter) {
		this.entityId = fighter.getEntityId();
		this.sizeMultiplier = fighter.sizeMultiplier;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityId = buf.readInt();
		this.sizeMultiplier = buf.readFloat();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeFloat(this.sizeMultiplier);
	}
	
	public static class S2CFighterHeightHandler implements IMessageHandler<S2CFighterHeight, IMessage> {
		@Override
		public IMessage onMessage(S2CFighterHeight message, MessageContext ctx) {
			Minecraft minecraft = Minecraft.getMinecraft();
			EntityPlayerSP player = minecraft.player;
			World world = player.world;
			
			Entity entity = world.getEntityByID(message.entityId);
			if(entity == null || entity.isDead || !(entity instanceof EntityFighter))return null;
			
			EntityFighter fighter = (EntityFighter)entity;
			fighter.changeSize(message.sizeMultiplier);
			return null;
		}
	}

}
