package iskallia.itraders.init;

import iskallia.itraders.Traders;
import iskallia.itraders.net.packet.C2SItemScroll;
import iskallia.itraders.net.packet.C2SItemScroll.C2SItemScrollHandler;
import iskallia.itraders.net.packet.C2SMovePouchRow;
import iskallia.itraders.net.packet.C2SUpdatePouchSearch;
import iskallia.itraders.net.packet.S2CFighterHeight;
import iskallia.itraders.net.packet.S2CPouchScroll;
import iskallia.itraders.net.packet.S2CUpdatePouchSearch;
import iskallia.itraders.net.packet.C2SMovePouchRow.C2SMovePouchRowHandler;
import iskallia.itraders.net.packet.C2SUpdatePouchSearch.C2SUpdatePouchSearchHandler;
import iskallia.itraders.net.packet.S2CFighterHeight.S2CFighterHeightHandler;
import iskallia.itraders.net.packet.S2CPouchScroll.S2CPouchScrollHandler;
import iskallia.itraders.net.packet.S2CUpdatePouchSearch.S2CUpdatePouchSearchHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class InitPacket {

	public static SimpleNetworkWrapper PIPELINE = null;
	private static int packetId = 0;

	private static int nextId() {
		return packetId++;
	}

	public static void registerPackets() {
		PIPELINE = NetworkRegistry.INSTANCE.newSimpleChannel(Traders.MOD_ID);
		PIPELINE.registerMessage(S2CFighterHeightHandler.class, S2CFighterHeight.class, nextId(), Side.CLIENT);
		PIPELINE.registerMessage(C2SMovePouchRowHandler.class, C2SMovePouchRow.class, nextId(), Side.SERVER);
		PIPELINE.registerMessage(S2CPouchScrollHandler.class, S2CPouchScroll.class, nextId(), Side.CLIENT);
		PIPELINE.registerMessage(C2SUpdatePouchSearchHandler.class, C2SUpdatePouchSearch.class, nextId(), Side.SERVER);
		PIPELINE.registerMessage(S2CUpdatePouchSearchHandler.class, S2CUpdatePouchSearch.class, nextId(), Side.CLIENT);
		PIPELINE.registerMessage(C2SItemScrollHandler.class, C2SItemScroll.class, nextId(), Side.SERVER);
	}

}
