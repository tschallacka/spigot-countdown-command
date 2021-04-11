package de.tschallacka.spigot.countdowncommand.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class CommandManager
{
    private static CommandManager instance;
    protected ConcurrentLinkedQueue<CommandTimer> deadWeight;
    protected ConcurrentHashMap<UUID, CommandTimer> queue;
    protected ArrayList<CommandWatcher> commands;

    protected CommandManager()
    {
        queue = new ConcurrentHashMap<UUID, CommandTimer>();
        deadWeight = new ConcurrentLinkedQueue<>();
        this.commands = new ArrayList<>();
    }

    public static CommandManager instance()
    {
        if(instance == null) instance = new CommandManager();
        return instance;
    }

    public void addCommandWatcher(CommandWatcher watcher)
    {
        this.commands.add(watcher);
    }

    public static void destroy()
    {
        instance().deadWeight.clear();
        instance().queue.clear();
        instance = null;
    }

    public void remove(CommandTimer commandTimer)
    {
        deadWeight.add(commandTimer);
    }
    public void add(CommandTimer timer)
    {
        if(queue.containsKey(timer.getId())) {
            queue.remove(timer.getId());
        }
        deadWeight.removeIf(e -> e.getId().equals(timer.getId()));
        queue.put(timer.getId(), timer);
    }

    public void tick()
    {
        int a = 1;
        if(queue.size() > 0) {
            if(deadWeight.size() > 0) {
                deadWeight.forEach(commandTimer -> {
                    CommandTimer timer = queue.remove(commandTimer.getId());
                    if(timer != null) {
                        commandTimer.markAsExecuted();
                    }
                });
                deadWeight.clear();
            }
            queue.values().forEach(e -> e.countdown());
        }
    }

    public boolean canRunCommand(UUID uniqueId)
    {
        CommandTimer timer = queue.get(uniqueId);
        return timer != null && timer.isAllowedToExecuteCommand();
    }

    public void removeTimer(UUID uniqueId)
    {
        CommandTimer timer = queue.remove(uniqueId);
        if(timer != null) {
            timer.markAsExecuted();
        }
    }

    public void removeTimerWithAbortMessage(UUID uniqueId)
    {
        CommandTimer timer = queue.remove(uniqueId);
        if(timer != null) {
            timer.sendAbortMessage();
            timer.markAsExecuted();
        }
    }

    public Optional<CommandWatcher> getWatcher(String message)
    {
        final String lower = message.toLowerCase();
        Optional<CommandWatcher> watcher = commands.stream().filter(c -> lower.startsWith(c.getCommand())).findFirst();
        return watcher;
    }
}
