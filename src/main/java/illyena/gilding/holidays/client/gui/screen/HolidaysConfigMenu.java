package illyena.gilding.holidays.client.gui.screen;

import illyena.gilding.compat.Mod;
import illyena.gilding.config.gui.ConfigScreen;
import illyena.gilding.config.option.ConfigOption;
import illyena.gilding.core.client.gui.screen.SharedBackground;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static illyena.gilding.holidays.HolidaysInit.*;

@Environment(EnvType.CLIENT)
public class HolidaysConfigMenu extends ConfigScreen implements SharedBackground {
    private static final Text TITLE = translationKeyOf("menu", "config.title");
    private static final CubeMapRenderer PANORAMA_CUBE_MAP = new CubeMapRenderer(new Identifier("textures/gui/title/background/panorama"));
    private final RotatingCubeMapRenderer backgroundRenderer;


    public HolidaysConfigMenu() { this(MinecraftClient.getInstance().currentScreen); }

    public HolidaysConfigMenu(Screen parent) {
        super(MOD_ID, parent, TITLE);
        if (parent instanceof SharedBackground previous) {
            this.backgroundRenderer = previous.getBackgroundRenderer();
            this.doBackgroundFade = false;
        } else {
            this.backgroundRenderer = new RotatingCubeMapRenderer(PANORAMA_CUBE_MAP);
            this.doBackgroundFade = true;
        }
    }

    protected void init() {
        this.initSync();
        int l = this.height / 4 + 48;

        this.initMultiWidgets();

        this.initBackWidget(l);
        this.initReturnWidget(l);
    }

    private void initMultiWidgets() {
        int i = 0;
        List<Mod> modsList = Mod.getModsWithSubGroups(MOD_ID);

        for (Mod mod : modsList) {
            if (mod.isLoaded()) {
                for (ConfigOption<?> config : getConfigs(mod.getModId())) {
                    int j = this.width / 2 - 155 + i % 2 * 160;
                    int k = this.height / 6 - 12 + 24 * (i >> 1) + 48;
                    ClickableWidget drawable;
                    if (!this.inactivateButton(config)) {
                        drawable = this.addDrawableChild(config.createButton(j, k, 150));
                    } else {
                        drawable = this.addDrawableChild(this.createDeadButton(config, j, k, 150));
                    }
                    this.map. put(drawable, config);
                    ++i;
                }
            }
        }
    }

    @Override
    protected void renderText(MatrixStack matrices, int mouseX, int mouseY, float delta, float alpha, int time) {
        List<String> modNames = new ArrayList<>();
        getHolidayMods().forEach(mod -> FabricLoader.getInstance().getModContainer(mod.getModId()).ifPresent(c -> modNames.add(c.getMetadata().getName())));
        String string = MOD_NAME + ": " + Mod.getModVersion(MOD_ID) + " {includes: " + modNames + "}";
        int width = this.textRenderer.getWidth(string);
        drawStringWithShadow(matrices, this.textRenderer, string, this.width - width - 2, this.height - 10, 16777215 | time);

    }

    @Override
    public RotatingCubeMapRenderer getBackgroundRenderer() { return this.backgroundRenderer; }

}
