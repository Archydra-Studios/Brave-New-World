package azzy.fabric.brave_new_world.item;

import azzy.fabric.brave_new_world.BNWCommon;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@SuppressWarnings("unused")
public class BNWItems {

    public static void init() {}

    public static FabricItemSettings defaultItem() {
        return new FabricItemSettings().group(BNWCommon.BNW_ITEMS);
    }

    public static final Item COMPOST = register("compost", new BoneMealItem(defaultItem()));
    public static final Item MUSH = register("mush", new Item(defaultItem()));
    public static final Item YEAST = register("yeast", new Item(defaultItem()));
    public static final Item MOLD = register("mold", new Item(defaultItem()));
    public static final Item HUMMUS = register("hummus", new Item(defaultItem()));
    public static final Item SPOILED_MILK = register("spoiled_milk", new SpoiledMilkBucketItem(defaultItem()));

    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(BNWCommon.MOD_ID, name), item);
    }
}
