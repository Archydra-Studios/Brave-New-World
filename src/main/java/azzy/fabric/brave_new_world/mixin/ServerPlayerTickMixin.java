package azzy.fabric.brave_new_world.mixin;

import azzy.fabric.brave_new_world.BNWComponents;
import azzy.fabric.brave_new_world.components.TemperatureComponent;
import azzy.fabric.brave_new_world.components.TemperatureComponentImpl;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerStateMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        TemperatureComponent component = BNWComponents.TEMPERATURE_KEY.get(this);
        component.tick(ServerPlayerEntity.class.cast(this));
    }
}
