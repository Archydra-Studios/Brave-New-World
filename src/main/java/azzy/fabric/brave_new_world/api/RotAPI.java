package azzy.fabric.brave_new_world.api;

import azzy.fabric.brave_new_world.BNWCommon;
import azzy.fabric.brave_new_world.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.IntRange;
import net.minecraft.util.registry.Registry;

import java.io.InputStream;

public class RotAPI {

    private static final Object2ObjectOpenHashMap<Item, SpecialFoodData> specialMap = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<Item, Item> rotMap = new Object2ObjectOpenHashMap<>();
    private static final Object2IntOpenHashMap<Item> ageMap = new Object2IntOpenHashMap<>();
    private static final Object2IntOpenHashMap<Item> nomMap = new Object2IntOpenHashMap<>();

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

    public static void load(ResourceManager manager) {
        rotMap.clear();
        ageMap.clear();
        nomMap.clear();
        specialMap.clear();
        manager.findResources("bnw/rot_system/item", path -> path.endsWith(".json")).forEach(id -> load(manager, id));
    }

    public static void load(ResourceManager manager, Identifier resourceId) {
        try {
            InputStream stream = manager.getResource(resourceId).getInputStream();
            JsonObject rotJson = JsonUtils.fromInputStream(stream);
            if(!rotJson.get("special").getAsBoolean()) {

            } else {
            }
        } catch (Exception e) {
            BNWCommon.LOG.error("Error found while loading rot system json " + resourceId.toString() + "!", e);
        }
    }

    public static class SpecialFoodData {
        public final int consumeTime;
        public final IntRange rotTempRange;
        public final int burnTemp;
        public final Item burnResult;

        public SpecialFoodData(int consumeTime, IntRange rotTempRange, int burnTemp, Item burnResult ) {
            this.consumeTime = consumeTime;
            this.rotTempRange = rotTempRange;
            this.burnTemp = burnTemp;
            this.burnResult = burnResult;
        }

        public SpecialFoodData from(JsonObject foodDataObject) {
            JsonElement cont = foodDataObject.get("consume_time");
            JsonElement mint = foodDataObject.get("min_rot_temp");
            JsonElement maxt = foodDataObject.get("max_rot_temp");
            JsonElement burnt = foodDataObject.get("rot_temp");
            Item result = null;
            if(foodDataObject.has("burn_result"))
                result = Registry.ITEM.get(Identifier.tryParse(foodDataObject.get("burn_result").getAsString()));

            IntRange range = null;
            if(mint != null && maxt != null)
                range = new IntRange(mint.getAsInt(), maxt.getAsInt());
            return new SpecialFoodData(
                    cont != null ? cont.getAsInt() : -1,
                    range,
                    burnt != null ? burnt.getAsInt() : -1,
                    result
            );
        }

        public boolean canBurn() {
            return burnResult != null;
        }

        public boolean hasSpecialConsumeTime() {
            return consumeTime > 0;
        }
    }
}
