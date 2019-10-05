package kaptainwutax.itraders;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class PouchInventory extends ItemStackHandler {

	public PouchInventory() {
		super(6 * 9);
	}
	
	public PouchInventory(NBTTagCompound nbt) {
		this.deserializeNBT(nbt);
	}

	public void expand(int slots) {
		for(int i = 0; i < slots; i++) {
			this.stacks.add(ItemStack.EMPTY);
		}
	}
	
}
