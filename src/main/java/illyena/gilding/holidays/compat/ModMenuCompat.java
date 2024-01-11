package illyena.gilding.holidays.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import illyena.gilding.holidays.client.gui.screen.HolidaysConfigMenu;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() { return HolidaysConfigMenu::new; }

}
