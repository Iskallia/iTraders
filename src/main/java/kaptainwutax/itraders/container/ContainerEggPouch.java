package kaptainwutax.itraders.container;

import kaptainwutax.itraders.PouchInventory;
import kaptainwutax.itraders.world.data.DataEggPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEggPouch extends Container {

	public ContainerEggPouch(World world, EntityPlayer player) {	
		DataEggPouch data = DataEggPouch.get(world);
		PouchInventory pouchInventory = data.getOrCreatePouch(player);
		
		for(int row = 0; row < 6; row++) {
			for(int column = 0; column < 9; column++) {
				int slotId = row * 9 + column;				
				this.addSlotToContainer(new SlotItemHandler(pouchInventory, slotId, 8 + column * 18, 18 + row * 18));
			}
		}
		
		for(int row = 0; row < 3; row++) {
			for(int column = 0; column < 9; column++) {
				int slotId = row * 9 + column + 9;				
				this.addSlotToContainer(new Slot(player.inventory, slotId, 8 + column * 18, 140 + row * 18));
			}
		}
		
		for(int hotbar = 0; hotbar < 9; hotbar++) {
			this.addSlotToContainer(new Slot(player.inventory, hotbar, 8 + hotbar * 18, 198));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}
