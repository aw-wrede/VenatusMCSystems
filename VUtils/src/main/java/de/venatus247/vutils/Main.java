package de.venatus247.vutils;

import de.venatus247.vutils.commands.TimerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.management.InstanceAlreadyExistsException;

public class Main extends JavaPlugin {

    private VUtils vUtils;

    @Override
    public void onEnable() {
        try {
            vUtils = new VUtils(this);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
            //Bukkit.getPluginManager().disablePlugin(this);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            //TODO disable plugin
        }
        registerCommands();
    }

    @Override
    public void onDisable() {
        vUtils.getTimerHandler().pause();
        vUtils.getConfigFile().setTimerTime(vUtils.getTimerHandler().getTime());
        vUtils.getConfigFile().save();
    }

    private void registerCommands() {
        getCommand("timer").setExecutor(new TimerCommand());
    }

    public static void sendPrefixedMessage(CommandSender receiver, String message) {
        receiver.sendMessage(VUtils.prefix + message);
    }

    public static void sendCommandUsageMessage(CommandSender receiver, String message) {
        receiver.sendMessage(VUtils.prefix + message);
    }
}
