package azzy.fabric.brave_new_world.api;

import azzy.fabric.brave_new_world.BNWCommon;
import azzy.fabric.brave_new_world.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class RotAPI {

    private static final Object2ObjectOpenHashMap<Item, String> specialRegistry = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<Item, Item> rotRegistry = new Object2ObjectOpenHashMap<>();
    private static final Object2IntOpenHashMap<Item> ageRegistry = new Object2IntOpenHashMap<>();

    private static final Object2ObjectOpenHashMap<Item, String> specialMap = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<Item, Item> rotMap = new Object2ObjectOpenHashMap<>();
    private static final Object2IntOpenHashMap<Item> ageMap = new Object2IntOpenHashMap<>();

    public static Item getRotProduct(Item item) {
        return rotMap.get(item);
    }

    public static boolean containsRot(Item item) {
        return rotMap.containsKey(item);
    }

    public static int getRottingTime(Item item) {
        return ageMap.getInt(item);
    }

    public static boolean containsTime(Item item) {
        return ageMap.containsKey(item);
    }

    public static void registerRottingProduct(Item input, Item product) {
        rotRegistry.put(input, product);
    }

    public static void registerRottingTime(Item input, int time) {
        ageRegistry.put(input, time);
    }

    public static void load(ResourceManager manager) {
        rotMap.clear();
        ageMap.clear();
        specialMap.clear();

        manager.findResources("bnw/rot_system/item", path -> path.endsWith(".json")).forEach(id -> load(manager, id));

        rotMap.putAll(rotRegistry);
        ageMap.putAll(ageRegistry);
        specialMap.putAll(specialRegistry);
    }

    public static void load(ResourceManager manager, Identifier resourceId) {
        Identifier id = new Identifier(resourceId.getNamespace(), resourceId.getPath().replace(".json", ""));

        try {
            InputStream stream = manager.getResource(resourceId).getInputStream();
            JsonObject rotJson = JsonUtils.fromInputStream(stream);
            if(!rotJson.get("special").getAsBoolean()) {

            } else {
            }
        } catch (Exception e) {
            BNWCommon.BNW_LOG.error("Error found while loading rot system json " + resourceId.toString() + "!", e);
        }
    }

    //static {
//
    //}
}
