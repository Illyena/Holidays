package illyena.gilding.holidays.client.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import illyena.gilding.compat.Mod;
import illyena.gilding.config.option.ConfigOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.Option;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static illyena.gilding.GildingInit.SUPER_MOD_NAME;
import static illyena.gilding.GildingInit.VERSION;
import static illyena.gilding.holidays.HolidaysInit.MOD_ID;

@Environment(EnvType.CLIENT)
public class HolidaysConfigMenu extends Screen {
    public static final CubeMapRenderer PANORAMA_CUBE_MAP = new CubeMapRenderer(new Identifier("textures/gui/title/background/panorama"));
    private static final Identifier PANORAMA_OVERLAY = new Identifier("textures/gui/title/background/panorama_overlay.png");
    private final boolean isMinceraft;
    private final RotatingCubeMapRenderer backgroundRenderer;

    private final Screen parent;


    protected HolidaysConfigMenu() {
        this(MinecraftClient.getInstance().currentScreen);
    }

    protected HolidaysConfigMenu(Screen parent) {
        super(new TranslatableText("menu." + MOD_ID + "." + MOD_ID + "_config.title"));
        this.backgroundRenderer = new RotatingCubeMapRenderer(PANORAMA_CUBE_MAP);
        this.isMinceraft = (double)(new Random()).nextFloat() < 1.0E-4;
        this.parent = parent;
    }

    public void tick() {  }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    protected void init() {
        int l = this.height / 4 + 48;

        this.initMultiWidgets();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, l + 72 + 12, 98, 20,
                ScreenTexts.BACK, (button) -> this.client.setScreen(this.parent)));

        if (this.client.world != null) {
            this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, l + 72 +12, 98, 20,
                    new TranslatableText("menu.returnToGame"), button -> this.client.setScreen((null))));
        } else {
            this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, l + 72 + 12, 98, 20,
                    new TranslatableText("gui.toTitle"), (button) -> this.client.setScreen(new TitleScreen())));
        }
    }

    private void initMultiWidgets() {
        int i = 0;
        List<Mod> modsList = Mod.getModsWithSubGroups(MOD_ID);
        ArrayList<Option> options = new ArrayList<>();

        for (Mod mod : modsList) {
            if (mod.isLoaded()) {
                for (Field field : mod.getConfigClass().getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && ConfigOption.class.isAssignableFrom(field.getType())) {
                        try {
                            options.add(((ConfigOption<?>) field.get(null)).asOption());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        for (Option option : options) {
            int j = this.width / 2 - 155 + i % 2 * 160;
            int k = this.height / 6 - 12 + 24 * (i >> 1) + 48;
            this.addDrawableChild(option.createButton(this.client.options, j, k, 150));
            ++i;
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
        if ((l & -67108864) != 0) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, g);
            if (this.isMinceraft) {
                this.drawWithOutline(j, 30, (x, y) -> {
                    this.drawTexture(matrices, x + 0, y, 0, 0, 99, 44);
                    this.drawTexture(matrices, x + 99, y, 129, 0, 27, 44);
                    this.drawTexture(matrices, x + 99 + 26, y, 126, 0, 3, 44);
                    this.drawTexture(matrices, x + 99 + 26 + 3, y, 99, 0, 26, 44);
                    this.drawTexture(matrices, x + 155, y, 0, 45, 155, 44);
                });
            } else {
                this.drawWithOutline(j, 30, (x, y) -> {
                    this.drawTexture(matrices, x + 0, y, 0, 0, 155, 44);
                    this.drawTexture(matrices, x + 155, y, 0, 45, 155, 44);
                });
            }
            drawTexture(matrices, j + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);
            String string = SUPER_MOD_NAME + " Mod v: " + VERSION;
            drawStringWithShadow(matrices, this.textRenderer, string, 2, this.height - 10, 16777215 | l);

            for (Element element : this.children()) {
                if (element instanceof ClickableWidget) {
                    ((ClickableWidget) element).setAlpha(g);
                }
            }

            super.render(matrices, mouseX, mouseY, delta);
        }
    }

}
