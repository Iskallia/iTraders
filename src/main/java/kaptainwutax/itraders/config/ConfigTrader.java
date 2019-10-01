package kaptainwutax.itraders.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import kaptainwutax.itraders.util.Product;
import kaptainwutax.itraders.util.Trade;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ConfigTrader extends Config {

	@Expose public int TRADES_COUNT;
	@Expose public List<Trade> TRADES = new ArrayList<Trade>();
	
	@Override
	public String getLocation() {
		return "trader.json";
	}

	@Override
	protected void resetConfig() {
		this.TRADES_COUNT = 5;
		
		this.TRADES.add(new Trade(new Product(Items.APPLE, 0, 8, null), null, new Product(Items.GOLDEN_APPLE, 0, 1, null)));
		this.TRADES.add(new Trade(new Product(Items.GOLDEN_APPLE, 0, 8, null), null, new Product(Items.GOLDEN_APPLE, 1, 1, null)));
		
        NBTTagCompound nbt = new NBTTagCompound();
        
        NBTTagList enchantments = new NBTTagList();
        
        NBTTagCompound knockback = new NBTTagCompound();
        knockback.setShort("id", (short)Enchantment.getEnchantmentID(Enchantments.KNOCKBACK));
        knockback.setShort("lvl", (short)10);
        enchantments.appendTag(knockback);      

        nbt.setTag("ench", enchantments);
		
		this.TRADES.add(new Trade(new Product(Items.GOLDEN_APPLE, 1, 8, null), null, new Product(Items.STICK, 0, 1, nbt)));
	}

}
