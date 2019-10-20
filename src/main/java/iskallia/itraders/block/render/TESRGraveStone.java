package iskallia.itraders.block.render;

import iskallia.itraders.block.BlockGraveStone;
import iskallia.itraders.block.entity.TileEntityGraveStone;
import iskallia.itraders.init.InitBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TESRGraveStone extends TileEntitySpecialRenderer<TileEntityGraveStone> {
	
	@Override
	public void render(TileEntityGraveStone te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);  

		IBlockState state = te.getWorld().getBlockState(te.getPos());
		if(state.getBlock() != InitBlock.GRAVE_STONE)return;
		
		EnumFacing facing = state.getValue(BlockGraveStone.FACING);
        
		this.drawString(facing, te.getName() == null ? "Unknown" : te.getName(), 12.0f / 16.0f, x, y, z, 0.01f);
		this.drawString(facing, te.getMonths() == -1 ? "-" : te.getMonths() + " Months", 9.0f / 16.0f, x, y, z, 0.007f);
		this.drawString(facing, te.getBlocksMined() == -1 ? "-" : "Mined " + te.getBlocksMined() + " Blocks", 7.0f / 16.0f, x, y, z, 0.007f);
	}
	
	public void drawString(EnumFacing facing, String text, float yOffset, double x, double y, double z, float scale) {
        float size = (float)this.getFontRenderer().getStringWidth(text) * scale;
        float textCenter = (1.0f + size) / 2.0f;
        
		GlStateManager.pushMatrix();		
		GlStateManager.translate((float)x,(float)y, (float)z);
        
        if(facing == EnumFacing.NORTH) {
            GlStateManager.translate(textCenter, yOffset, 6.0f / 16.0f - 0.01f);     
            GlStateManager.rotate(180, 0, 0, 1);
        } else if(facing == EnumFacing.SOUTH) {
            GlStateManager.translate(-textCenter + 1, yOffset, (16.0f - 6.0f) / 16.0f + 0.01f);     
            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.rotate(180, 0, 1, 0);
        } else if(facing == EnumFacing.EAST) {
            GlStateManager.translate((16.0f - 6.0f) / 16.0f + 0.01f, yOffset, textCenter);     
            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.rotate(90, 0, 1, 0);
        } else if(facing == EnumFacing.WEST) {
            GlStateManager.translate(6.0f / 16.0f - 0.01f, yOffset, -textCenter + 1);     
            GlStateManager.rotate(180, 0, 0, 1);
            GlStateManager.rotate(270, 0, 1, 0);
        }
        
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.depthMask(false);
        
        this.getFontRenderer().drawString(text, 0, 0, 0xFFFFFF);
        GlStateManager.popMatrix();
	}

}
