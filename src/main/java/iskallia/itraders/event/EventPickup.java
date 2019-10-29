package iskallia.itraders.event;

import iskallia.itraders.Traders;
import iskallia.itraders.item.ItemEggPouch;
import iskallia.itraders.item.ItemSpawnEggFighter;
import iskallia.itraders.world.data.DataEggPouch;
import iskallia.itraders.world.storage.PouchInventory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Traders.MOD_ID)
public class EventPickup {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onFighterEggPickup(EntityItemPickupEvent event) {
		if (event.getEntityPlayer().world.isRemote)
			return;

		EntityItem itemEntity = event.getItem();

		if (itemEntity == null || itemEntity.isDead)
			return; // Item entity is gone/dead before the event

		// Fetch player and the item stack they're picking up
		ItemStack itemStack = itemEntity.getItem();
		EntityPlayer player = event.getEntityPlayer();

		if (!(itemStack.getItem() instanceof ItemSpawnEggFighter))
			return; // Picked up anything other than a Fighter Spawn Egg

		if (!hasEggPouch(player))
			return; // Player does not have an Egg Pouch

		// Fetch pouch and put a copy of the egg on first empty slot
		PouchInventory pouch = DataEggPouch.get(player.world).getOrCreatePouch(player);
		pouch.insertItem(PouchInventory.FAKE_SLOT, itemStack.copy(), false);

		// Remove item from the item entity
		itemStack.setCount(0);
		itemEntity.setItem(itemStack);
		event.setResult(Event.Result.ALLOW);
	}

	private static boolean hasEggPouch(EntityPlayer player) {
		for (ItemStack itemStack : player.inventory.mainInventory) {
			if (itemStack.getItem() instanceof ItemEggPouch)
				return true;
		}
		for (ItemStack itemStack : player.inventory.offHandInventory) {
			if (itemStack.getItem() instanceof ItemEggPouch)
				return true;
		}
		return false;
	}

}
