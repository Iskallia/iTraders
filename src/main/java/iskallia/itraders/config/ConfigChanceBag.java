package iskallia.itraders.config;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import iskallia.itraders.config.definition.LootDefinition;

public class ConfigChanceBag extends Config {
	
	@Expose
	public List<LootDefinition> COMMON_LOOT = new ArrayList<LootDefinition>();
	
	@Expose
	public List<LootDefinition> RARE_LOOT = new ArrayList<LootDefinition>();
	
	@Expose
	public List<LootDefinition> EPIC_LOOT = new ArrayList<LootDefinition>();

	@Override
	public String getLocation() {
		return "chanceBag.json";
	}

	@Override
	protected void resetConfig() {
		this.COMMON_LOOT.add(new LootDefinition("minecraft:wooden_sword", 20));
		this.COMMON_LOOT.add(new LootDefinition("minecraft:leather_chestplate", 20));

		this.RARE_LOOT.add(new LootDefinition("minecraft:stone_sword", 20));
		this.RARE_LOOT.add(new LootDefinition("minecraft:iron_chestplate", 20));

		this.EPIC_LOOT.add(new LootDefinition("minecraft:diamond_sword", 20));
		this.EPIC_LOOT.add(new LootDefinition("minecraft:diamond_chestplate", 20));		
	}

}
