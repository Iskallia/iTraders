package kaptainwutax.itraders.event;

import baubles.api.BaublesApi;
import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.item.ItemSkullNeck;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = Traders.MOD_ID)
public class EventGhosts {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ItemStack necklaceBauble = BaublesApi.getBaublesHandler(event.player).getStackInSlot(0);

        if (necklaceBauble.getItem() != InitItem.SKULL_NECKLACE)
            return;

        ItemSkullNeck.createMiniGhostFor(event.player, necklaceBauble);
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ItemStack necklaceBauble = BaublesApi.getBaublesHandler(event.player).getStackInSlot(0);

        if (necklaceBauble.getItem() != InitItem.SKULL_NECKLACE)
            return;

        ItemSkullNeck.removeMiniGhostOf(event.player);
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        ItemStack necklaceBauble = BaublesApi.getBaublesHandler(event.player).getStackInSlot(0);

        if (necklaceBauble.getItem() != InitItem.SKULL_NECKLACE)
            return;

        ItemSkullNeck.removeMiniGhostOf(event.player);
        ItemSkullNeck.createMiniGhostFor(event.player, necklaceBauble);
    }

}
