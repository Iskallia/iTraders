package iskallia.itraders.block.render;

import iskallia.itraders.block.BlockFacing;
import iskallia.itraders.block.entity.TileEntityJar;
import iskallia.itraders.init.InitBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TESRJar extends TileEntitySpecialRenderer<TileEntityJar> {
	
	@Override
	public void render(TileEntityJar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		EnumFacing facing = te.getFacing();
		if(te.getFacing() == null)return;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5f, y + 1.01f, z + 0.5f);
		GlStateManager.scale(1.1f, 1.1f, 1.1f);
		GlStateManager.rotate(-90.0f, 1, 0, 0);
		
		if(facing == EnumFacing.EAST) {
			GlStateManager.rotate(180.0f, 0, 1, 0);
			GlStateManager.rotate(180.0f, 1, 0, 0);
		}
		
		GlStateManager.rotate(facing.getHorizontalAngle(), 0, 0, 1);
		Minecraft.getMinecraft().getRenderItem().renderItem(te.getFood(), ItemCameraTransforms.TransformType.GROUND);	
		GlStateManager.popMatrix();
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}
	
}
