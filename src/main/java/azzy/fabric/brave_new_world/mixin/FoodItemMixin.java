package azzy.fabric.brave_new_world.mixin;

import azzy.fabric.brave_new_world.api.RotAPI;
import azzy.fabric.brave_new_world.item.BNWItems;
import azzy.fabric.brave_new_world.util.RottableFoodItem;
import azzy.fabric.brave_new_world.util.RottableStack;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;

@Mixin(Item.class)
public abstract class FoodItemMixin implements RottableFoodItem {

    @Shadow @Final private @Nullable FoodComponent foodComponent;

    @Shadow public abstract Item asItem();

    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void cum(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        if(canRot() && world != null) {
            float progress = getRotProgress(((RottableStack) (Object) stack), world);
            tooltip.add(new LiteralText("§o§l" + getRotStage(((RottableStack) (Object) stack), world)));
            tooltip.add(new LiteralText("§7" + ((int) (Math.min(progress, 1) * 100)) + "% spoiled"));
        }
    }

    @Override
    public ItemStack getRotStack(ItemStack stack) {
        if(!canRot())
            return ItemStack.EMPTY;

        if(RotAPI.containsRot(stack.getItem()))
            return new ItemStack(RotAPI.getRotProduct(stack.getItem()), stack.getCount());
        Item item;
        if(foodComponent.isMeat()) {
            item = Items.ROTTEN_FLESH;
        }
        else
            item = BNWItems.MUSH;
        return new ItemStack(item, stack.getCount());
    }

    @Override
    public long getMaxAge() {
        if(RotAPI.containsTime(this.asItem()))
            return RotAPI.getRottingTime(this.asItem());
        if(true)
            return 1000;
        return foodComponent.isMeat() ? 12000 : foodComponent.isSnack() ? 70000 : 48000;
    }

    @Override
    public boolean canRot() {
        if(this.asItem() == Items.HONEY_BOTTLE || this.asItem() == Items.ROTTEN_FLESH)
            return false;
        return RotAPI.containsRot(this.asItem()) || foodComponent != null;
    }

    @Override
    public String getRotStage(RottableStack stack, World world) {
        float progress = getRotProgress(stack, world);
        if(!canRot())
            return "§6Preserved";
        if(progress < 0.1)
            return "§bFresh";
        else if(progress < 0.6)
            return "§aPlain";
        else if(progress < 0.9)
            return "§eStale";
        return "§cRancid";
    }

    @Override
    public float getRotProgress(RottableStack stack, World world) {
        return (float) (world.getTime() - stack.getAge()) / getMaxAge();
    }

    //static {
    //    rotMap.put();
    //}
}