package iskallia.itraders.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.annotations.Expose;

import iskallia.itraders.util.Product;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

public class ConfigJar extends Config {

	@Expose public List<Product> FOODS = new ArrayList<>();
	@Expose public List<Product> POOPS = new ArrayList<>();
	
	@Override
	public String getLocation() {
		return "jar.json";
	}

	@Override
	protected void resetConfig() {	
		Item.REGISTRY.forEach(item -> {
			if(item.getRegistryName().getResourceDomain().equals("minecraft")) {
				if(item instanceof ItemFood) {
					this.FOODS.add(new Product(item, 0, 1, null));
				}
			}
		});
		
		this.POOPS.add(new Product(Items.DIAMOND, 0, 1, null));
		this.POOPS.add(new Product(Items.EMERALD, 0, 1, null));
		this.POOPS.add(new Product(Items.IRON_INGOT, 0, 1, null));
		this.POOPS.add(new Product(Items.GOLD_INGOT, 0, 1, null));
		this.POOPS.add(new Product(Items.QUARTZ, 0, 1, null));
	}

}
