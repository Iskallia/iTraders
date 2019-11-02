package iskallia.itraders.config;

import com.google.gson.annotations.Expose;
import iskallia.itraders.config.definition.TerrariumWorldDefinition;

import java.util.ArrayList;
import java.util.List;

public class ConfigTerrarium extends Config {
	/**
	 * The number of worlds that can be used in terrariums.
	 */
	@Expose
	public int WORLDS_COUNT;

	/**
	 * The list of worlds that can be used in terrariums.
	 */
	@Expose
	public List<TerrariumWorldDefinition> WORLDS = new ArrayList<>();

	/**
	 * @return The location of the JSON file containing the configuration for terrariums.
	 */
	@Override
	public String getLocation() {
		return "terrarium.json";
	}

	/**
	 * Reset the configuration
	 */
	@Override
	protected void resetConfig() {
		this.WORLDS.add(new TerrariumWorldDefinition(0, 0, 0, 0));
		this.WORLDS.add(new TerrariumWorldDefinition(1, 1, 1, 1));

		this.WORLDS_COUNT = this.WORLDS.size();
	}
}