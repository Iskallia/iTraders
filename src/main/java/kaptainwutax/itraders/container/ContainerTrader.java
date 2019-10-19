package kaptainwutax.itraders.container;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotMerchantResult;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class ContainerTrader extends ContainerMerchant {

    private IMerchant merchant;
	private World world;

    private EntityPlayer player;

    public ContainerTrader(InventoryPlayer playerInventory, IMerchant merchant, World worldIn) {
    	super(playerInventory, merchant, worldIn);
        this.merchant = merchant;
        this.world = worldIn;   
        this.player = playerInventory.player;
        
        InventoryMerchant inv = this.getMerchantInventory();
                
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();      
        
        this.addSlotToContainer(new Slot(inv, 0, 136, 37));
        this.addSlotToContainer(new Slot(inv, 1, 162, 37));
        this.addSlotToContainer(new SlotMerchantResult(player, merchant, inv, 2, 220, 37));

        int int_4;
        for(int_4 = 0; int_4 < 3; ++int_4) {
           for(int int_3 = 0; int_3 < 9; ++int_3) {
              this.addSlotToContainer(new Slot(playerInventory, int_3 + int_4 * 9 + 9, 108 + int_3 * 18, 84 + int_4 * 18));
           }
        }

        for(int_4 = 0; int_4 < 9; ++int_4) {
           this.addSlotToContainer(new Slot(playerInventory, int_4, 108 + int_4 * 18, 142));
        }
    }
    
    private void cramStack(int slotId, ItemStack wantedStack, boolean shift) {
        InventoryPlayer playerInventory = this.player.inventory;
        ItemStack currentStack = this.inventorySlots.get(slotId).getStack();
        int currentCount = ItemHandlerHelper.canItemStacksStackRelaxed(currentStack, wantedStack) 
        		? currentStack.getCount() : 0;
        
        //If the current slot already has items in it, put them back in the player's inventory.
        if(!currentStack.isEmpty()) {
        	//If there's no space in the player's inventory, stop and do nothing.
        	if(!playerInventory.addItemStackToInventory(currentStack))return;
        }
        
        wantedStack.setCount(wantedStack.getCount() + currentCount);
        
        if(shift || wantedStack.getCount() > wantedStack.getMaxStackSize()) {
        	wantedStack.setCount(wantedStack.getMaxStackSize());
        }
        
        ItemStack decreasingStack = wantedStack.copy();
        
        for(int i = 0; i < playerInventory.getSizeInventory() && !decreasingStack.isEmpty(); i++) {
        	ItemStack stackInSlot = playerInventory.getStackInSlot(i);
        	if(!ItemHandlerHelper.canItemStacksStackRelaxed(stackInSlot, decreasingStack))continue;
        	int decreasedCount = playerInventory.decrStackSize(i, decreasingStack.getCount()).getCount();
        	decreasingStack.setCount(decreasingStack.getCount() - decreasedCount);
        }                 
        
        if(!decreasingStack.isEmpty()) {
        	wantedStack.setCount(wantedStack.getCount() - decreasingStack.getCount());
        }
        
        this.inventorySlots.get(slotId).putStack(wantedStack);
    }
    
    @Override
	public void setCurrentRecipeIndex(int currentRecipeIndex) {
    	int index = currentRecipeIndex & ((1 << 31) - 1);    
    	super.setCurrentRecipeIndex(index);   	

        MerchantRecipe recipe = this.merchant.getRecipes(null).get(index);  

        this.cramStack(0, recipe.getItemToBuy().copy(), currentRecipeIndex >>> 31 == 1);
        
        if(recipe.hasSecondItemToBuy()) {
        	this.cramStack(1, recipe.getSecondItemToBuy().copy(), currentRecipeIndex >>> 31 == 1);
        }
    }
  
}
