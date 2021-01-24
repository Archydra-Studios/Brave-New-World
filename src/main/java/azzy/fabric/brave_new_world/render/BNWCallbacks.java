package azzy.fabric.brave_new_world.render;

import azzy.fabric.brave_new_world.BNWCommon;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.util.Identifier;

public class BNWCallbacks extends DrawableHelper implements HudRenderCallback {

    public static final Identifier NEUTRAL_TEMP_TEX = new Identifier(BNWCommon.MOD_ID, "textures/gui/hud/temp_meter_neutral.png");

    public static void init() {
        HudRenderCallback.EVENT.register(new BNWCallbacks());
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {



            //client.getTextureManager().bindTexture(NEUTRAL_TEMP_TEX);
            //int x = centerX;
            //client.textRenderer.draw(matrixStack, "AAAAAAAAAAAAAAAAAAAA", x - 8, (float)14, 8453920);
    }
}
