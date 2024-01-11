package illyena.gilding.holidays.client.gui.screen;

import illyena.gilding.compat.Mod;
import illyena.gilding.config.gui.widget.ModButtonWidget;
import illyena.gilding.core.client.gui.screen.SharedBackground;
import illyena.gilding.core.util.time.GildingCalendar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import static illyena.gilding.core.client.gui.screen.GildingMenuScreen.MOD_INACTIVE_TOOLTIP;
import static illyena.gilding.holidays.HolidaysInit.MOD_ID;
import static illyena.gilding.holidays.HolidaysInit.translationKeyOf;

public class HolidayMenuScreen extends Screen implements SharedBackground {
    private static final Text TITLE = translationKeyOf("menu", "title");
    private static final Text CONFIG_BUTTON = translationKeyOf("menu", "config.button");

    private static final CubeMapRenderer PANORAMA_CUBE_MAP = new CubeMapRenderer(new Identifier("textures/gui/title/background/panorama"));
    private final RotatingCubeMapRenderer backgroundRenderer;
    private final boolean doBackgroundFade;
    private long backgroundFadeStart;
    private final Screen parent;

    public HolidayMenuScreen() { this(MinecraftClient.getInstance().currentScreen); }

    public HolidayMenuScreen(Screen parent) {
        super(TITLE);
        if (parent instanceof SharedBackground previous) {
            this.backgroundRenderer = previous.getBackgroundRenderer();
            this.doBackgroundFade = false;
        } else {
            this.backgroundRenderer = new RotatingCubeMapRenderer(PANORAMA_CUBE_MAP);
            this.doBackgroundFade = true;
        }
        this.parent = parent;
    }

    protected void init() {
        int l = this.height / 4 + 48;

        this.addDrawableChild(new ButtonWidget( this.width / 2 - 100, this.height / 6 , 200, 20,
                CONFIG_BUTTON, button -> this.client.setScreen(new HolidaysConfigMenu(this))));

        this.initMultiWidgets();

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, l + 72 + 12, 98, 20,
                ScreenTexts.BACK, button -> this.client.setScreen(this.parent)));
        if (this.client.world != null) {
            this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, l + 72 +12, 98, 20,
                    new TranslatableText("menu.returnToGame"), button -> this.client.setScreen((null))));
        } else {
            this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, l + 72 + 12, 98, 20,
                    new TranslatableText("gui.toTitle"), button -> this.client.setScreen(new TitleScreen())));
        }
    }

    private void initMultiWidgets() {
        int i = 0;
        List<Mod> modList = Mod.getModsSansSubGroups(MOD_ID);

        for (Mod mod : modList) {
            int j = this.width / 2 - 155 + i % 2 * 160;
            int k = this.height / 6 - 12 + 24 * (i  >> 1) + 48;
            this.addDrawableChild(this.createButton(mod, j, k, 150, 20));
            ++i;
        }
    }

    private ButtonWidget createButton(Mod mod, int x, int y, int width, int height ) {
        Text text = new TranslatableText("menu." + mod.getModId() + ".config.button");
        ButtonWidget.TooltipSupplier tooltipSupplier = new ButtonWidget.TooltipSupplier() {
            @Override
            public void onTooltip(ButtonWidget button, MatrixStack matrices, int mouseX, int mouseY) {
                if (button.active) {
                    HolidayMenuScreen.this.renderTooltip(matrices, MOD_INACTIVE_TOOLTIP, mouseX, mouseY);
                }
            }
            public void supply(Consumer<Text> consumer) { consumer.accept(MOD_INACTIVE_TOOLTIP); }
        };

        return new ModButtonWidget(mod, x, y, width, height, text, button -> {
            if (mod.isLoaded()) {
                this.client.setScreen(Mod.ModScreens.getScreen(mod.getModId(), this));
            }
        }, mod.isLoaded() ? ButtonWidget.EMPTY : tooltipSupplier);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.backgroundFadeStart == 0L && this.doBackgroundFade) {
            this.backgroundFadeStart = Util.getMeasuringTimeMs();
        }

        float f = this.doBackgroundFade ? (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        this.backgroundRenderer.render(delta, MathHelper.clamp(f, 0.0F, 1.0F));

        float g = this.doBackgroundFade ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
        int l = MathHelper.ceil(g * 255.0F) << 24;
        if ((l & -67108864) != 0) {
            drawCenteredText(matrices, this.textRenderer, new LiteralText("HAPPY " + GildingCalendar.checkHolidays().name() + "!"), this.width / 2, this.height / 8, Color.CYAN.getRGB());
            int m = this.textRenderer.getWidth(GildingCalendar.getDateLong()) / 2;
            drawStringWithShadow(matrices, this.textRenderer, GildingCalendar.getDateLong(), this.width / 2 - m, this.height - 10, 16777215 | l);

            for (Element element : this.children()) {
                if (element instanceof ClickableWidget) {
                    ((ClickableWidget) element).setAlpha(g);
                }
            }

            super.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    public RotatingCubeMapRenderer getBackgroundRenderer() { return this.backgroundRenderer; }

}
