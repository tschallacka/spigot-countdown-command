package de.tschallacka.spigot.countdowncommand.config;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;


public abstract class Config {

    private File yamlFile;
    FileConfiguration config;
    private String fileName;

    protected Config(String fileName)
    {
        this.fileName = fileName;
    }

    public void setup(Plugin plugin)
    {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        this.yamlFile = new File(plugin.getDataFolder(), fileName);

        boolean newlyCreatedFile = false;

        if (!this.yamlFile.exists()) {
            newlyCreatedFile = this.createNewConfigurationFile(plugin);
        }

        this.config = YamlConfiguration.loadConfiguration(yamlFile);

        this.load();

        update();

        if(newlyCreatedFile) {
            this.save();
        }

    }

    private boolean createNewConfigurationFile(Plugin plugin)
    {

        try {
            this.yamlFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Unable to create the configuration file for Unfreeze plugin: " + fileName, e);
        }
        return true;
    }

    public abstract void load();

    public abstract void update();

    public void save()
    {
        Tools.info("Saving default values");
        try {

            config.save(yamlFile);
        } catch (IOException e) {
            Tools.getLogger().severe("Could not save configuration file:" + fileName + "!");
        }
    }

    public void reload()
    {
        config = YamlConfiguration.loadConfiguration(yamlFile);
    }

    public FileConfiguration getConfig()
    {
        return config;
    }

    public String getFileName()
    {
        return fileName;
    }
}

