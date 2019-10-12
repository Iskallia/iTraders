package kaptainwutax.itraders.event;

import kaptainwutax.itraders.Traders;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Traders.MOD_ID)
public class EventPlayer {

	public static void onStartTracking(PlayerEvent.StartTracking event) {
		Entity target = event.getTarget();
	}
	
}
