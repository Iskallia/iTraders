package iskallia.itraders.block.entity;

import javax.annotation.Nonnull;

import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.block.BlockDoublePartDirectional;
import iskallia.itraders.card.SubCardData;
import iskallia.itraders.card.SubCardGenerator;
import iskallia.itraders.card.SubCardRarity;
import iskallia.itraders.init.InitConfig;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemSpawnEggFighter;
import iskallia.itraders.item.ItemSubCard;
import iskallia.itraders.util.math.MathHelper;
import iskallia.itraders.util.math.Randomizer;
import iskallia.itraders.util.profile.SkinProfile;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

public class TileEntityCryoChamber extends TileInventoryBase {

    @Nonnull
    private SkinProfile skin = new SkinProfile();
    private String nickname;

    public CryoState state = CryoState.EMPTY;
    public int shrinkingRemainingTicks = 0;
    public int shrinkingFailTick = -1; // absolute tick to fail after

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
        if (getBlockState().getValue(BlockDoublePartDirectional.PART) == BlockDoublePartDirectional.EnumPartType.TOP) {
            TileEntity master = getWorld().getTileEntity(pos.down());
            if (master != null) return master.hasCapability(capability, facing);
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (getBlockState().getValue(BlockDoublePartDirectional.PART) == BlockDoublePartDirectional.EnumPartType.TOP) {
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
        if (state != CryoState.DONE)
            return ItemStack.EMPTY;

        return getInventoryHandler().extractItem(0, 1, false);
    }

    public double getFailRate(ItemStack eggStack) {
        if (eggStack.getItem() != InitItem.SPAWN_EGG_FIGHTER)
            return 0.0;

        int months = InitItem.SPAWN_EGG_FIGHTER.getMonths(eggStack);
        return MathHelper.map(months, 1, 36, 0.90, 0.00);
        // [1,36] -> %[10,100] success rate
        // == [1,36] -> %[90,0] fail rate
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
                    nickname = ItemSpawnEggFighter.getNickname(stack);
                    shrinkingRemainingTicks = InitConfig.CONFIG_CRYO_CHAMBER.SHRINKING_TICKS;
                    shrinkingFailTick = Randomizer.randomDouble() <= getFailRate(stack)
                            ? Randomizer.randomInt(
                            InitConfig.CONFIG_CRYO_CHAMBER.MIN_TICKS_BEFORE_FAIL,
                            InitConfig.CONFIG_CRYO_CHAMBER.SHRINKING_TICKS)
                            : -1;
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
                if (shrinkingRemainingTicks == 0) {
                    state = CryoState.DONE;
                    turnEggIntoCard();

                } else {
                    if (shrinkingFailTick != -1) {
                        int remainingFailTicks = InitConfig.CONFIG_CRYO_CHAMBER.SHRINKING_TICKS - shrinkingFailTick;
                        if (remainingFailTicks >= shrinkingRemainingTicks) {
                            state = CryoState.DONE;
                            turnEggIntoHead();
                        }
                    }
                    shrinkingRemainingTicks--;
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

    private void turnEggIntoCard() {
        ItemStack eggStack = getContent();
        String eggNickname = ItemSpawnEggFighter.getNickname(eggStack);

        ItemStack subCard = new ItemStack(InitItem.SUB_CARD);
        Tuple<SubCardRarity, SubCardData> cardData = SubCardGenerator.generateRandom();
        ItemSubCard.setSubUUID(subCard, eggNickname != null ? eggNickname : "");
        ItemSubCard.setCardRarity(subCard, cardData.getFirst());
        ItemSubCard.setCardData(subCard, cardData.getSecond());

        getInventoryHandler().setStackInSlot(0, subCard);
    }

    private void turnEggIntoHead() {
        ItemStack eggStack = getContent();

        ItemStack headStack = new ItemStack(Items.SKULL, 1, 3);
        NBTTagCompound stackNBT = new NBTTagCompound();
        stackNBT.setString("SkullOwner", eggStack.getDisplayName());
        headStack.setTagCompound(stackNBT);

        int particleCount = 300;

        world.playSound(null,
                pos.getX(), pos.getY(), pos.getZ(),
                SoundEvents.ENTITY_ITEM_BREAK,
                SoundCategory.MASTER, 1.0f, (float) Math.random());
        ((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false,
                pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d,
                particleCount, 0, 0, 0, 0.1d);

        getInventoryHandler().setStackInSlot(0, headStack);
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.nickname = compound.getString("Nickname");

        this.state = CryoState.values()[compound.getInteger("CryoState")];

        if (compound.hasKey("ShrinkingTicks", Constants.NBT.TAG_INT))
            this.shrinkingRemainingTicks = compound.getInteger("ShrinkingTicks");

        if (compound.hasKey("ShrinkingFailTime", Constants.NBT.TAG_INT))
            this.shrinkingFailTick = compound.getInteger("ShrinkingFailTime");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        compound.setString("Nickname", nickname != null ? nickname : "");

        compound.setInteger("CryoState", state.ordinal());

        if (state == CryoState.SHRINKING)
            compound.setInteger("ShrinkingTicks", shrinkingRemainingTicks);

        if (shrinkingFailTick != -1)
            compound.setInteger("ShrinkingFailTime", shrinkingFailTick);
    }

    public enum CryoState {EMPTY, SHRINKING, DONE}

}
