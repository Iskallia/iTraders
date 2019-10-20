package iskallia.itraders.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import iskallia.itraders.gui.container.GuiContainerTrader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.IMerchant;
import net.minecraft.world.World;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    public MixinEntityPlayerSP(World world, GameProfile playerProfile) {
		super(world, playerProfile);
	}

    @Inject(method = "displayVillagerTradeGui", at = @At("HEAD"), cancellable = true)
	private void displayTraderGui(IMerchant villager, CallbackInfo ci) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiContainerTrader(this.inventory, villager, this.world));
        ci.cancel();
    }
	
}
