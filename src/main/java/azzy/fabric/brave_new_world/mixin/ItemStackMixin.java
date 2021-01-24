package azzy.fabric.brave_new_world.mixin;

import azzy.fabric.brave_new_world.util.RottableFoodItem;
import azzy.fabric.brave_new_world.util.RottableStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements RottableStack {

    @Shadow public @Nullable abstract CompoundTag getTag();

    @Shadow public abstract CompoundTag getOrCreateTag();

    @Shadow @Final @Deprecated private Item item;

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    public void tick(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if(((RottableFoodItem) item).canRot() && world != null) {
            if(!getOrCreateTag().contains("age"))
                getOrCreateTag().putLong("age", world.getTime());
            if(world.getTime() - getAge() > ((RottableFoodItem) item).getMaxAge() && entity instanceof PlayerEntity)
                ((PlayerEntity) entity).inventory.setStack(slot, ((RottableFoodItem) item).getRotStack(ItemStack.class.cast(this)));
        }
    }

    @Override
    public void tickRot(World world, Inventory inventory, int slot) {
        if(((RottableFoodItem) item).canRot() && world != null) {
            if(!getOrCreateTag().contains("age"))
                getOrCreateTag().putLong("age", world.getTime());
            if(world.getTime() - getAge() > ((RottableFoodItem) item).getMaxAge())
                inventory.setStack(slot, ((RottableFoodItem) item).getRotStack(ItemStack.class.cast(this)));
        }
    }

    @Override
    public long getAge() {
        return getTag() != null ? getTag().getLong("age") : 0;
    }

    @Override
    public void setAge(long age) {
        getOrCreateTag().putLong("age", age);
    }
}
