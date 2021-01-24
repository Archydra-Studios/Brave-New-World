package azzy.fabric.brave_new_world.components;

import azzy.fabric.brave_new_world.util.HeatHelper;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class TemperatureComponentImpl implements TemperatureComponent, AutoSyncedComponent {

    private double temperature = 32;

    @Override
    public double getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public void readFromNbt(CompoundTag compoundTag) {
        temperature = compoundTag.getDouble("temperature");
    }

    @Override
    public void writeToNbt(CompoundTag compoundTag) {
        compoundTag.putDouble("temperature", temperature);
    }


    public void tick(ServerPlayerEntity entity) {
        ServerWorld world = (ServerWorld) entity.world;
        BlockPos pos = entity.getBlockPos();
        //if(world.getBiome(entity.getBlockPos()).getCategory() == Biome.Category.DESERT)
            //entity.damage(DamageSource.OUT_OF_WORLD, 1F);
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 10; i++) {
                temperature += HeatHelper.playerAmbientHeat(entity.isSubmergedInWater() ? HeatHelper.HeatMaterial.WATER : HeatHelper.HeatMaterial.AIR, temperature, world.getBiome(pos), !world.isDay(), world.isRaining(), pos.getY());
            }
            temperature += (0.02 - ((pos.getY() - (world.getSeaLevel())) / 10000.0));
        }
        if(world.getTime() % 20 == 0) {
            entity.sendMessage(new LiteralText("" + world.getTime() / 20), false);
            entity.sendMessage(new LiteralText("Temp: " + temperature), false);
            entity.sendMessage(new LiteralText("Biome Temp: " + HeatHelper.translateBiomeHeat(entity.world.getBiome(entity.getBlockPos()), !world.isDay(), world.isRaining(), pos.getY())), false);
        }
    }

    @Override
    public void respawnTick(ServerPlayerEntity entity) {
        temperature = 32;
    }

    @Override
    public void markRefresh() {
    }
}
