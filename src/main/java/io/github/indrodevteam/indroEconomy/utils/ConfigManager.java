package io.github.indrodevteam.indroEconomy.utils;

import io.github.indrodevteam.indroEconomy.IndroEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {
    public final YamlConfiguration config;
    private final String resourceName, filePath;

    public ConfigManager(String resourceName, boolean updateChecks) {
        if (!resourceName.endsWith(".yml")) {
            resourceName = resourceName + ".yml";
        }

        this.resourceName = resourceName;

        File f = new File(IndroEconomy.getInstance().getDataFolder() + File.separator + resourceName);
        this.filePath = f.getPath();

        if (!f.exists()) {
            IndroEconomy.getInstance().saveResource(resourceName, false);
        }

        config = YamlConfiguration.loadConfiguration(f);

        if (updateChecks) {
            updateFromDefaultSave();
        }

    }

    /**
     * Used to load / create a config file where the resource name is different from
     * the destination path
     *
     * @param resourceName The name of the resource within the jar file
     * @param filePath     The path to save the resource to
     */
    public ConfigManager(String resourceName, String filePath) {
        if (!resourceName.endsWith(".yml")) {
            resourceName = resourceName + ".yml";
        }

        this.resourceName = resourceName;

        if (!filePath.endsWith(".yml")) {
            filePath = filePath + ".yml";
        }
        this.filePath = IndroEconomy.getInstance().getDataFolder().getPath() + File.separator + filePath;
        File f = new File(IndroEconomy.getInstance().getDataFolder(), filePath);

        if (!f.exists()) {
            saveResource(resourceName, this.filePath, false);
        }
        config = YamlConfiguration.loadConfiguration(f);

    }

    public void save() {
        save(true);
    }

    public void save(boolean log) {
        if (log) {
            Bukkit.getLogger().info("Saving new values to " + filePath);
        }

        File f = new File(filePath);
        try {
            config.save(f);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config to " + f, ex);
        }
    }

    public void updateFromDefaultSave() {
        updateFromDefaultSave(true);
    }

    public void updateFromDefaultSave(boolean log) {

        Logger logger = Bukkit.getLogger();

        if (log) {
            logger.info("[IndroEconomy] Checking if the file " + resourceName + " is up to date");
        }

        List<String> changes = updateFileConfig(IndroEconomy.getInstance().getResource(resourceName));

        if (log) {
            if (changes.isEmpty()) {
                logger.info("[IndroEconomy] File is up to date");
            } else {
                logger.info("[IndroEconomy] ==================================================================");
                logger.info("[IndroEconomy] File is not updated, adding values under the following references:");

                for (String str : changes) {
                    logger.info("[IndroEconomy] - " + str);
                }

                logger.info("[IndroEconomy] " + resourceName
                        + " is now updated to the latest version, thank you for using IndroEconomy");
                logger.info("[IndroEconomy] ==================================================================");

            }
        }

        if (!changes.isEmpty()) {
            save(false);
        }

    }

    private @NotNull List<String> updateFileConfig(@NotNull InputStream input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        return updateFileConfig(reader);
    }

    private @NotNull List<String> updateFileConfig(@NotNull BufferedReader reader) {

        List<String> addedPaths = new ArrayList<>();

        YamlConfiguration internalConfig = YamlConfiguration.loadConfiguration(reader);
        for (String str : internalConfig.getKeys(true)) {
            if (!config.contains(str)) {
                Object toSave = internalConfig.get(str);
                if (toSave == null || toSave instanceof ConfigurationSection) {
                    continue;
                }

                // saving the new value to the config
                config.set(str, toSave);
                addedPaths.add(str);
            }
        }

        return addedPaths;

    }

    public void saveResource(String resourcePath, String resultPath, boolean replace) {
        if (resourcePath == null || resourcePath.equals(""))
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        if (resultPath == null || resultPath.equals(""))
            throw new IllegalArgumentException("ResultPath cannot be null or empty");

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = IndroEconomy.getInstance().getResource(resourcePath);

        if (in == null)
            throw new IllegalArgumentException(
                    "The embedded resource '" + resourcePath + "' cannot be found in " + IndroEconomy.getInstance().getDataFolder());
        File outFile = new File(resultPath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(resultPath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists())
            outDir.mkdirs();

        try {
            if (!outFile.exists() || replace) {
                if (!outFile.exists()) {
                    outFile.createNewFile();
                }

                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);
                out.close();
                in.close();
            } else {
                IndroEconomy.getInstance().getLogger().log(Level.WARNING, "Could not save " + resourcePath + " to " + outFile
                        + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            IndroEconomy.getInstance().getLogger().log(Level.SEVERE, "Could not save " + resourcePath + " to " + resultPath, ex);
        }
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFilePath() {
        return filePath;
    }

}
