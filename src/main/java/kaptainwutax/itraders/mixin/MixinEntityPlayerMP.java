package kaptainwutax.itraders.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import io.netty.buffer.Unpooled;
import kaptainwutax.itraders.container.ContainerTrader;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends EntityPlayer implements IContainerListener {

	@Shadow private int currentWindowId;
	@Shadow private NetHandlerPlayServer connection;
	
	@Shadow protected abstract void getNextWindowId();
	
	public MixinEntityPlayerMP(World world, GameProfile gameProfile) {
		super(world, gameProfile);
	}
	
	@Inject(method = "displayVillagerTradeGui", at = @At("HEAD"), cancellable = true)
    private void displayTraderGui(IMerchant villager, CallbackInfo ci) {
        this.getNextWindowId();
        
        //This is the only change.
        this.openContainer = new ContainerTrader(this.inventory, villager, this.world);
        
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addListener((EntityPlayerMP)(Object)this);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open((EntityPlayerMP)(Object)this, this.openContainer));
        IInventory iinventory = ((ContainerMerchant)this.openContainer).getMerchantInventory();
        ITextComponent itextcomponent = villager.getDisplayName();
        this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:villager", itextcomponent, iinventory.getSizeInventory()));
        MerchantRecipeList merchantrecipelist = villager.getRecipes((EntityPlayerMP)(Object)this);

        if(merchantrecipelist != null) {
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.currentWindowId);
            merchantrecipelist.writeToBuf(packetbuffer);
            this.connection.sendPacket(new SPacketCustomPayload("MC|TrList", packetbuffer));
        }
        
        ci.cancel();
    }
	
}
