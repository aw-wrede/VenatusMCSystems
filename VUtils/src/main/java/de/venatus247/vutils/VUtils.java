package de.venatus247.vutils;

import com.github.yannicklamprecht.worldborder.api.WorldBorderApi;
import de.venatus247.vutils.utils.file.VUtilsConfig;
import de.venatus247.vutils.utils.handlers.InventoryGuiHandler;
import de.venatus247.vutils.utils.handlers.player.PlayerQuitHandler;
import de.venatus247.vutils.utils.handlers.timer.PlayTimerHandler;
import de.venatus247.vutils.utils.handlers.timer.TimerStringFormat;
import de.venatus247.vutils.utils.handlers.timer.TimerStringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import javax.management.InstanceAlreadyExistsException;
import java.util.Locale;

public class VUtils {

    public static final String prefix = "§7[§5VUtils§7] ";

    private static VUtils instance = null;
    public static VUtils getInstance() {
        return instance;
    }

    private final Main main;

    private final ConsoleCommandSender console;

    private final VUtilsConfig configFile;
    private final InventoryGuiHandler inventoryGuiHandler;

    private final PlayTimerHandler timerHandler;
    private final PlayerQuitHandler playerQuitHandler;

    private WorldBorderApi worldBorderApi;

    public VUtils(Main main) throws Exception {
        if(instance != null)
            throw new InstanceAlreadyExistsException("VUtils already instantiated");

        instance = this;

        this.main = main;
        this.console = main.getServer().getConsoleSender();

        configFile = new VUtilsConfig();

        inventoryGuiHandler = new InventoryGuiHandler();

        playerQuitHandler = new PlayerQuitHandler();

        timerHandler = new PlayTimerHandler(TimerStringFormatter.getFromFormat(configFile.getTimerStyle()), configFile.getTimerTime());

        registerHandlersInPluginManager(Bukkit.getServer().getPluginManager());

        // Setup world border API
        RegisteredServiceProvider<WorldBorderApi> worldBorderApiRegisteredServiceProvider = this.main.getServer().getServicesManager().getRegistration(WorldBorderApi.class);
        if(worldBorderApiRegisteredServiceProvider == null) {
            Logger.getInstance().error(prefix + "WorldBorderApi not found!");
            this.main.getServer().getPluginManager().disablePlugin(this.main);

            return;
        }

        worldBorderApi = worldBorderApiRegisteredServiceProvider.getProvider();
    }

    private void registerHandlersInPluginManager(PluginManager pluginManager) {
        pluginManager.registerEvents(playerQuitHandler, main);
    }

    public Main getMain() {
        return main;
    }

    public ConsoleCommandSender getConsole() {
        return console;
    }

    public VUtilsConfig getConfigFile() {
        return configFile;
    }

    public InventoryGuiHandler getInventoryGuiHandler() {
        return inventoryGuiHandler;
    }

    public PlayTimerHandler getTimerHandler() {
        return timerHandler;
    }

    public PlayerQuitHandler getPlayerQuitHandler() {
        return playerQuitHandler;
    }

    public void setTimerFormat(TimerStringFormat format) {
        timerHandler.setTimerStringFormatter(TimerStringFormatter.getFromFormat(format));
        configFile.setTimerFormat(format);
    }


    private void disablePlugin() {
        getConsole().sendMessage(prefix + "§7Disabling VUtils due to an error!");
        Bukkit.getPluginManager().disablePlugin(main);
    }

    public WorldBorderApi getWorldBorderApi() {
        return worldBorderApi;
    }
}
