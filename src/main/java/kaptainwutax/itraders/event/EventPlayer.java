package kaptainwutax.itraders.event;

import baubles.api.BaublesApi;
import baubles.common.Baubles;
import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.entity.EntityFighter;
import kaptainwutax.itraders.entity.EntityMiniGhost;
import kaptainwutax.itraders.init.InitPacket;
import kaptainwutax.itraders.item.ItemSkullNeck;
import kaptainwutax.itraders.net.packet.S2CFighterHeight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
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

    @SubscribeEvent
    public static void onLogin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        Item necklaceBauble = BaublesApi.getBaublesHandler(event.player).getStackInSlot(0).getItem();

        if (necklaceBauble instanceof ItemSkullNeck) {
            EntityMiniGhost ghost = ItemSkullNeck.createMiniGhostFor(event.player);
        }
    }

    @SubscribeEvent
    public static void onLogout(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        Item necklaceBauble = BaublesApi.getBaublesHandler(event.player).getStackInSlot(0).getItem();

        if (necklaceBauble instanceof ItemSkullNeck) {
            ItemSkullNeck.removeMiniGhostOf(event.player);
        }
    }

    @SubscribeEvent
    public static void onDimensionChange(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent event) {
        Item necklaceBauble = BaublesApi.getBaublesHandler(event.player).getStackInSlot(0).getItem();

        System.out.println("FROM " + event.fromDim + " TO " + event.toDim);

        if (necklaceBauble instanceof ItemSkullNeck) {
            ItemSkullNeck.removeMiniGhostOf(event.player);
            ItemSkullNeck.createMiniGhostFor(event.player);
        }

    }

}
