package kaptainwutax.itraders.world.data;

import java.util.HashMap;
import java.util.Map;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.world.storage.PouchInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class DataEggPouch extends WorldSavedData {

	private static final String DATA_NAME = Traders.MOD_ID + "_EggPouchData";
	private Map<String, PouchInventory> pouchesMap = new HashMap<>();

	public DataEggPouch() {
		super(DATA_NAME);
	}

	public DataEggPouch(String name) {
		super(name);
	}

	public PouchInventory getOrCreatePouch(EntityPlayer player) {
		String uuidString = player.getUniqueID().toString();

		if (!this.pouchesMap.containsKey(uuidString)) {
			this.pouchesMap.put(uuidString, new PouchInventory(false));
		}

		return this.pouchesMap.get(uuidString);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("pouches")) {
			NBTTagList pouches = nbt.getTagList("pouches", 10);

			pouches.forEach(nbtBase -> {
				NBTTagCompound pouch = (NBTTagCompound) nbtBase;
				this.pouchesMap.put(pouch.getString("key"), new PouchInventory(pouch.getCompoundTag("value")));
			});
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList pouches = new NBTTagList();

		pouchesMap.forEach((key, value) -> {
			NBTTagCompound pouch = new NBTTagCompound();
			pouch.setString("key", key);
			pouch.setTag("value", value.serializeNBT());
			pouches.appendTag(pouch);
		});

		compound.setTag("pouches", pouches);
		return compound;
	}

	@Override
	public boolean isDirty() {
		return true;
	}

	public static DataEggPouch get(World world) {
		DataEggPouch savedData = (DataEggPouch) world.getMapStorage().getOrLoadData(DataEggPouch.class, DATA_NAME);

		if (savedData == null) {
			savedData = new DataEggPouch();
			world.getMapStorage().setData(DATA_NAME, savedData);
		}

		return savedData;
	}

}
