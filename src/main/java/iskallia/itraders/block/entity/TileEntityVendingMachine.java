package iskallia.itraders.block.entity;

import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityVendingMachine extends TileEntitySynchronized implements IMerchant, ITickable {

    private EntityPlayer buyingPlayer;
    private MerchantRecipeList buyingList;

    @Nonnull
    private SkinProfile skin = new SkinProfile();
    private String nickname;

    @Nonnull
    public SkinProfile getSkin() {
        return skin;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void update() {
        if (nickname != null) {
            String previousNickname = skin.getLatestNickname();
            if (previousNickname == null || !previousNickname.equals(nickname)) {
                if (world.isRemote)
                    skin.updateSkin(nickname);
                else
                    markForUpdate();
            }
        }
    }

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
        return new TextComponentString(getNickname()); // TODO
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

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.nickname = compound.getString("Nickname");

        if (compound.hasKey("Offers", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound offersNBT = compound.getCompoundTag("Offers");
            this.buyingList = new MerchantRecipeList(offersNBT);
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        compound.setString("Nickname", nickname != null ? nickname : "");

        if (this.buyingList != null) {
            compound.setTag("Offers", this.buyingList.getRecipiesAsTags());
        }
    }

}
