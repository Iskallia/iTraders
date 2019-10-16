package kaptainwutax.itraders.container;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotMerchantResult;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.MathHelper;
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

        //If the current slot already has items in it, put them back in the player's inventory.
        if(!currentStack.isEmpty()) {
        	//If there's no space in the player's inventory, stop and do nothing.
        	if(!playerInventory.addItemStackToInventory(currentStack))return;
        }
        
        if(shift) {
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
        
        /*
    	//Remove stack.
        ItemStack stack = this.inventorySlots.get(slotId).getStack();
        boolean same = true;
        
        if(stack.getItem() != buy.getItem() || stack.getMetadata() != buy.getMetadata() || 
        		(!stack.hasTagCompound() && !buy.hasTagCompound()) || 
        		(stack.hasTagCompound() && buy.hasTagCompound() && stack.getTagCompound().equals(buy.getTagCompound()))) {
        	same = false;
        	if(!stack.isEmpty() && !this.player.inventory.addItemStackToInventory(stack))return;
        }
        
        //Add stack.
        if(shift)buy.setCount(buy.getMaxStackSize());                     
        if(same)buy.setCount(buy.getCount() + stack.getCount());
        
        buy.setCount(MathHelper.clamp(buy.getCount(), 0, buy.getMaxStackSize()));
        
        int count = 0;
        if(same)count += stack.getCount();
        
        for(int i = 0; i < playerInv.getSizeInventory() && count < buy.getCount(); i++) {
        	ItemStack slotStack = playerInv.getStackInSlot(i);        	
        	if(slotStack.getItem() != buy.getItem() || slotStack.getMetadata() != buy.getMetadata())continue;
        	count += playerInv.decrStackSize(i, buy.getCount() - count).getCount();
        }
        
        buy.setCount(count);
        
        this.inventorySlots.get(slotId).putStack(buy);
        */
    }
    
    @Override
	public void setCurrentRecipeIndex(int currentRecipeIndex) {
    	int index = currentRecipeIndex & ((1 << 31) - 1);    
    	super.setCurrentRecipeIndex(index);   	

        MerchantRecipe recipe = this.merchant.getRecipes(null).get(index);  

        this.cramStack(0, recipe.getItemToBuy().copy(), currentRecipeIndex >>> 31 == 1);
        if(recipe.hasSecondItemToBuy())this.cramStack(1, recipe.getSecondItemToBuy().copy(), currentRecipeIndex >>> 31 == 1);
    }
    
    public class SlotVillagerResult extends Slot {

        private final InventoryMerchant merchantInventory;
        private final EntityPlayer player;
        private int removeCount;
        private final IMerchant merchant;

        public SlotVillagerResult(EntityPlayer player, IMerchant merchant, InventoryMerchant merchantInventory, int slotIndex, int xPosition, int yPosition) {
            super(merchantInventory, slotIndex, xPosition, yPosition);
            this.player = player;
            this.merchant = merchant;
            this.merchantInventory = merchantInventory;
        }

        @Override
		public ItemStack decrStackSize(int amount) {
            if(this.getHasStack()) {
                this.removeCount += Math.min(amount, this.getStack().getCount());
            }

            return super.decrStackSize(amount);
        }

        private boolean doTrade(MerchantRecipe trade, ItemStack firstItem, ItemStack secondItem) {
            ItemStack itemstack = trade.getItemToBuy();
            ItemStack itemstack1 = trade.getSecondItemToBuy();

            if (firstItem.getItem() == itemstack.getItem() && firstItem.getCount() >= itemstack.getCount())
            {
                if (!itemstack1.isEmpty() && !secondItem.isEmpty() && itemstack1.getItem() == secondItem.getItem() && secondItem.getCount() >= itemstack1.getCount())
                {
                    firstItem.shrink(itemstack.getCount());
                    secondItem.shrink(itemstack1.getCount());
                    return true;
                }

                if (itemstack1.isEmpty() && secondItem.isEmpty())
                {
                    firstItem.shrink(itemstack.getCount());
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }

        @Override
		protected void onCrafting(ItemStack stack) {
            stack.onCrafting(this.player.world, this.player, this.removeCount);
            this.removeCount = 0;
        }

        @Override
		protected void onCrafting(ItemStack stack, int amount) {
            this.removeCount += amount;
            this.onCrafting(stack);
        }

        @Override
		public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
            this.onCrafting(stack);
            MerchantRecipe merchantrecipe = this.merchantInventory.getCurrentRecipe();

            if(merchantrecipe != null) {
                ItemStack itemstack = this.merchantInventory.getStackInSlot(0);
                ItemStack itemstack1 = this.merchantInventory.getStackInSlot(1);

                if(this.doTrade(merchantrecipe, itemstack, itemstack1) || this.doTrade(merchantrecipe, itemstack1, itemstack)) {
                    this.merchant.useRecipe(merchantrecipe);
                    thePlayer.addStat(StatList.TRADED_WITH_VILLAGER);
                    this.merchantInventory.setInventorySlotContents(0, itemstack);
                    this.merchantInventory.setInventorySlotContents(1, itemstack1);
                }
            }

            return stack;
        }
        
    }
  
}