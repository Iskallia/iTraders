package kaptainwutax.itraders.event;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.entity.EntityFighter;
import kaptainwutax.itraders.init.InitPacket;
import kaptainwutax.itraders.net.packet.S2CFighterHeight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Traders.MOD_ID)
public class EventPlayer {

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target == null || !(target instanceof EntityFighter))
            return;

        EntityFighter fighter = (EntityFighter) target;
        EntityPlayer player = event.getEntityPlayer();

        InitPacket.PIPELINE.sendTo(new S2CFighterHeight(fighter), (EntityPlayerMP) player);
    }

}
