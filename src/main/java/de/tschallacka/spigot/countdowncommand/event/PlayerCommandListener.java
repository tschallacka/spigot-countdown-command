package de.tschallacka.spigot.countdowncommand.event;


import de.tschallacka.spigot.countdowncommand.command.CommandManager;
import de.tschallacka.spigot.countdowncommand.command.CommandTimer;
import de.tschallacka.spigot.countdowncommand.command.CommandWatcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.Optional;

public class PlayerCommandListener implements Listener
{
    ArrayList<String> timeoutCommands;
    public PlayerCommandListener()
    {
        timeoutCommands = new ArrayList<>();
        timeoutCommands.add("/fliegen");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player player = e.getEntity();
        CommandManager.instance().removeTimerWithAbortMessage(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerDisconnenct(PlayerQuitEvent e)
    {
        Player player = e.getPlayer();
        CommandManager.instance().removeTimer(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerDisconnenct(PlayerTeleportEvent e)
    {
        Player player = e.getPlayer();
        CommandManager.instance().removeTimerWithAbortMessage(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e)
    {
        if(e.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) e.getEntity();
            CommandManager.instance().removeTimerWithAbortMessage(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerDrown(EntityAirChangeEvent e)
    {
        if(e.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) e.getEntity();
            CommandManager.instance().removeTimerWithAbortMessage(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerCommandAttempt(PlayerCommandPreprocessEvent e)
    {
        Player player = e.getPlayer();
        final String message = e.getMessage();
        Optional<CommandWatcher> commandWatcher = CommandManager.instance().getWatcher(message);
        if(commandWatcher.isPresent()) {
            CommandWatcher watcher = commandWatcher.get();
            if(!CommandManager.instance().canRunCommand(player.getUniqueId())) {
                TextComponent msg = new TextComponent(watcher.getWaitMessage());
                msg.setColor(ChatColor.GOLD);
                player.spigot().sendMessage(msg);

                CommandTimer timer = new CommandTimer(player.getUniqueId(), player.getLocation(), message, watcher);
                CommandManager.instance().add(timer);
                e.setCancelled(true);
            }
            else {
                CommandManager.instance().removeTimer(player.getUniqueId());
            }
        }
    }
}
