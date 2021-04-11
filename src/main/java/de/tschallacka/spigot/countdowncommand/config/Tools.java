package de.tschallacka.spigot.countdowncommand.config;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class Tools
{
    public static JavaPlugin plugin;


    public static void setup(JavaPlugin javaPlugin)
    {
        plugin = javaPlugin;

        setupConfig();


    }

    public static void info(String o)
    {
        plugin.getLogger().info(o);
    }

    public static Logger getLogger()
    {
        return plugin.getLogger();
    }

    public static void setupConfig()
    {
        Configuration.getInstance().setup(plugin);
    }

    public static void error(String message, Throwable e) throws RuntimeException
    {
        getLogger().warning(e.getMessage());
        throw new RuntimeException(message, e);
    }



    public static void tearDown()
    {

    }
}