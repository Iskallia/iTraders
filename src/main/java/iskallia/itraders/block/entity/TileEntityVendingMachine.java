package iskallia.itraders.block.entity;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityVendingMachine extends TileEntity implements IMerchant {

    private EntityPlayer buyingPlayer;
    private MerchantRecipeList buyingList;

    @Override
    public void setCustomer(@Nullable EntityPlayer player) {
        this.buyingPlayer = player;
    }

    @Nullable
    @Override
    public EntityPlayer getCustomer() {
        return this.buyingPlayer;
    }

    @Nullable
    @Override
    public MerchantRecipeList getRecipes(@Nonnull EntityPlayer player) {
        if (this.buyingList == null) {
            this.buyingList = new MerchantRecipeList();
        }

        return net.minecraftforge.event.ForgeEventFactory.listTradeOffers(this, player, buyingList);
    }

    @Override
    public void setRecipes(@Nullable MerchantRecipeList recipeList) {
        this.buyingList = recipeList;
    }

    @Override
    public void useRecipe(@Nonnull MerchantRecipe recipe) {
        System.out.println("useRecipe(" + recipe + ");");
//        recipe.incrementToolUses();
//        this.livingSoundTime = -this.getTalkInterval();
//        this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
//        int i = 3 + this.rand.nextInt(4);
//
//        if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0) {
//            this.timeUntilReset = 40;
//            this.needsInitilization = true;
//            this.isWillingToMate = true;
//
//            if (this.buyingPlayer != null) {
//                this.lastBuyingPlayer = this.buyingPlayer.getUniqueID();
//            } else {
//                this.lastBuyingPlayer = null;
//            }
//
//            i += 5;
//        }
//
//        if (recipe.getItemToBuy().getItem() == Items.EMERALD) {
//            this.wealth += recipe.getItemToBuy().getCount();
//        }
//
//        if (recipe.getRewardsExp()) {
//            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
//        }
//
//        if (this.buyingPlayer instanceof EntityPlayerMP) {
//            CriteriaTriggers.VILLAGER_TRADE.trigger((EntityPlayerMP) this.buyingPlayer, this, recipe.getItemToSell());
//        }
    }

    @Override
    public void verifySellingItem(@Nonnull ItemStack stack) {
        System.out.println("verifySellingItem(" + stack + ")");
//        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
//            this.livingSoundTime = -this.getTalkInterval();
//            this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
//        }
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString("TODO"); // TODO
    }

    @Nonnull
    @Override
    public World getWorld() {
        return this.world;
    }

    @Nonnull
    @Override
    public BlockPos getPos() {
        return this.pos;
    }

}
