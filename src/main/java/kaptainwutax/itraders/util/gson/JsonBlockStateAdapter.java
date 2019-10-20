package kaptainwutax.itraders.util.gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Optional;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Class: JsonBlockStateAdapter
 * Created by HellFirePvP
 * Date: 19.10.2019 / 13:01
 */
public class JsonBlockStateAdapter extends TypeAdapter<IBlockState> {

    @Override
    public void write(JsonWriter out, IBlockState value) throws IOException {
        ResourceLocation key = value.getBlock().getRegistryName();
        List<String> propertyList = new ArrayList<>();
        for (IProperty prop : value.getPropertyKeys()) {
            String val = prop.getName(value.getValue(prop));
            propertyList.add(String.format("%s=%s", prop.getName(), val));
        }
        String serialized = key.toString();
        if (!propertyList.isEmpty()) {
            serialized += String.format("[%s]", String.join(";", propertyList));
        }
        out.value(serialized);
    }

    @Override
    public IBlockState read(JsonReader in) throws IOException {
        String serialized = in.nextString();
        int propIndex = serialized.indexOf('[');
        if (propIndex != -1) {
            String blockKey = serialized.substring(0, propIndex);
            IBlockState state = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockKey)).getDefaultState();
            String[] propertyArray = serialized.substring(propIndex + 1, serialized.length() - 1).split(";");
            for (String propertyStr : propertyArray) {
                state = applyProperty(state, propertyStr);
            }
            return state;
        } else {
            return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(serialized)).getDefaultState();
        }
    }

    private static <T extends Comparable<T>> IBlockState applyProperty(IBlockState state, String propertyStr) {
        Collection<IProperty<?>> properties = state.getPropertyKeys();
        String property = propertyStr.substring(0, propertyStr.indexOf('='));
        String value = propertyStr.substring(property.length() + 1);

        IProperty<T> prop = (IProperty<T>) MiscUtils.iterativeSearch(properties, pr -> pr.getName().equals(property));
        if (prop != null) {
            Optional<T> optValue = prop.parseValue(value);
            if (optValue.isPresent()) {
                state = state.withProperty(prop, optValue.get());
            }
        }
        return state;
    }
}
