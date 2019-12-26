package iskallia.itraders.block.render;

import iskallia.itraders.block.BlockFacing;
import iskallia.itraders.block.entity.TileEntityJar;
import iskallia.itraders.init.InitBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.World;

public class TESRJar extends TileEntitySpecialRenderer<TileEntityJar> {
	
    private static ModelPlayer playerModel = new ModelPlayer(.1f, false);
    private static Entity dummyEntity;
	
	@Override
	public void render(TileEntityJar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		EnumFacing facing = te.getFacing();
		if(te.getFacing() == null)return;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5f, y + 1.01f, z + 0.5f);
		GlStateManager.translate(facing.getFrontOffsetX() * 0.08f, 0, facing.getFrontOffsetZ() * 0.08f);

		GlStateManager.scale(0.8f, 0.8f, 0.8f);
		
		GlStateManager.rotate(-90.0f, 1, 0, 0);
		
		if(facing.getAxis() == Axis.X) {
			GlStateManager.rotate(180.0f, 0, 1, 0);
			GlStateManager.rotate(180.0f, 1, 0, 0);
		}

		GlStateManager.rotate(facing.getHorizontalAngle(), 0, 0, 1);	
		Minecraft.getMinecraft().getRenderItem().renderItem(te.getFood(), ItemCameraTransforms.TransformType.GROUND);	
		GlStateManager.popMatrix();
		
		this.renderModel(te, x, y, z, facing);
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
	}

	private void renderModel(TileEntityJar te, double x, double y, double z, EnumFacing facing) {
		if(te.getDonator().isEmpty())return;
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0d, -0.63d, 0.0d);
		
        if(this.dummyEntity == null) {
            initDummyEntity(te.getWorld());
        }

        bindTexture(te.getSkin().getLocationSkin());

        double scale = 0.025;

        if(this.dummyEntity != null) {
            this.playerModel.isRiding = true;
            
            GlStateManager.pushMatrix();         
            GlStateManager.translate(x + 0.5f, y + 1f, z + 0.5f);
            GlStateManager.translate(-facing.getFrontOffsetX() * 0.22f, 0.0f, -facing.getFrontOffsetZ() * 0.22f);       
            
            if(facing.getAxis() == Axis.X) {
            	facing = facing.getOpposite();
            }
            
            GlStateManager.rotate(facing.getHorizontalAngle(), 0f, 1f, 0f);
            GlStateManager.rotate(180f, 0f, 0f, 1f);
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(0, -scale * 125, 0);
            this.playerModel.setRotationAngles(0, 0, 0, 0, 0, (float)scale, this.dummyEntity);
            this.playerModel.bipedHead.render(1f);
            this.playerModel.bipedBody.render(1f);
            this.playerModel.bipedLeftLeg.render(1.02f);
            this.playerModel.bipedRightLeg.render(1.02f);
            this.playerModel.bipedLeftArm.render(1.05f);
            this.playerModel.bipedRightArm.render(1.05f);

            this.playerModel.bipedBodyWear.render(1f);
            this.playerModel.bipedHeadwear.render(1f);
            this.playerModel.bipedLeftLegwear.render(1.02f);
            this.playerModel.bipedRightLegwear.render(1.02f);
            this.playerModel.bipedLeftArmwear.render(1.05f);
            GlStateManager.translate(0f, 0f, -11.05f);
            this.playerModel.bipedRightArmwear.render(1.05f);
            GlStateManager.popMatrix();
        }	
        
        GlStateManager.popMatrix();
	}
	
    protected float getFacingAngle(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return 0;
            case SOUTH:
                return 180;
            case WEST:
                return 90;
            case EAST:
            default:
                return -90;
        }
    }
	
    private void initDummyEntity(World world) {
    	this.dummyEntity = new Entity(world) {
            @Override
            protected void entityInit() {}

            @Override
            protected void readEntityFromNBT(NBTTagCompound compound) {}

            @Override
            protected void writeEntityToNBT(NBTTagCompound compound) {}
        };
        
        this.dummyEntity.posX = 0;
        this.dummyEntity.posY = 0;
        this.dummyEntity.posZ = 0;
    }
	
}
