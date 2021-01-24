package azzy.fabric.brave_new_world.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.server.network.ServerPlayerEntity;

public interface TemperatureComponent extends Component {
    double getTemperature();
    double getLastTemperature();

    void setTemperature(double temperature);
    void tick(ServerPlayerEntity entity);
    void respawnTick(ServerPlayerEntity entity);
    void markRefresh();
}
