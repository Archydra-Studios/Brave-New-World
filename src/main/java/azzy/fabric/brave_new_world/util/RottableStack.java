package azzy.fabric.brave_new_world.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface RottableStack {

    void tickRot(World world, Inventory inventory, int slot);
    void setAge(long age);

    long getAge();
}
