package azzy.fabric.brave_new_world;

import azzy.fabric.brave_new_world.util.RottableFoodItem;
import azzy.fabric.brave_new_world.util.RottableStack;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class BNWWorldEvents {

    private static final ConcurrentHashMap<Class<? extends BlockEntity>, Integer> blockEntityInvMap = new ConcurrentHashMap<>();

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
            if(serverWorld.getTime() % 4 == 0) {
                for (BlockEntity blockEntity : serverWorld.blockEntities) {
                    if(blockEntity instanceof Inventory) {
                        Class<? extends BlockEntity> clazz = blockEntity.getClass();
                        if(blockEntityInvMap.containsKey(clazz)) {
                            for(int slot = 0; slot < blockEntityInvMap.get(clazz); slot++) {
                                ItemStack stack = ((Inventory) blockEntity).getStack(slot);
                                if(((RottableFoodItem) stack.getItem()).canRot()) {
                                    ((RottableStack) (Object) stack).tickRot(serverWorld, (Inventory) blockEntity, slot);
                                }
                            }
                            continue;
                        }
                        int slot = 0;
                        try {
                            while (true) {
                                ItemStack stack = ((Inventory) blockEntity).getStack(slot);
                                if(((RottableFoodItem) stack.getItem()).canRot()) {
                                    ((RottableStack) (Object) stack).tickRot(serverWorld, (Inventory) blockEntity, slot);
                                }
                                slot++;
                            }
                        } catch (Exception e) {
                            blockEntityInvMap.put(clazz, slot);
                        }
                    }
                }
            }
        });
    }
}
