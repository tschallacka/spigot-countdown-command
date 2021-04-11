package de.tschallacka.spigot.countdowncommand.timer;


import de.tschallacka.spigot.countdowncommand.command.CommandManager;
import de.tschallacka.spigot.countdowncommand.config.Tools;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeTracker extends BukkitRunnable
{


    public void run()
    {
        CommandManager.instance().tick();
    }


}
