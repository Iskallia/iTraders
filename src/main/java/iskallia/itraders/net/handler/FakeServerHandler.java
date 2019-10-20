package iskallia.itraders.net.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;

public class FakeServerHandler extends NetHandlerPlayServer {

	public FakeServerHandler(EntityPlayerMP player) {
		super(player.getServer(), new NetworkManager(EnumPacketDirection.SERVERBOUND), player);
	}

	@Override
	public void update() {
	}
	
}
