package iskallia.itraders.gui;

import iskallia.itraders.block.entity.TileEntityCubeChamber;
import iskallia.itraders.block.entity.TileEntityGhostPedestal;
import iskallia.itraders.container.ContainerCubeChamber;
import iskallia.itraders.container.ContainerEggPouch;
import iskallia.itraders.container.ContainerGhostPedestal;
import iskallia.itraders.container.ContainerReactor;
import iskallia.itraders.gui.container.GuiContainerCubeChamber;
import iskallia.itraders.gui.container.GuiContainerEggPouch;
import iskallia.itraders.gui.container.GuiContainerGhostPedestal;
import iskallia.itraders.gui.container.GuiContainerReactor;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorCore;
import iskallia.itraders.multiblock.reactor.entity.TileEntityReactorSlave;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int POUCH = 0;
    public static final int PEDESTAL = 1;
    public static final int POWER_CHAMBER = 2;
    public static final int REACTOR = 3;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == POUCH) {
            return new ContainerEggPouch(world, player);

        } else if (id == PEDESTAL) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof TileEntityGhostPedestal)
                return new ContainerGhostPedestal(world, player, (TileEntityGhostPedestal) tileEntity);

        } else if (id == POWER_CHAMBER) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof TileEntityCubeChamber)
                return new ContainerCubeChamber(world, player, (TileEntityCubeChamber) tileEntity);

        } else if (id == REACTOR) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof TileEntityReactorCore)
                return new ContainerReactor(world, player, (TileEntityReactorCore) tileEntity);
            if (tileEntity instanceof TileEntityReactorSlave) {
                TileEntityReactorSlave slave = (TileEntityReactorSlave) tileEntity;
                if (slave.hasMaster()) return new ContainerReactor(world, player, slave.getMaster());
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == POUCH) {
            return new GuiContainerEggPouch(world, player);

        } else if (id == PEDESTAL) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof TileEntityGhostPedestal)
                return new GuiContainerGhostPedestal(world, player, (TileEntityGhostPedestal) tileEntity);

        } else if (id == POWER_CHAMBER) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            return new GuiContainerCubeChamber(world, player, (TileEntityCubeChamber) tileEntity);

        } else if (id == REACTOR) {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
            if (tileEntity instanceof TileEntityReactorCore)
                return new GuiContainerReactor(world, player, (TileEntityReactorCore) tileEntity);
            if (tileEntity instanceof TileEntityReactorSlave) {
                TileEntityReactorSlave slave = (TileEntityReactorSlave) tileEntity;
                if (slave.hasMaster()) return new GuiContainerReactor(world, player, slave.getMaster());
            }
        }

        return null;
    }
}
