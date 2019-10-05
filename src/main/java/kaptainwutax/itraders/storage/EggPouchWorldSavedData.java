package kaptainwutax.itraders.storage;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.item.ItemSpawnEggFighter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EggPouchWorldSavedData extends WorldSavedData {

    private static final String DATA_NAME = Traders.MOD_ID + "_EggPouchData";

    public static EggPouchWorldSavedData get(World world) {
        EggPouchWorldSavedData savedData = (EggPouchWorldSavedData) world.getMapStorage()
                .getOrLoadData(EggPouchWorldSavedData.class, DATA_NAME);

        // Lazy init instance
        if (savedData == null) {
            savedData = new EggPouchWorldSavedData();
            world.getMapStorage().setData(DATA_NAME, savedData);
        }

        return savedData;
    }

    /* ------------------------------- */

    /*
     * EggPouchWorldSavedData NBT {
     *  content: {
     *   "nickname": [{...}, {...}],
     *   "anotherBud": [{...}],
     *   "yetAnother": []
     *  }
     * }
     */

    protected Map<String, List<ItemStack>> pouches;

    public EggPouchWorldSavedData() {
        this(DATA_NAME);
    }

    public EggPouchWorldSavedData(String name) {
        super(name);
        this.pouches = new HashMap<>();
    }

    /* ------------------------------- */

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        System.out.printf("Reading from NBT: %s\n", nbt);

        if (nbt.hasKey("contents", 10)) { // 10 - COMPOUND
            NBTTagCompound contents = nbt.getCompoundTag("contents");
            contents.getKeySet().forEach(nickname -> {
                List<ItemStack> items = new LinkedList<>();

                for (NBTBase itemNBT : contents.getTagList(nickname, 10)) { // 10 - COMPOUND
                    if (!(itemNBT instanceof NBTTagCompound)) continue;
                    items.add(new ItemStack((NBTTagCompound) itemNBT));
                }

                pouches.put(nickname, items);
            });
        }

        System.out.printf("Read Contents: %s\n", pouches);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        System.out.printf("Writing NBT: %s\n", nbt);

        NBTTagCompound contents = new NBTTagCompound();
        pouches.forEach((nickname, eggs) -> {
            NBTTagList nbtList = new NBTTagList();
            for (ItemStack egg : eggs) {
                nbtList.appendTag(egg.writeToNBT(new NBTTagCompound()));
            }
            contents.setTag(nickname, nbtList);
        });

        nbt.setTag("contents", contents);

        System.out.printf("Wrote NBT: %s\n", nbt);
        return nbt;
    }

    /* ------------------------------- */

    /**
     * Fetches the contents of the pouch owned by given nickname.
     * If the pouch is not created yet, will create and then return it.
     * <b>MAKE SURE YOU DO NOT PUSH DIRECTLY TO THIS LIST.
     * USE {@link EggPouchWorldSavedData#putSpawnEgg(String, ItemStack)} INSTEAD!</b>
     *
     * @param nickname Nickname of pouch owner
     * @return Contents of the pouch
     */
    public List<ItemStack> getPouchContent(String nickname) {
        return pouches.getOrDefault(nickname, createPouchFor(nickname));
    }

    /**
     * Puts given item stack to given user's pouch.
     *
     * @param nickname  Nickname of pouch owner
     * @param itemStack Spawn egg item stack to be put
     * @throws IllegalArgumentException thrown if given item stack is
     *                                  not an item stack of {@link ItemSpawnEggFighter}
     */
    public void putSpawnEgg(String nickname, ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemSpawnEggFighter))
            throw new IllegalArgumentException("Expected ItemSpawnEggFighter, found " + itemStack.getItem().getClass().getSimpleName());

        // Fetch pouch contents
        List<ItemStack> pouchContent = getPouchContent(nickname);

        // Pouch is absent, lazy init it
        if (pouchContent == null)
            pouchContent = createPouchFor(nickname);

        // Append stack to username's pouch
        pouchContent.add(itemStack);

        // Mark dirty, so Minecraft saves it later
        markDirty();
    }

    /**
     * Creates a pouch for given user.
     *
     * @param nickname Nickname of pouch owner
     * @return Created pouch, or existing one if was already there
     */
    public List<ItemStack> createPouchFor(String nickname) {
        // Pouch already exists
        if (pouches.get(nickname) != null)
            return pouches.get(nickname);

        // Create pouch and put into the map
        List<ItemStack> pouchContent = new LinkedList<>();
        pouches.put(nickname, pouchContent);

        // Mark dirty, so Minecraft saves it later
        markDirty();

        return pouchContent;
    }

}
