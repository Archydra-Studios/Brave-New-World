package azzy.fabric.brave_new_world.render;

import azzy.fabric.brave_new_world.BNWCommon;
import azzy.fabric.brave_new_world.BNWComponents;
import azzy.fabric.brave_new_world.components.TemperatureComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static org.lwjgl.opengl.GL46.*;

public class BNWHudRenderer{
    private static final Identifier NEUTRAL_TEMP_TEX = new Identifier(BNWCommon.MOD_ID, "textures/gui/hud/temp_meter_atlas.png");
    
    private enum Texture{
        ORB_NORMAL(0, 0, 16, 16),
        ORB_COLD(16, 0, 16, 16),
        ORB_FREEZING(32, 0, 16, 16),
        ORB_HOT(48, 0, 16, 16),
        ORB_BURNING(64, 0, 16, 16),
        
        ARROW_HOT(4, 19, 8, 9),
        ARROW_COLD(20, 19, 8, 9);
        
        private final int u;
        private final int v;
        private final int w;
        private final int h;
        
        Texture(int u, int v, int w, int h){
            this.u = u;
            this.v = v;
            this.w = w;
            this.h = h;
        }
    }
    
    private enum TempBucket{
        FREEZING(Double.MIN_VALUE, 5, true, Texture.ORB_FREEZING),
        COLD(5, 20, false, Texture.ORB_COLD),
        NORMAL(20, 34, false, Texture.ORB_NORMAL),
        HOT(34, 49, false, Texture.ORB_HOT),
        BURNING(49, Double.MAX_VALUE, true, Texture.ORB_BURNING);
    
        private final double min;
        private final double max;
        private final boolean critical;
        private final Texture texture;
    
        TempBucket(double min, double max, boolean critical, Texture texture){
            this.min = min;
            this.max = max;
            this.critical = critical;
            this.texture = texture;
        }
        
        public static TempBucket getBucket(double temperature){
            for(TempBucket value : values()){
                if(temperature >= value.min && temperature < value.max){
                    return value;
                }
            }
            if(temperature < 0){
                return FREEZING;
            }else{
                return BURNING;
            }
        }
    }
    
    private static Texture transitionTexture = null;
    private static float transitionProgress = 0;
    private static float transitionSign = 0;
    
    public static void render(MatrixStack matrices, float tickDelta){
        final MinecraftClient client = MinecraftClient.getInstance();
        final ClientPlayerEntity player = client.player;
        final TemperatureComponent temperatureComponent = BNWComponents.TEMPERATURE_KEY.get(player);
        if(!player.abilities.invulnerable) {
            final int screenWidth = client.getWindow().getScaledWidth();
            final int screenHeight = client.getWindow().getScaledHeight();
        
            final int screenMidY = screenHeight >> 1;
            final int screenMidX = screenWidth >> 1;
        
            RenderSystem.pushMatrix();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    
            TextureManager textureManager = client.getTextureManager();
            textureManager.bindTexture(NEUTRAL_TEMP_TEX);
            double temperature = temperatureComponent.getTemperature();
    
            TempBucket bucket = TempBucket.getBucket(temperature);
            TempBucket lastBucket = TempBucket.getBucket(temperatureComponent.getLastTemperature());
            Texture texture = bucket.texture;
            
            double offsetX = 0;
            if(bucket.critical){
                offsetX = Math.sin(player.age + tickDelta);
            }
        
            drawTextureCentered(offsetX + screenMidX, screenHeight - 46 + 8, -100, texture);
    
            if(bucket != lastBucket){
                 if(bucket.ordinal() < lastBucket.ordinal()){
                    transitionTexture = Texture.ARROW_HOT;
                    transitionSign = -1;
                }else{
                    transitionTexture = Texture.ARROW_COLD;
                    transitionSign = 1;
                }
                transitionProgress = 0;
            }
            if(transitionTexture != null){
                drawTextureCentered(offsetX + screenMidX, screenHeight - 46 - 16 + (16 - Math.sin(transitionProgress * transitionSign / 40) * 32), -99, transitionTexture);
                
                transitionProgress += tickDelta / 10;
                if(transitionProgress > 20){
                    transitionTexture = null;
                }
            }
    
            RenderSystem.popMatrix();
        }
    }
    
    private static void drawTextureCentered(double x, double y, double z, Texture texture){
        drawTexture(x - (texture.w >> 1), y - (texture.h >> 1), z, texture.w, texture.h, texture.u, texture.v);
    }
    
    private static void drawTexture(double x, double y, double z, int width, int height, int u, int v){
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        
        float uvScale = 0.0078125f; // 1 / 128
        
        bufferBuilder.begin(GL_QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(x, y + height, z)
            .texture(u * uvScale, (height + v) * uvScale)
            .color(255, 255, 255, 255).next();
        bufferBuilder.vertex(x + width, y + height, z)
            .texture((width + u) * uvScale, (height + v) * uvScale)
            .color(255, 255, 255, 255).next();
        bufferBuilder.vertex(x + width, y, z)
            .texture((width + u) * uvScale, v * uvScale)
            .color(255, 255, 255, 255).next();
        bufferBuilder.vertex(x, y, z)
            .texture(u * uvScale, v * uvScale)
            .color(255, 255, 255, 255).next();
        tessellator.draw();
    }
}
