package illyena.gilding.holidays;

import illyena.gilding.compat.Mod;
import illyena.gilding.holidays.client.gui.screen.HolidayMenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import static illyena.gilding.holidays.HolidaysInit.MOD_ID;

@Environment(EnvType.CLIENT)
public class HolidaysClientInit implements ClientModInitializer {
    public static final Screen HOLIDAY_CONFIG_SCREEN = Mod.ModScreens.registerConfigScreen(MOD_ID, new HolidayMenuScreen());

    @Override
    public void onInitializeClient() {

    }

}
