package kaptainwutax.itraders.gui;

import kaptainwutax.itraders.container.ContainerEggPouch;
import kaptainwutax.itraders.gui.container.GuiContainerEggPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public static final int POUCH = 0;

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == POUCH) {
			return new ContainerEggPouch(world, player);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		if (id == POUCH) {
			return new GuiContainerEggPouch(world, player);
		}

		return null;
	}

}
