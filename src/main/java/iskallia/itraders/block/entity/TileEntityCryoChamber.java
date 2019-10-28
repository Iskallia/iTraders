package iskallia.itraders.block.entity;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.block.BlockCryoChamber;
import iskallia.itraders.card.SubCardData;
import iskallia.itraders.card.SubCardGenerator;
import iskallia.itraders.card.SubCardRarity;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemSpawnEggFighter;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class TileEntityCryoChamber extends TileInventoryBase {

    public static final int MAX_SHRINKING_TICKS = 20 * 10;

    @Nonnull
    private SkinProfile skin = new SkinProfile();
    private String nickname;

    public CryoState state = CryoState.EMPTY;
    public int shrinkingTicks = 0;

    public TileEntityCryoChamber() {
        super(1);
    }

    public ItemStack getContent() {
        return getInventoryHandler().getStackInSlot(0);
    }

    public boolean isOccupied() {
        return getContent() != ItemStack.EMPTY;
    }

    public String getNickname() {
        return nickname;
    }

    @Nonnull
    public SkinProfile getSkin() {
        return skin;
    }

    public CryoState getState() {
        return state;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (getBlockState().getValue(BlockCryoChamber.PART) == BlockCryoChamber.EnumPartType.TOP) {
            TileEntity master = getWorld().getTileEntity(pos.down());
            if (master != null) return master.hasCapability(capability, facing);
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (getBlockState().getValue(BlockCryoChamber.PART) == BlockCryoChamber.EnumPartType.TOP) {
            TileEntity master = getWorld().getTileEntity(pos.down());
            if (master != null) return master.getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

    public boolean insertEgg(ItemStack eggStack) {
        if (eggStack.getItem() != InitItem.SPAWN_EGG_FIGHTER)
            return false;

        if (isOccupied())
            return false;

        // Insert item
        getInventoryHandler().insertItem(0, eggStack, false);
        return true;
    }

    public ItemStack extractContent() {
        if (state != CryoState.CARD)
            return ItemStack.EMPTY;

        return getInventoryHandler().extractItem(0, 1, false);
    }

    @Override
    protected ItemHandlerTile createNewItemHandler() {
        return new ItemHandlerTileFiltered(this) {
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                ItemStack remainingStack = super.insertItem(slot, stack, simulate);

                if (remainingStack == ItemStack.EMPTY) {
                    // Start shrinking state
                    state = CryoState.SHRINKING;
                    shrinkingTicks = MAX_SHRINKING_TICKS;
                    nickname = ItemSpawnEggFighter.getNickname(stack);
                }

                return remainingStack;
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                ItemStack extractedStack = super.extractItem(slot, amount, simulate);

                if (extractedStack != ItemStack.EMPTY) {
                    // Go back to empty state
                    state = CryoState.EMPTY;
                }

                return extractedStack;
            }

            @Override
            public boolean canInsertItem(int slot, ItemStack toAdd, @Nonnull ItemStack existing) {
                return existing.isEmpty() && (toAdd.getItem() == InitItem.SPAWN_EGG_FIGHTER);
            }

            @Override
            public boolean canExtractItem(int slot, int amount, @Nonnull ItemStack existing) {
                return !existing.isEmpty() && (state != CryoState.SHRINKING);
            }
        };
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (state == CryoState.SHRINKING) {
                if (shrinkingTicks == 0) {
                    state = CryoState.CARD;
                    turnEggIntoCard();

                } else {
                    shrinkingTicks--;
                }
                markForUpdate();
            }
        }

        if (world.isRemote) {
            if (nickname != null) {
                String previousNickname = skin.getLatestNickname();
                if (previousNickname == null || !previousNickname.equals(nickname)) {
                    skin.updateSkin(nickname);
                }
            }
        }
    }

    private void turnEggIntoCard() { // TODO: Generate sub card and put on Slot#0
        ItemStack eggStack = getContent();

        ItemStack pseudoSubCard = new ItemStack(InitItem.MAGIC_ORE_DUST); // TODO

        getInventoryHandler().setStackInSlot(0, pseudoSubCard);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        if (compound.hasKey("Nickname", Constants.NBT.TAG_STRING))
            this.nickname = compound.getString("Nickname");

        this.state = CryoState.values()[compound.getInteger("CryoState")];

        if (compound.hasKey("ShrinkingTicks", Constants.NBT.TAG_INT))
            this.shrinkingTicks = compound.getInteger("ShrinkingTicks");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        compound.setString("Nickname", nickname != null ? nickname : "");

        compound.setInteger("CryoState", state.ordinal());

        if (state == CryoState.SHRINKING)
            compound.setInteger("ShrinkingTicks", shrinkingTicks);
    }

    public enum CryoState {EMPTY, SHRINKING, CARD}

}
