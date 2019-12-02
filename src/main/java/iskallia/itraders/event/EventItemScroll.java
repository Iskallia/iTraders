package iskallia.itraders.event;

import iskallia.itraders.Traders;
import iskallia.itraders.init.InitPacket;
import iskallia.itraders.item.ItemAccelerationBottle;
import iskallia.itraders.net.packet.C2SItemScroll;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Traders.MOD_ID, value = Side.CLIENT)
public class EventItemScroll {

	private static Minecraft mc = FMLClientHandler.instance().getClient();
	private static int wheelStatus = 0;

	@SubscribeEvent
	public static void onMouseEvent(MouseEvent event) {
		if (mc.player != null && mc.player.isSneaking()) {
			ItemStack stack = mc.player.getHeldItemMainhand();
			int delta = event.getDwheel();

			if (stack.getItem() instanceof ItemAccelerationBottle && delta != 0) {
				ItemAccelerationBottle bottle = (ItemAccelerationBottle) stack.getItem();

				int subCount = bottle.getSubCount(stack);
				
				if(subCount == 0) return;

				wheelStatus += event.getDwheel();
				int scaledDelta = wheelStatus / 120;
				wheelStatus = wheelStatus % 120;
				int newIndex = bottle.getSelectedSubIndex(stack) + (scaledDelta % subCount);

				if (newIndex > 0) {
					newIndex = newIndex % subCount;
				} else if (newIndex < 0) {
					newIndex = subCount + newIndex;
				}

				bottle.setSelectedSubIndex(stack, newIndex);

				
				InitPacket.PIPELINE.sendToServer(new C2SItemScroll(newIndex));

				event.setCanceled(true);
			}
		}
	}

}
