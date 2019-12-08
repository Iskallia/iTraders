package iskallia.itraders.config;

import com.google.gson.annotations.Expose;

public class ConfigAccelerationBottle extends Config {

	@Expose
	public int DURATION_PER_USE;

	@Expose
	public int MAX_CONTAINED_SUBS;

	@Expose
	public int SPEED_MULTIPLIER;

	@Override
	public String getLocation() {
		return "accelerationBottle.json";
	}

	@Override
	protected void resetConfig() {
		this.DURATION_PER_USE = 10;
		this.MAX_CONTAINED_SUBS = 10;
		this.SPEED_MULTIPLIER = 5;
	}

}
