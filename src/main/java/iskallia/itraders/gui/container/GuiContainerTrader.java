package iskallia.itraders.gui.container;

import java.io.IOException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;

import io.netty.buffer.Unpooled;
import iskallia.itraders.Traders;
import iskallia.itraders.container.ContainerTrader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiContainerTrader extends GuiMerchant {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation MERCHANT_GUI_TEXTURE = Traders.getResource("textures/gui/container/villager2.png");
    
    protected IMerchant merchant;
    private int selectedMerchantRecipe;
    private final ITextComponent chatComponent;
    protected int blitOffset;
    
    private int field_19161;

	private int slotOffset;
	private boolean field_19164;
    
    private WidgetButtonPage[] tradeButtons = new WidgetButtonPage[7];
    
	protected MerchantRecipeList recipes;
	 
    protected InventoryPlayer playerInventory;

    public GuiContainerTrader(InventoryPlayer playerInventory, IMerchant merchant, World world) {
        super(playerInventory, merchant, world);
        this.inventorySlots = new ContainerTrader(playerInventory, merchant, world);
        this.playerInventory = playerInventory;
        this.merchant = merchant;
        this.chatComponent = merchant.getDisplayName();
        this.xSize = 276;
        this.recipes = this.merchant.getRecipes(playerInventory.player);
    }

    @Override
	public void initGui() {
        super.initGui();     
        this.tradeButtons = new WidgetButtonPage[7];
        
        //Removes the buttons from GuiMerchant so we can add our own.
        this.buttonList.clear();
        
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        int heightOffset = centerY + 16 + 2;

        for(int i = 0; i < this.tradeButtons.length; i++) {
            this.tradeButtons[i] = this.addButton(new WidgetButtonPage(i, centerX + 5, heightOffset));
            heightOffset += 20;
         }
    }
    
    @Override
	protected void actionPerformed(GuiButton button) throws IOException {  	
    	super.actionPerformed(button);
    	
    	//Get which trade button was clicked.
    	int id = button.id + this.slotOffset;
		GuiContainerTrader.this.selectedMerchantRecipe = id;	
		
		//Why not reuse the packet so it also stores if it's a shift click?
        if(GuiScreen.isShiftKeyDown())id |= (1 << 31);
        
		((ContainerTrader)GuiContainerTrader.this.inventorySlots).setCurrentRecipeIndex(id);
        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
        packetbuffer.writeInt(id);
        GuiContainerTrader.this.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|TrSel", packetbuffer));
    }  

	@Override
	public void updateScreen() {
		super.updateScreen();
		MerchantRecipeList recipeList = this.merchant.getRecipes(this.mc.player);
		if(recipeList == null)return;
		
		//Takes care of the scrolling. Note that Mouse Tweaks messes with this.
		int scroll = Mouse.getDWheel();
		while(scroll >= 120) {scroll -= 120; this.slotOffset--;}	
		while(scroll <= -120) {scroll += 120; this.slotOffset++;}
		
		int upperLimit = MathHelper.clamp(recipeList.size() - 7, 0, recipeList.size() - 7);
		this.slotOffset = MathHelper.clamp(this.slotOffset, 0, upperLimit);
	}
    
    @Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, this.blitOffset, 0.0F, 0.0F, this.xSize, this.ySize, 256, 512);
        
        MerchantRecipeList recipeList = this.merchant.getRecipes(this.mc.player);
        if(recipeList == null)return;
        
        if(recipeList != null && !recipeList.isEmpty()) {
            int k = this.selectedMerchantRecipe;

            if(k < 0 || k >= recipeList.size()) {
                return;
            }

            MerchantRecipe merchantrecipe = recipeList.get(k);

            //Draw the X when the trade locks.
            if(merchantrecipe.isRecipeDisabled()) {
                this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableLighting();
                this.blit(this.guiLeft + 83 + 99, this.guiTop + 35, this.blitOffset, 311.0F, 0.0F, 28, 21, 256, 512);
            }
        }
    }
    
    @Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.chatComponent.getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2 + 50, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8 + 100, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        
        MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.player);
        this.merchant.setRecipes(new MerchantRecipeList());
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.merchant.setRecipes(merchantrecipelist);

        if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
        {
            int i = (this.width - this.xSize) / 2;
            int j = (this.height - this.ySize) / 2;
            int k = this.selectedMerchantRecipe;

            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            
            this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
            this.method_20221(i, j, merchantrecipelist);
            int int_6 = i + 5 + 5;
            int int_5 = j + 16 + 1;
            int int_7 = 0;
            Iterator var10 = merchantrecipelist.iterator();

            MerchantRecipe tradeOffer_1;
            
            while(var10.hasNext()) {
              tradeOffer_1 = (MerchantRecipe)var10.next();
              if (this.method_20220(merchantrecipelist.size()) && (int_7 < this.slotOffset || int_7 >= 7 + this.slotOffset)) {
                 ++int_7;
              } else {
                 ItemStack itemStack_1 = tradeOffer_1.getItemToBuy();
                 ItemStack itemStack_2 = tradeOffer_1.getItemToBuy();
                 ItemStack itemStack_3 = tradeOffer_1.getSecondItemToBuy();
                 ItemStack itemStack_4 = tradeOffer_1.getItemToSell();
                 this.itemRender.zLevel = 100.0F;
                 int int_8 = int_5 + 2;
                 this.method_20222(itemStack_2, itemStack_1, int_6, int_8);
                 if (!itemStack_3.isEmpty()) {
                    this.itemRender.renderItemAndEffectIntoGUI(itemStack_3, i + 5 + 35, int_8);
                    this.itemRender.renderItemOverlays(this.fontRenderer, itemStack_3, i + 5 + 35, int_8);
                 }

                 this.method_20223(tradeOffer_1, i, int_8);
                 this.itemRender.renderItemAndEffectIntoGUI(itemStack_4, i + 5 + 68, int_8);
                 this.itemRender.renderItemOverlays(this.fontRenderer, itemStack_4, i + 5 + 68, int_8);
                 this.itemRender.zLevel = 0.0F;
                 int_5 += 20;
                 ++int_7;
              }
           }
           
            GuiContainerTrader.WidgetButtonPage[] var18 = this.tradeButtons;
            int var19 = var18.length;

            for(int var20 = 0; var20 < var19; ++var20) {
               GuiContainerTrader.WidgetButtonPage merchantScreen$WidgetButtonPage_1 = var18[var20];
            	
               if (merchantScreen$WidgetButtonPage_1.isMouseOver()) {           	  
                  merchantScreen$WidgetButtonPage_1.renderToolTip(mouseX, mouseY);
               }

               merchantScreen$WidgetButtonPage_1.visible = merchantScreen$WidgetButtonPage_1.id < merchantrecipelist.size();
            }

            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
        }

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    private boolean method_20220(int int_1) {
        return int_1 > 7;
     }
    
    private void method_20221(int int_1, int int_2, MerchantRecipeList traderOfferList_1) {
    	RenderHelper.disableStandardItemLighting();
        int int_3 = traderOfferList_1.size() + 1 - 7;
        if (int_3 > 1) {
           int int_4 = 139 - (27 + (int_3 - 1) * 139 / int_3);
           int int_5 = 1 + int_4 / int_3 + 139 / int_3;
           int int_7 = Math.min(113, this.slotOffset * int_5);
           if (this.slotOffset == int_3 - 1) {
              int_7 = 113;
           }

           blit(int_1 + 94, int_2 + 18 + int_7, this.blitOffset, 0.0F, 199.0F, 6, 27, 256, 512);
        } else {
           blit(int_1 + 94, int_2 + 18, this.blitOffset, 6.0F, 199.0F, 6, 27, 256, 512);
        }

        RenderHelper.enableGUIStandardItemLighting();
     }
    
	private void method_20222(ItemStack itemStack_1, ItemStack itemStack_2, int int_1, int int_2) {
        this.itemRender.renderItemAndEffectIntoGUI(itemStack_1, int_1, int_2);
        if (itemStack_2.getCount() == itemStack_1.getCount()) {
           this.itemRender.renderItemOverlays(this.fontRenderer, itemStack_1, int_1, int_2);
        } else {
           this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemStack_2, int_1, int_2, itemStack_2.getCount() == 1 ? "1" : null);
           this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemStack_1, int_1 + 14, int_2, itemStack_1.getCount() == 1 ? "1" : null);
           this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
           this.blitOffset += 300;
           RenderHelper.disableStandardItemLighting();
           blit(int_1 + 7, int_2 + 12, this.blitOffset, 0.0F, 176.0F, 9, 2, 256, 512);
           RenderHelper.enableGUIStandardItemLighting();
           this.blitOffset -= 300;
        }

     }

    private void method_20223(MerchantRecipe tradeOffer_1, int int_1, int int_2) {
    	RenderHelper.disableStandardItemLighting();
    	
        GlStateManager.enableBlend();
        this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
        if (tradeOffer_1.isRecipeDisabled()) {
           blit(int_1 + 5 + 35 + 20, int_2 + 3, this.blitOffset, 25.0F, 171.0F, 10, 9, 256, 512);
        } else {
           blit(int_1 + 5 + 35 + 20, int_2 + 3, this.blitOffset, 15.0F, 171.0F, 10, 9, 256, 512);
        }

        RenderHelper.enableGUIStandardItemLighting();
     }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.field_19164 = false;
        int int_2 = (this.width - this.xSize) / 2;
        int int_3 = (this.height - this.ySize) / 2;
        
        MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(this.mc.player);
        if(merchantrecipelist == null)return;
        
        if (this.method_20220(merchantrecipelist.size()) && mouseX > (double)(int_2 + 94) && 
        		mouseX < (double)(int_2 + 94 + 6) && mouseY > (double)(int_3 + 18) && mouseY <= (double)(int_3 + 18 + 139 + 1)) {
           this.field_19164 = true;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    private void blit(int int_1, int int_2, int int_3, float float_1, float float_2, int int_4, int int_5, int int_6, int int_7) {
    	innerBlit(int_1, int_1 + int_4, int_2, int_2 + int_5, int_3, int_4, int_5, float_1, float_2, int_7, int_6);	
	}
	
    protected static void innerBlit(int int_1, int int_2, int int_3, int int_4, int int_5, float float_1, float float_2, float float_3, float float_4) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(int_1, int_4, int_5).tex(float_1, float_4).endVertex();
        bufferbuilder.pos(int_2, int_4, int_5).tex(float_2, float_4).endVertex();
        bufferbuilder.pos(int_2, int_3, int_5).tex(float_2, float_3).endVertex();
        bufferbuilder.pos(int_1, int_3, int_5).tex(float_1, float_3).endVertex();
		tessellator.draw();
	}
    
    private static void innerBlit(int int_1, int int_2, int int_3, int int_4, int int_5, int int_6, int int_7, float float_1, float float_2, int int_8, int int_9) {
        innerBlit(int_1, int_2, int_3, int_4, int_5, (float_1 + 0.0F) / int_8, (float_1 + int_6) / int_8, (float_2 + 0.0F) / int_9, (float_2 + int_7) / int_9);
    }

	@SideOnly(Side.CLIENT)
	class WidgetButtonPage extends GuiButton {
		final int id;
	
		public WidgetButtonPage(int id, int x, int y) {
			super(id, x, y, 89, 20, "");
			this.id = id;
			this.visible = false;
		}
	
		public int getId() {
			return this.id;
		}
	  
		public void renderToolTip(int x, int y) {
			MerchantRecipeList recipes = GuiContainerTrader.this.merchant.getRecipes(GuiContainerTrader.this.mc.player);
			if(recipes == null)return;
			
			MerchantRecipe recipe = recipes.get(this.id + GuiContainerTrader.this.slotOffset);
		  
			if(this.hovered && recipes.size() > this.id + GuiContainerTrader.this.slotOffset) {
				ItemStack stackToRender;
				
				if(x < this.x + 20) {
					stackToRender = recipe.getItemToBuy();
					GuiContainerTrader.this.renderToolTip(stackToRender, x, y);
				} else if(x < this.x + 50 && x > this.x + 30) {
					stackToRender = recipe.getSecondItemToBuy();
					
					if(!stackToRender.isEmpty()) {
						GuiContainerTrader.this.renderToolTip(stackToRender, x, y);
					}
				} else if(x > this.x + 65) {
					stackToRender = recipe.getItemToSell();
					GuiContainerTrader.this.renderToolTip(stackToRender, x, y);
				}
			}
	  	}		
	  
	}
	
}