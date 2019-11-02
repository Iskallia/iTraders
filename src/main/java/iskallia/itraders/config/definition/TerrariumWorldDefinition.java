package iskallia.itraders.config.definition;

import com.google.gson.annotations.Expose;

/**
 * Defines the values we need to know which world seed and where in it the terrarium world will generate.
 */
public class TerrariumWorldDefinition {
    /**
     * The Minecraft seed to use for world generation.
     */
    @Expose
    protected long seed;

    /**
     * The x-coordinate (East/West) to use for world generation (Terrarium will display x to x + 48)
     */
    @Expose
    protected long x;

    /**
     * The y-coordinate (Up/Down) to display in the terrarium (Terrarium will display y to y + 48)
     */
    @Expose
    protected long y;

    /**
     * The z-coordinate (North/South) to use for world generation (Terrarium will display x to x + 48)
     */
    @Expose
    protected long z;

    /**
     * Instantiate a TerrariumWorldDefinition object.
     *
     * @param seed The Minecraft seed to use for world generation.
     * @param x The x-coordinate (East/West) to use for world generation (Terrarium will display x to x + 48)
     * @param y The y-coordinate (Up/Down) to display in the terrarium (Terrarium will display y to y + 48)
     * @param z The z-coordinate (North/South) to use for world generation (Terrarium will display x to x + 48)
     */
    public TerrariumWorldDefinition(long seed, long x, long y, long z) {
        this.seed = seed;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return The Minecraft seed to use for world generation.
     */
    public long getSeed() {
        return this.seed;
    }

    /**
     * @return The x-coordinate (East/West) to use for world generation (Terrarium will display x to x + 48)
     */
    public long getX() {
        return this.x;
    }

    /**
     * @return The y-coordinate (Up/Down) to display in the terrarium (Terrarium will display y to y + 48)
     */
    public long getY() {
        return y;
    }

    /**
     * @return The z-coordinate (North/South) to use for world generation (Terrarium will display x to x + 48)
     */
    public long getZ() {
        return z;
    }
}
