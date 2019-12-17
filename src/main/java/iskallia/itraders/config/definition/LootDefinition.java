package iskallia.itraders.config.definition;

import javax.annotation.Nullable;

import com.google.gson.annotations.Expose;

public class LootDefinition {

	@Expose
	protected String itemId;

	@Expose
	protected int weight;
	
	@Expose
	protected int amount;

	@Expose
	protected int meta;

	@Expose
	@Nullable
	protected String nbt;

	public LootDefinition(String itemId, int weight, int amount, int meta, @Nullable String nbt) {
		this.itemId = itemId;
		this.weight = weight;
		this.amount = amount;
		this.meta = meta;
		this.nbt = nbt;
	}

	public String getItemId() {
		return itemId;
	}

	public int getWeight() {
		return weight;
	}

	public int getAmount() {
		return amount;
	}

	public int getMeta() {
		return meta;
	}

	public String getNbt() {
		return nbt;
	}

}
