package de.tschallacka.spigot.countdowncommand.timer;

import de.tschallacka.spigot.countdowncommand.config.Tools;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Manages a Bukkit runnable.
 */
public class TaskManager
{
    BukkitTask task;
    final BukkitRunnable runnable;
    final int timer;

    /**
     * @param managed The BukkitRunnable to manage with this Manager
     * @param timer Time in ticks this should run.
     */
    public TaskManager(BukkitRunnable managed, int timer)
    {
        this.runnable = managed;
        this.timer = timer;
    }

    public BukkitTask startTask()
    {
        this.task = runnable.runTaskTimer(Tools.plugin, timer, timer);
        return this.task;
    }

    public void stopTask()
    {
        Bukkit.getScheduler().cancelTask(this.task.getTaskId());
    }
}
