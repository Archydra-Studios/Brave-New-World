package azzy.fabric.brave_new_world;

import azzy.fabric.brave_new_world.components.TemperatureComponent;
import azzy.fabric.brave_new_world.components.TemperatureComponentImpl;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

import static azzy.fabric.brave_new_world.BNWCommon.MOD_ID;

public class BNWComponents implements EntityComponentInitializer {

    public static final ComponentKey<TemperatureComponent> TEMPERATURE_KEY = ComponentRegistry.getOrCreate(new Identifier(MOD_ID, "temperature"), TemperatureComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TEMPERATURE_KEY, playerEntity -> new TemperatureComponentImpl(), RespawnCopyStrategy.NEVER_COPY);
    }
}
