package de.tschallacka.spigot.countdowncommand;

import de.tschallacka.spigot.countdowncommand.config.Tools;
import de.tschallacka.spigot.countdowncommand.event.PlayerCommandListener;
import de.tschallacka.spigot.countdowncommand.timer.TaskManager;
import de.tschallacka.spigot.countdowncommand.timer.TimeTracker;
import org.bukkit.plugin.java.JavaPlugin;

public final class CountdownCommand extends JavaPlugin {

    TaskManager countdownTimer;
    @Override
    public void onEnable()
    {
        Tools.setup(this);
        getServer().getPluginManager().registerEvents(new PlayerCommandListener(), Tools.plugin);
        countdownTimer = new TaskManager(new TimeTracker(), 20);
        countdownTimer.startTask();
    }

    @Override
    public void onDisable()
    {
        countdownTimer.stopTask();
        Tools.tearDown();
    }
}
