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
public class ServerPlayerTickMixin {

    private final ServerPlayerEntity player = ServerPlayerEntity.class.cast(this);
    private final TemperatureComponent temperatureComponent = BNWComponents.TEMPERATURE_KEY.get(player);

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        temperatureComponent.tick(player);
        BNWComponents.TEMPERATURE_KEY.sync(player);
    }

    @Inject(method = "moveToSpawn", at = @At("TAIL"))
    public void spawn(CallbackInfo ci) {
        temperatureComponent.respawnTick(player);
        BNWComponents.TEMPERATURE_KEY.sync(player);
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    public void death(CallbackInfo ci) {
        temperatureComponent.markRefresh();
        BNWComponents.TEMPERATURE_KEY.sync(player);
    }
}
