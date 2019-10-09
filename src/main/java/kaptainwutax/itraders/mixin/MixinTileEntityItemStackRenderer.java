package kaptainwutax.itraders.mixin;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;

import kaptainwutax.itraders.SkinProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShield;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;

@Mixin(TileEntityItemStackRenderer.class)
public abstract class MixinTileEntityItemStackRenderer {

	@Shadow private static TileEntityShulkerBox[] SHULKER_BOXES = new TileEntityShulkerBox[16];
    @Shadow private TileEntityChest chestBasic = new TileEntityChest(BlockChest.Type.BASIC);
    @Shadow private TileEntityChest chestTrap = new TileEntityChest(BlockChest.Type.TRAP);
    @Shadow private TileEntityEnderChest enderChest = new TileEntityEnderChest();
    @Shadow private TileEntitySkull skull = new TileEntitySkull();
	@Shadow @Final private TileEntityBanner banner;
	@Shadow @Final private TileEntityBed bed;
    @Shadow private ModelShield modelShield = new ModelShield();
    
	@Overwrite
    public void renderByItem(ItemStack stack, float partialTicks)
    {
        Item item = stack.getItem();

        if (item == Items.BANNER) {
            this.banner.setItemValues(stack, false);
            TileEntityRendererDispatcher.instance.render(this.banner, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        } else if(item == Items.BED) {
            this.bed.setItemValues(stack);
            TileEntityRendererDispatcher.instance.render(this.bed, 0.0D, 0.0D, 0.0D, 0.0F);
        } else if(item == Items.SHIELD) {
            if(stack.getSubCompound("BlockEntityTag") != null) {
                this.banner.setItemValues(stack, true);
                Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_DESIGNS.getResourceLocation(this.banner.getPatternResourceLocation(), this.banner.getPatternList(), this.banner.getColorList()));
            } else {
                Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
            }

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            this.modelShield.render();
            GlStateManager.popMatrix();
        } else if (item == Items.SKULL) {
            GameProfile gameprofile = null;

            if(stack.hasTagCompound()) {
                NBTTagCompound nbttagcompound = stack.getTagCompound();

                if(nbttagcompound.hasKey("SkullOwner", 10)) {
                    gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                } else if(nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.getString("SkullOwner"))) {
                	/* OLD CODE
                    GameProfile gameprofile1 = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    gameprofile = TileEntitySkull.updateGameProfile(gameprofile1);
                    nbttagcompound.removeTag("SkullOwner");
                    nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                	*/
                	String username = nbttagcompound.getString("SkullOwner");
                	
                	nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), 
                			new GameProfile((UUID)null, username)));
                	
                    SkinProfile.updateGameProfile(new GameProfile((UUID)null, username), 
	                    newProfile -> {
	                    	nbttagcompound.removeTag("SkullOwner");
	                        nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), newProfile));
	                    }
                    );
                }
            }

            if(TileEntitySkullRenderer.instance != null) {
                GlStateManager.pushMatrix();
                GlStateManager.disableCull();
                TileEntitySkullRenderer.instance.renderSkull(0.0F, 0.0F, 0.0F, EnumFacing.UP, 180.0F, stack.getMetadata(), gameprofile, -1, 0.0F);
                GlStateManager.enableCull();
                GlStateManager.popMatrix();
            }
        } else if(item == Item.getItemFromBlock(Blocks.ENDER_CHEST)) {
            TileEntityRendererDispatcher.instance.render(this.enderChest, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        } else if(item == Item.getItemFromBlock(Blocks.TRAPPED_CHEST)) {
            TileEntityRendererDispatcher.instance.render(this.chestTrap, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        } else if(Block.getBlockFromItem(item) instanceof BlockShulkerBox) {
            TileEntityRendererDispatcher.instance.render(SHULKER_BOXES[BlockShulkerBox.getColorFromItem(item).getMetadata()], 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        } else if(Block.getBlockFromItem(item) != Blocks.CHEST) {
        	net.minecraftforge.client.ForgeHooksClient.renderTileItem(stack.getItem(), stack.getMetadata());
        } else {
            TileEntityRendererDispatcher.instance.render(this.chestBasic, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        }
    }
	
}
