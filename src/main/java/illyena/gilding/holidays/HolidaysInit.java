package illyena.gilding.holidays;

import illyena.gilding.compat.Mod;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

import static illyena.gilding.GildingInit.GILDING;
import static illyena.gilding.compat.Mod.MODS;

public class HolidaysInit implements ModInitializer {
    public static final String MOD_ID = "holidays";
    public static final String MOD_NAME = "Holidays";
    public static final String VERSION = Mod.getModVersion(MOD_ID);

    public static final Mod HOLIDAY = new Mod(MOD_ID, GILDING, true, null);

    @Override
    public void onInitialize() {

    }

    public static Text translationKeyOf(String type, String key) {
        return new TranslatableText(type + "." + MOD_ID + "." + key);
    }

    /**
     * @return List of all Mods with HolidaysInit.HOLIDAYS as mod.parent
     */
    public static List<Mod> getHolidayMods() {
        List<Mod> mods = new ArrayList<>();
        for (Mod mod : MODS) {
            if (mod.getParentMod() != null && mod.getParentMod().equals(HOLIDAY)) {
                mods.add(mod);
            }
        }
        return mods;
    }

}
