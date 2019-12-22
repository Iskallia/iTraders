package iskallia.itraders.container.slot;

import iskallia.itraders.block.BlockPowerChamber;
import iskallia.itraders.block.BlockPowerCube;
import iskallia.itraders.container.ContainerReactor;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.init.InitItem;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ReactorSlot extends SlotItemHandler {

    @Nonnull
    private World world;

    @Nonnull
    private BlockPos reactorCorePos;

    public ReactorSlot(@Nonnull World world, TileEntityReactorCore reactorCore, int index, int xPosition, int yPosition) {
        super(reactorCore.getInventoryHandler(), index, xPosition, yPosition);
        this.world = world;
        this.reactorCorePos = reactorCore.getPos();
    }

    public BlockPos getWorldPos() {
        return ContainerReactor.OFFSET_TO_SLOT_INDEX_MAP
                .inverse().get(this.slotNumber).add(reactorCorePos);
    }

    @Override
    public ItemStack onTake(EntityPlayer player, @Nonnull ItemStack stack) {
        if (TileEntityReactorCore.getReactorCore(world, reactorCorePos) != null) {
            System.out.println("Removing " + getWorldPos());
            world.setBlockToAir(getWorldPos());
        }
        return super.onTake(player, stack);
    }

    @Override
    public void putStack(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && TileEntityReactorCore.getReactorCore(world, reactorCorePos) != null) {
            System.out.println("Setting world state @" + getWorldPos());
            BlockPowerCube.placePowerCube(stack, world, getWorldPos());
        }
        super.putStack(stack);
    }

}
