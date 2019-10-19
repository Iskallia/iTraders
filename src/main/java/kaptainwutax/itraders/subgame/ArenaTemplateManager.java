package kaptainwutax.itraders.subgame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.util.JsonBlockStateAdapter;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class: ArenaTemplateManager
 * Created by HellFirePvP
 * Date: 19.10.2019 / 12:54
 */
public class ArenaTemplateManager {

    private static final String ROOT_PATH = "subgame/arena";
    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(IBlockState.class, new JsonBlockStateAdapter())
            .create();

    private static Map<UUID, ArenaTemplate> loadedTemplates = new HashMap<>();

    @Nonnull
    public static ArenaTemplate loadUserTemplate(UUID plUUID) {
        return loadedTemplates.computeIfAbsent(plUUID, uuid -> loadTemplate(uuid.toString() + ".tpl"));
    }

    //Allows for modification of the base template.
    private static ArenaTemplate loadDefaultTemplate() {
        File templateFile = new File(getTemplateDirectory(), "_default.tpl");
        if (!templateFile.exists()) {
            writeTemplate(DefaultArenaTemplate.INSTANCE, "_default.tpl");
        }
        return loadTemplate("_default.tpl");
    }

    @Nonnull
    private static ArenaTemplate loadTemplate(String fileName) {
        File templateFile = new File(getTemplateDirectory(), fileName);
        if (!templateFile.exists()) {
            ArenaTemplate defaultTemplate = loadDefaultTemplate();
            writeTemplate(defaultTemplate, fileName);
            return defaultTemplate;
        }
        try (FileReader reader = new FileReader(templateFile)) {
            return GSON.fromJson(reader, ArenaTemplatePalette.class).toTemplate();
        } catch (IOException e) {
            Traders.LOG.warn("Failed to load template " + fileName, e);
            if (fileName.equals("_default.tpl")) {
                Traders.LOG.warn("Failed to load default template! Using builtin as fallback!", e);
                return DefaultArenaTemplate.INSTANCE;
            } else {
                return loadDefaultTemplate();
            }
        }
    }

    private static void writeTemplate(ArenaTemplate tpl, String fileName) {
        File templateFile = new File(getTemplateDirectory(), fileName);
        try (FileWriter writer = new FileWriter(templateFile)) {
            GSON.toJson(ArenaTemplatePalette.fromTemplate(tpl), writer);
            writer.flush();
        } catch (IOException e) {
            Traders.LOG.warn("Failed to write template " + fileName, e);
        }
    }

    private static File getTemplateDirectory() {
        File dir = new File(ROOT_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

}
