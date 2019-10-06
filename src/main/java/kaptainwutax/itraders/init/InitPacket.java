package kaptainwutax.itraders.init;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.net.packet.C2SMovePouchRow;
import kaptainwutax.itraders.net.packet.C2SMovePouchRow.C2SMovePouchRowHandler;
import kaptainwutax.itraders.net.packet.S2CFighterHeight;
import kaptainwutax.itraders.net.packet.S2CFighterHeight.S2CFighterHeightHandler;
import kaptainwutax.itraders.net.packet.S2CPouchScroll;
import kaptainwutax.itraders.net.packet.S2CPouchScroll.S2CPouchScrollHandler;
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
    }
    
}
