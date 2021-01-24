package azzy.fabric.brave_new_world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.ItemStack;

import java.util.WeakHashMap;

public class RotTracker {

    private static final WeakHashMap<ItemStack, Void> rottableStackTracker = new WeakHashMap<>();

    public static void add(ItemStack stack) {
        rottableStackTracker.put(stack, void);
    }

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {

        });
    }
}
