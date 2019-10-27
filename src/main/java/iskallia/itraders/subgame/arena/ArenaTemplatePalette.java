package iskallia.itraders.subgame.arena;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;

public class ArenaTemplatePalette {

    private Map<Integer, IBlockState> palette;
    private int[] data;

    private ArenaTemplatePalette(Map<Integer, IBlockState> palette, int[] data) {
        this.palette = palette;
        this.data = data;
    }

    public static ArenaTemplatePalette fromTemplate(ArenaTemplate template) {
        Map<BlockPos, IBlockState> stateMap = template
                .getPattern()
                .entrySet()
                .stream()
                .map(entry -> new Tuple<>(entry.getKey(), entry.getValue().state))
                .filter(tpl -> !tpl.getSecond().getBlock().equals(Blocks.AIR))
                .collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));

        Map<Integer, IBlockState> palette = generatePalette(stateMap.values());
        Map<IBlockState, Integer> storeMap = palette
                .entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        int[] data = new int[stateMap.size() * 3];
        int ptr = 0;
        for (Map.Entry<BlockPos, IBlockState> entry : stateMap.entrySet()) {
            ptr = serialize(data, ptr, entry.getKey(), storeMap.get(entry.getValue()));
        }
        return new ArenaTemplatePalette(palette, data);
    }

    public ArenaTemplate toTemplate() {
        ArenaTemplate template = new ArenaTemplate();
        for (int i = 0; i < data.length; i += 3) {
            BlockPos pos = BlockPos.fromLong(((long) data[i]) << 32 | data[i + 1]);
            IBlockState state = this.palette.get(data[i + 2]);
            if (state != null) {
                template.addBlock(pos, state);
            }
        }
        return template;
    }

    private static Map<Integer, IBlockState> generatePalette(Collection<IBlockState> values) {
        Set<IBlockState> seenStates = new HashSet<>();
        Map<Integer, IBlockState> palette = new HashMap<>();
        int index = 0;
        for (IBlockState state : values) {
            if (seenStates.contains(state)) {
                continue;
            }
            seenStates.add(state);
            palette.put(index, state);
            index++;
        }
        return palette;
    }

    private static int serialize(int[] out, int index, BlockPos pos, Integer paletteId) {
        long lPos = pos.toLong();
        out[index] = (int) (lPos >> 32);
        out[index + 1] = (int) lPos;
        out[index + 2] = paletteId;
        return index + 3;
    }

}
