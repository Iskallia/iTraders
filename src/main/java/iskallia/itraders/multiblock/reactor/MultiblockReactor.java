package iskallia.itraders.multiblock.reactor;

import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import hellfirepvp.astralsorcery.common.tile.base.TileInventoryBase;
import iskallia.itraders.Traders;
import iskallia.itraders.block.entity.TileEntityPowerCube;
import iskallia.itraders.container.ContainerReactor;
import iskallia.itraders.init.InitBlock;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorCore;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorSlave;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MultiblockReactor extends PatternBlockArray {

    public static final MultiblockReactor INSTANCE = new MultiblockReactor();

    private MultiblockReactor() {
        super(new ResourceLocation(Traders.MOD_ID, "pattern_reactor"));
        load();
    }

    private void load() {
        int y, z; // <-- Hey do not judge my laziness! :p

        IBlockState reactorBlock = InitBlock.REACTOR_BLOCK.getDefaultState();
        IBlockState reactorGlass = InitBlock.REACTOR_GLASS.getDefaultState();
        IBlockState reactorHeatSink = InitBlock.REACTOR_HEAT_SINK.getDefaultState();
        IBlockState reactorCore = InitBlock.REACTOR_CORE.getDefaultState();
        IBlockState reactorCPU = InitBlock.REACTOR_CPU.getDefaultState();
        IBlockState[] blockPalette = {
                reactorBlock,
                reactorGlass,
                reactorHeatSink,
                reactorCore,
                reactorCPU
        };

        {
            y = 2;
            z = -2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[0]);
            addBlockFromPalette(0, y, z, blockPalette[0]);
            addBlockFromPalette(1, y, z, blockPalette[0]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = -1;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 0;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 1;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[0]);
            addBlockFromPalette(0, y, z, blockPalette[0]);
            addBlockFromPalette(1, y, z, blockPalette[0]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
        }

        {
            y = 1;
            z = -2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = -1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 0;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, blockPalette[2]);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
        }

        {
            y = 0;
            z = -2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = -1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 0;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, blockPalette[3]);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
        }

        {
            y = -1;
            z = -2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = -1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 0;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, blockPalette[4]);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 1;
            addBlockFromPalette(-2, y, z, blockPalette[1]);
            addBlockFromPalette(-1, y, z, null);
            addBlockFromPalette(0, y, z, null);
            addBlockFromPalette(1, y, z, null);
            addBlockFromPalette(2, y, z, blockPalette[1]);
            z = 2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
        }

        {
            y = -2;
            z = -2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[0]);
            addBlockFromPalette(0, y, z, blockPalette[0]);
            addBlockFromPalette(1, y, z, blockPalette[0]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = -1;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 0;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 1;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[1]);
            addBlockFromPalette(0, y, z, blockPalette[1]);
            addBlockFromPalette(1, y, z, blockPalette[1]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
            z = 2;
            addBlockFromPalette(-2, y, z, blockPalette[0]);
            addBlockFromPalette(-1, y, z, blockPalette[0]);
            addBlockFromPalette(0, y, z, blockPalette[0]);
            addBlockFromPalette(1, y, z, blockPalette[0]);
            addBlockFromPalette(2, y, z, blockPalette[0]);
        }
    }

    private void addBlockFromPalette(int x, int y, int z, IBlockState blockFromPalette) {
        if (blockFromPalette == null) return;
        super.addBlock(x, y, z, blockFromPalette);
    }

    public boolean structureReactor(World world, BlockPos masterPos) {
        for (Map.Entry<BlockPos, BlockInformation> entry : pattern.entrySet()) {
            BlockInformation info = entry.getValue();
            BlockPos pos = masterPos.add(entry.getKey());

            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof TileEntityReactorSlave) {
                ((TileEntityReactorSlave) tileEntity).connectToMaster(masterPos);

            } else if (!(tileEntity instanceof TileEntityReactorCore)) {
                return false;
            }
        }

        validateReactorInventory(world, masterPos);

        return true;
    }

    public void destructReactor(World world, BlockPos masterPos) {
        for (Map.Entry<BlockPos, BlockInformation> entry : pattern.entrySet()) {
            BlockInformation info = entry.getValue();
            BlockPos pos = masterPos.add(entry.getKey());

            TileEntityReactorSlave reactorSlave = TileEntityReactorSlave.getReactorSlave(world, pos);

            if (reactorSlave == null)
                continue;

            reactorSlave.disconnectFromMaster();
        }

        TileEntityReactorCore reactorCore = TileEntityReactorCore.getReactorCore(world, masterPos);

        if (reactorCore != null) {
            reactorCore.onReactorDestructed();
        }
    }

    public void validateReactorInventory(IBlockAccess world, BlockPos masterPos) {
        TileEntityReactorCore reactorCore = TileEntityReactorCore.getReactorCore(world, masterPos);

        // Master pos does not point to a valid Reactor Core
        if (reactorCore == null) return;

        // Fetch cubes in the world and the Reactor Inventory
        Map<BlockPos, TileEntityPowerCube> powerCubes = getPowerCubes(world, masterPos);
        TileInventoryBase.ItemHandlerTile inventoryHandler = reactorCore.getInventoryHandler();

        // Clear current inventory
        inventoryHandler.clearInventory();

        // Serialize and put cubes on right inventory indices
        for (Map.Entry<BlockPos, TileEntityPowerCube> cubeEntry : powerCubes.entrySet()) {
            BlockPos cubePos = cubeEntry.getKey();
            TileEntityPowerCube cube = cubeEntry.getValue();

            NBTTagCompound cubeStackNBT = new NBTTagCompound();
            cube.writeCustomNBT(cubeStackNBT);
            ItemStack cubeStack = new ItemStack(InitBlock.ITEM_POWER_CUBE);
            cubeStack.setTagCompound(cubeStackNBT);

            Integer index = ContainerReactor.OFFSET_TO_SLOT_INDEX_MAP.get(cubePos);
            inventoryHandler.insertItem(index, cubeStack, false);
        }
    }

    public Map<BlockPos, TileEntityPowerCube> getPowerCubes(IBlockAccess world, BlockPos masterPos) {
        Map<BlockPos, TileEntityPowerCube> offsetToCubes = new HashMap<>();

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    BlockPos pos = masterPos.add(xOffset, yOffset, zOffset);
                    TileEntity tileEntity = world.getTileEntity(pos);

                    if (tileEntity instanceof TileEntityPowerCube)
                        offsetToCubes.put(
                                new BlockPos(xOffset, yOffset, zOffset),
                                (TileEntityPowerCube) tileEntity
                        );
                }
            }
        }

        return offsetToCubes;
    }

}
