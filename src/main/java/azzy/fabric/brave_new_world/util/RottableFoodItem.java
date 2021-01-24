package azzy.fabric.brave_new_world.util;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface RottableFoodItem {

    ItemStack getRotStack(ItemStack stack);

    long getMaxAge();

    boolean canRot();

    String getRotStage(RottableStack stack, World world);

    float getRotProgress(RottableStack stack, World world);
}
