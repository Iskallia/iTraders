package kaptainwutax.itraders.mixin;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import kaptainwutax.itraders.SkinProfile;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;

@Mixin(TileEntityItemStackRenderer.class)
public abstract class MixinTileEntityItemStackRenderer {

	@Shadow private TileEntitySkull skull = new TileEntitySkull();

	@Inject(method = "renderByItem", at = @At("HEAD"), cancellable = true)
	public void renderByItem(ItemStack stack, CallbackInfo ci) {
		Item item = stack.getItem();

		if (item != Items.SKULL)return;
	
		GameProfile gameprofile = null;

		if (stack.hasTagCompound()) {
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound.hasKey("SkullOwner", 10)) {
				gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
			} else if (nbttagcompound.hasKey("SkullOwner", 8)
					&& !StringUtils.isBlank(nbttagcompound.getString("SkullOwner"))) {
				/*
				 * OLD CODE GameProfile gameprofile1 = new GameProfile((UUID)null,
				 * nbttagcompound.getString("SkullOwner")); gameprofile =
				 * TileEntitySkull.updateGameProfile(gameprofile1);
				 * nbttagcompound.removeTag("SkullOwner"); nbttagcompound.setTag("SkullOwner",
				 * NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
				 */
				String username = nbttagcompound.getString("SkullOwner");

				nbttagcompound.setTag("SkullOwner",
						NBTUtil.writeGameProfile(new NBTTagCompound(), new GameProfile((UUID) null, username)));

				SkinProfile.updateGameProfile(new GameProfile((UUID) null, username), newProfile -> {
					nbttagcompound.removeTag("SkullOwner");
					nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), newProfile));
				});
			}
		}

		if(TileEntitySkullRenderer.instance != null) {
			GlStateManager.pushMatrix();
			GlStateManager.disableCull();
			TileEntitySkullRenderer.instance.renderSkull(0.0F, 0.0F, 0.0F, EnumFacing.UP, 180.0F,
					stack.getMetadata(), gameprofile, -1, 0.0F);
			GlStateManager.enableCull();
			GlStateManager.popMatrix();
		}
		
		ci.cancel();
	}

}
