package iskallia.itraders.gui;

import iskallia.itraders.block.entity.TileEntityCubeChamber;
import iskallia.itraders.block.entity.TileEntityGhostPedestal;
import iskallia.itraders.container.ContainerCubeChamber;
import iskallia.itraders.container.ContainerEggPouch;
import iskallia.itraders.container.ContainerGhostPedestal;
import iskallia.itraders.gui.container.GuiContainerCubeChamber;
import iskallia.itraders.gui.container.GuiContainerEggPouch;
import iskallia.itraders.gui.container.GuiContainerGhostPedestal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public static final int POUCH = 0;
	public static final int PEDESTAL = 1;
	public static final int POWER_CHAMBER = 2;

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
		}
		
		return null;
	}
}
