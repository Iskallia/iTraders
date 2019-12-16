package iskallia.itraders.config.definition;

import com.google.gson.annotations.Expose;

public class LootDefinition {

	@Expose
	protected String itemId;

	@Expose
	protected int weight;

	public LootDefinition(String itemId, int weight) {
		this.itemId = itemId;
		this.weight = weight;
	}

	public String getItemId() {
		return itemId;
	}


	public int getWeight() {
		return weight;
	}
	
	

}
