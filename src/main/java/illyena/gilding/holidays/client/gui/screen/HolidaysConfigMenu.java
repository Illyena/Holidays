package illyena.gilding.holidays.client.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import illyena.gilding.compat.Mod;
import illyena.gilding.config.gui.ConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

import static illyena.gilding.GildingInit.SUPER_MOD_NAME;
import static illyena.gilding.GildingInit.VERSION;
import static illyena.gilding.holidays.HolidaysInit.MOD_ID;

@Environment(EnvType.CLIENT)
public class HolidaysConfigMenu extends ConfigScreen {
    public static final CubeMapRenderer PANORAMA_CUBE_MAP = new CubeMapRenderer(new Identifier("textures/gui/title/background/panorama"));
    private static final Identifier PANORAMA_OVERLAY = new Identifier("textures/gui/title/background/panorama_overlay.png");
    private final RotatingCubeMapRenderer backgroundRenderer;

    public HolidaysConfigMenu() { this(MinecraftClient.getInstance().currentScreen); }

    public HolidaysConfigMenu(Screen parent) {
        super(MOD_ID, parent);
        this.backgroundRenderer = new RotatingCubeMapRenderer(PANORAMA_CUBE_MAP);
    }

    protected void init() {
        this.initSync();

        int l = this.height / 4 + 48;
        this.initMultiWidgets();
        this.initBackWidget(l);
        this.initReturnWidget(l);
    }

    private void initMultiWidgets() {
        List<Mod> modsList = Mod.getModsWithSubGroups(MOD_ID);
        for (Mod mod : modsList) {
            if (mod.isLoaded()) {
                this.initMultiWidgets(mod.getModId(), false);
            }
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float f = 1.0f;
        this.backgroundRenderer.render(delta, MathHelper.clamp(f, 0.0F, 1.0F));
        int j = this.width / 2 - 137;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, PANORAMA_OVERLAY);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        drawTexture(matrices, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
        float g = 1.0f;
        int l = MathHelper.ceil(g * 255.0F) << 24;

        drawTexture(matrices, j + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);
        String string = SUPER_MOD_NAME + " Mod v: " + VERSION;
        drawStringWithShadow(matrices, this.textRenderer, string, 2, this.height - 10, 16777215 | l);

        super.render(matrices, mouseX, mouseY, delta);
    }

}
