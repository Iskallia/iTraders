package iskallia.itraders.capability;

import io.netty.util.internal.MathUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.EnergyStorage;

public class ModifiableEnergyStorage extends EnergyStorage {

    public ModifiableEnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public ModifiableEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public ModifiableEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public ModifiableEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergy(int energy) {
        this.energy = MathHelper.clamp(energy, 0, capacity);
    }

    public int voidEnergy(int energyToVoid, boolean simulated) {
        if (energy < energyToVoid)
            return 0;

        if (!simulated)
            energy -= energyToVoid;

        return energyToVoid;
    }

}
