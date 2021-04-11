package de.tschallacka.spigot.countdowncommand.command;

import de.tschallacka.spigot.countdowncommand.config.Configuration;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandTimer
{
    protected final UUID id;
    protected final String command;
    protected final Location location;
    protected int seconds;
    protected boolean isGreenlighted;
    protected boolean hasRun;
    protected final CommandWatcher watcher;


    public CommandTimer(UUID id, Location location, String command, CommandWatcher watcher)
    {
        this.id = id;
        this.command = command;
        this.seconds = watcher.getSecondsTimer();
        this.isGreenlighted = false;
        this.location = location;
        this.hasRun = false;
        this.watcher = watcher;
    }

    public synchronized void markAsExecuted()
    {
        this.hasRun = true;
        CommandManager.instance().remove(this);
    }
    public synchronized boolean isAllowedToExecuteCommand()
    {
        return !this.hasRun && !this.isGreenlighted && this.seconds <= 0;
    }
    public synchronized boolean hasRun()
    {
        return this.hasRun;
    }

    public synchronized void countdown()
    {
        if(this.hasRun() || this.isRunningValidCommand()) return;

        this.seconds--;
        this.sendCountdown();
    }

    public boolean isSameLocation(Location loc)
    {
        return loc != null
                && Double.doubleToLongBits(loc.getX()) == Double.doubleToLongBits(location.getX())
                && Double.doubleToLongBits(loc.getY()) == Double.doubleToLongBits(location.getY())
                && Double.doubleToLongBits(loc.getZ()) == Double.doubleToLongBits(location.getZ());
    }

    protected synchronized void sendCountdown()
    {
        if(this.hasRun() || this.isRunningValidCommand()) return;

        Player player = Bukkit.getPlayer(id);
        if(player != null && this.isSameLocation(player.getLocation())) {
            if(seconds >= 0) {
                TextComponent msg = new TextComponent((this.seconds + 1) + "...");
                msg.setColor(ChatColor.GOLD);
                player.spigot().sendMessage(msg);
            }
            else {
                this.run();
            }
        }
        else {
            if(player != null) {
                TextComponent message = new TextComponent(watcher.getAbortedMessage());
                message.setColor(ChatColor.RED);
                player.spigot().sendMessage(message);
            }
            this.markAsExecuted();
        }
    }

    public UUID getId()
    {
        return id;
    }

    public synchronized void run()
    {
        if(!this.isRunningValidCommand()) {
            this.isGreenlighted = true;
            Player player = Bukkit.getPlayer(id);
            if (player != null) {
                Bukkit.getServer().dispatchCommand(player, command.substring(1));
            }
        }
    }

    public synchronized boolean isRunningValidCommand()
    {
        return this.isGreenlighted;
    }

    public void sendAbortMessage()
    {
        Player player = Bukkit.getPlayer(id);
        if(player != null) {
            TextComponent message = new TextComponent(watcher.getAbortedMessage());
            message.setColor(ChatColor.RED);
            player.spigot().sendMessage(message);
        }
    }
}
