package kaptainwutax.itraders.gui;

import kaptainwutax.itraders.container.ContainerEggPouch;
import kaptainwutax.itraders.gui.container.GuiContainerEggPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import scala.reflect.internal.Trees.New;

public class GuiHandler implements IGuiHandler {

	public static final int POUCH = 0;
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("RUN SERVER");
		if(id == POUCH) {
			System.out.println("POUCH");
			return new ContainerEggPouch(world, player);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("RUN CLIENT");
		if(id == POUCH) {
			System.out.println("POUCH");
			return new GuiContainerEggPouch(world, player);
		}
		
		return null;
	}

}
