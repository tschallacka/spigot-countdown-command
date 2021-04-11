package de.tschallacka.spigot.countdowncommand.config;

import de.tschallacka.spigot.countdowncommand.command.CommandManager;
import de.tschallacka.spigot.countdowncommand.command.CommandWatcher;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public final class Configuration extends Config {


    public static String waitMessage;
    public static String commandAborted;
    public static int countdownSeconds;
    private static Configuration instance;



    private Configuration()
    {
        super("config.yml");
    }


    protected String loadString(String name, String default_value)
    {
        String value = getConfig().getString(name, default_value);
        getConfig().set(name, value);
        return value;
    }

    protected int loadInteger(String name, int default_value)
    {
        int value = getConfig().getInt(name, default_value);
        getConfig().set(name, value);
        return value;
    }

    @Override
    public void load()
    {
        this.waitMessage = this.loadString("wait-message", "Bitte nicht bewegen bis die countdown abgelaufen ist.");
        this.commandAborted = this.loadString("aborted-message", "Nur rundstehen ist swierig ne?");
        this.countdownSeconds = this.loadInteger("countdown-duration-in-seconds", 5);
        ConfigurationSection section  = getConfig().getConfigurationSection("commands");
        if(section == null) {
            getConfig().set("commands.server.command", "/server");
            getConfig().set("commands.server.countdown-duration-in-seconds", 5);
            getConfig().set("commands.server.aborted-message", "Warum bist du so nervös?");
            getConfig().set("commands.server.wait-message", "Hande hoch und rure dich nicht! Ich habe ein messer!");

            section  = getConfig().getConfigurationSection("commands");
        }
        final ConfigurationSection process_section = section;
        section.getKeys(false).forEach(k -> this.addCountdownCommand(k, process_section.getConfigurationSection(k)));

    }

    public void addCountdownCommand(String key, ConfigurationSection section)
    {
        String command = section.getString("command");
        if(command == null) throw new RuntimeException("ILLEGAL. no command to listen to found in configuration "+key);

        String aborted = section.getString("aborted-message");
        String waitmessage = section.getString("wait-message");

        if(waitmessage == null) waitmessage = waitMessage;
        if(aborted == null) aborted = commandAborted;
        int countdown = countdownSeconds;

        if(section.contains("countdown-duration-in-seconds")) {
            countdown = section.getInt("countdown-duration-in-seconds");
        }
        CommandManager.instance().addCommandWatcher(
                new CommandWatcher(command,
                                    waitmessage,
                                    aborted,
                                    countdown));
    }



    public static FileConfiguration get()
    {
        return getInstance().config;
    }

    public static Configuration getInstance()
    {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    @Override
    public void update()
    {
        return;
    }

}