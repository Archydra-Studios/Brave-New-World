package azzy.fabric.brave_new_world.mixin;

import azzy.fabric.brave_new_world.BNWCommon;
import azzy.fabric.brave_new_world.BNWComponents;
import azzy.fabric.brave_new_world.components.TemperatureComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudRenderMixin {

    private static final Identifier NEUTRAL_TEMP_TEX = new Identifier(BNWCommon.MOD_ID, "textures/gui/hud/temp_meter_atlas.png");

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {

        final MinecraftClient client = MinecraftClient.getInstance();
        final ClientPlayerEntity player = client.player;
        final TemperatureComponent temperatureComponent = BNWComponents.TEMPERATURE_KEY.get(player);
        if(!player.isCreative() && !player.isSpectator()) {
            final int screenWidth = client.getWindow().getScaledWidth();
            final int screenHeight = client.getWindow().getScaledHeight();

            final int screenMidY = screenHeight / 2, screenMidX = screenWidth / 2;
            final int height = 16, width = 16;


            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();

            RenderSystem.pushMatrix();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            client.getTextureManager().bindTexture(NEUTRAL_TEMP_TEX);
            float f = 128.0F;
            float vStart = 0;
            float uStart = 0;
            double  temperature = temperatureComponent.getTemperature();
            boolean shake = temperature < 0 || temperature > 54;

            if(temperature > 49)
                uStart = 64;
            else if(temperature > 34)
                uStart = 48;
            else if(temperature < 5)
                uStart = 32;
            else if(temperature < 20)
                uStart = 16;

            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(0.0D + screenMidX - 8, height + (screenHeight - 46), -100).texture(uStart / f, (height + vStart) / f).color(255, 255, 255, 255).next();
            bufferBuilder.vertex((double)width + screenMidX - 8, height + (screenHeight - 46), -100).texture((width + uStart) / f, (height + vStart) / f).color(255, 255, 255, 255).next();
            bufferBuilder.vertex((double)width + screenMidX - 8, 0.0D + (screenHeight - 46), -100).texture((width + uStart) / f, vStart / f).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(0.0D + screenMidX - 8, 0.0D + (screenHeight - 46), -100).texture(uStart / f, vStart / f).color(255, 255, 255, 255).next();
            tessellator.draw();

            RenderSystem.popMatrix();
        }
    }
}
