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

public class ConfigFighter extends Config {

	@Expose public int LOOT_COUNT;
	@Expose public List<Product> LOOT = new ArrayList<>();
	
	@Override
	public String getLocation() {
		return "fighter.json";
	}

	@Override
	protected void resetConfig() {
		this.LOOT_COUNT = 3;
		
		this.LOOT.add(new Product(Items.REDSTONE, 0, 64, null));
		this.LOOT.add(new Product(Items.APPLE, 0, 3, null));
		this.LOOT.add(new Product(Items.GOLDEN_APPLE, 0, 3, null));	
		this.LOOT.add(new Product(Items.GOLDEN_APPLE, 1, 3, null));	
		
        NBTTagCompound nbt = new NBTTagCompound();
        
        NBTTagList enchantments = new NBTTagList();
        
        NBTTagCompound knockback = new NBTTagCompound();
        knockback.setShort("id", (short)Enchantment.getEnchantmentID(Enchantments.KNOCKBACK));
        knockback.setShort("lvl", (short)10);
        enchantments.appendTag(knockback);      

        nbt.setTag("ench", enchantments);
		
		this.LOOT.add(new Product(Items.STICK, 0, 1, nbt));
	}

}
