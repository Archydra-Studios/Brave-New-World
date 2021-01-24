package azzy.fabric.brave_new_world;

import azzy.fabric.brave_new_world.render.BNWCallbacks;
import net.fabricmc.api.ClientModInitializer;

public class BNWClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BNWCallbacks.init();
    }
}
